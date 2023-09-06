package com.bot.processor.impl.general.admin.comments.create;

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

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CreateCommentThirdStepProcessor implements IProcessor {

    private final IContextService contextService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();
        String comment = MessageUtils.getTextFromUpdate(update);

        String selectedTitle = ContextUtils.getStringParam(context, Constants.SELECTED_TITLE);
        long selectedContextId = ContextUtils.getLongParam(context, selectedTitle);
        Context userContext = contextService.getContext(selectedContextId, department.getId());

        List<String> comments = userContext.getComments();
        if (comments == null) {
            userContext.setComments(List.of(comment));
        } else {
            comments.add(comment);
        }
        contextService.updateContext(userContext);

        ContextUtils.resetLocationToStep(context, Constants.Processors.COMMENTS_DASH);
        LString message = LString.builder()
                .title("Comment for user ${user} was created")
                .placeholders(Map.of("user", userContext.getName()))
                .build();
        return MessageUtils.buildCustomKeyboardHolders("", Constants.ADMIN_APPOINTMENT_BUTTONS, KeyBoardType.TWO_ROW,
                List.of(message), true);
    }
}
