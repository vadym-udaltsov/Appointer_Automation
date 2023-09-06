package com.bot.processor.impl.general.admin.comments;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
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
public abstract class ViewCommentsSecondStep {

    private final IContextService contextService;

    protected List<MessageHolder> buildResponse(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Department department = request.getDepartment();
        Context context = request.getContext();

        long selectedContextId = ContextUtils.getLongParam(context, Constants.SELECTED_CONTEXT);

        if (selectedContextId == 0) {
            String selectedTitle = MessageUtils.getTextFromUpdate(update);
            List<String> availableTitles = (List<String>) context.getParams().get(Constants.AVAILABLE_TITLES);
            if (!availableTitles.contains(selectedTitle)) {
                ContextUtils.resetLocationToPreviousStep(context);
                return MessageUtils.buildCustomKeyboardHolders("Select client from proposed", availableTitles,
                        KeyBoardType.VERTICAL, true);
            }
            selectedContextId = ContextUtils.getLongParam(context, selectedTitle);
        }

        Context userContext = contextService.getContext(selectedContextId, department.getId());

        List<String> comments = userContext.getComments();
        if (comments == null || comments.size() == 0) {
            String strategyKey = ContextUtils.getStrategyKey(context, department);
            return List.of(MessageUtils.buildDashboardHolder("User does not have comments", List.of(), strategyKey));
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
        String lastMessage = getLastMessage();
        if (!"".equals(lastMessage)) {
            commentsToDisplay.add(LString.builder().title(lastMessage).build());
        }
        numbers.add(Constants.ALL);

        List<String> buttons = getButtons(numbers);

        context.getParams().put(Constants.SELECTED_CONTEXT, userContext.getUserId());
        context.getParams().put(Constants.AVAILABLE_NUMBERS, buttons);

        resetLocation(context);
        return MessageUtils.buildCustomKeyboardHolders("", buttons, getKeyBoard(),
                commentsToDisplay, true);
    }

    protected String getLastMessage() {
        return "";
    }

    protected void resetLocation(Context context) {
    }

    protected abstract KeyBoardType getKeyBoard();

    protected abstract List<String> getButtons(List<String> numbers);
}
