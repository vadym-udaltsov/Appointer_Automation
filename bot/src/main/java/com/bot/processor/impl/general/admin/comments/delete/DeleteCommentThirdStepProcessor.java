package com.bot.processor.impl.general.admin.comments.delete;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class DeleteCommentThirdStepProcessor implements IProcessor {

    private final IContextService contextService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        String selectedNumber = MessageUtils.getTextFromUpdate(update);
        Department department = request.getDepartment();
        Context context = request.getContext();
        List<String> availableNumbers = (List) context.getParams().get(Constants.AVAILABLE_NUMBERS);
        if (!availableNumbers.contains(selectedNumber)) {
            ContextUtils.resetLocationToPreviousStep(context);
            return MessageUtils.buildCustomKeyboardHolders("Select number from proposed", availableNumbers,
                    KeyBoardType.VERTICAL, true);
        }
        long selectedContextId = ContextUtils.getLongParam(context, Constants.SELECTED_CONTEXT);
        Context userContext = contextService.getContext(selectedContextId, department.getId());

        if (Constants.ALL.equals(selectedNumber)) {
            userContext.setComments(List.of());
            contextService.updateContext(userContext);
            ContextUtils.resetLocationToStep(context, Constants.Processors.COMMENTS_DASH);
            return MessageUtils.buildCustomKeyboardHolders("All user comments were deleted",
                    Constants.ADMIN_APPOINTMENT_BUTTONS, KeyBoardType.TWO_ROW, List.of(), true);
        }

        String commentToDelete = ContextUtils.getStringParam(context, selectedNumber);
        List<String> comments = userContext.getComments();
        comments.removeIf(commentToDelete::equals);
        contextService.updateContext(userContext);

        if (comments.size() == 0) {
            ContextUtils.resetLocationToStep(context, Constants.Processors.COMMENTS_DASH);
            return MessageUtils.buildCustomKeyboardHolders("All user comments were deleted",
                    Constants.ADMIN_APPOINTMENT_BUTTONS, KeyBoardType.TWO_ROW, List.of(), true);
        }

        List<LString> commentsToDisplay = new ArrayList<>();
        commentsToDisplay.add(LString.builder()
                .title("Comments for user ${user}")
                .placeholders(Map.of("user", userContext.getName()))
                .build());
        commentsToDisplay.add(LString.empty());

        List<String> numbers = new ArrayList<>();
        int number = 1;
        for (String comment : comments) {
            String numberStr = String.valueOf(number);
            numbers.add(numberStr);
            commentsToDisplay.add(LString.builder().title(number + ". " + comment).build());
            commentsToDisplay.add(LString.empty());
            context.getParams().put(numberStr, comment);
            number++;
        }
        numbers.add(Constants.ALL);

        context.getParams().put(Constants.AVAILABLE_NUMBERS, numbers);
        ContextUtils.resetLocationToStepSaveParams(context, "deleteComment2");
        return MessageUtils.buildCustomKeyboardHolders("", numbers, KeyBoardType.VERTICAL,
                commentsToDisplay, true);
    }
}
