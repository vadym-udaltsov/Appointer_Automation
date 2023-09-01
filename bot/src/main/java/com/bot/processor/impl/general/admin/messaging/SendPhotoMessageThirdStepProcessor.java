package com.bot.processor.impl.general.admin.messaging;

import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import com.bot.service.ISendMessageService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.bot.util.StringUtils;
import com.commons.model.Department;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SendPhotoMessageThirdStepProcessor implements IProcessor {

    private final IContextService contextService;
    private final ISendMessageService sendMessageService;

    public SendPhotoMessageThirdStepProcessor(IContextService contextService, ISendMessageService sendMessageService) {
        this.contextService = contextService;
        this.sendMessageService = sendMessageService;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Context context = request.getContext();
        Update update = request.getUpdate();
        Department department = request.getDepartment();
        String textFromUpdate = MessageUtils.getTextFromUpdate(update);
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        List<LString> messages = new ArrayList<>();
        String submittedPhoto = ContextUtils.getStringParam(context, Constants.PHOTO_ID_FOR_SUBMIT);
        List<Context> contextList = contextService.getUserContextsByDepartment(department);
        List<String> admins = department.getAdmins();
        List<Context> recipients = contextList.stream()
                .filter(r -> !admins.contains(r.getPhoneNumber()))
                .collect(Collectors.toList());

        if (!StringUtils.isBlank(submittedPhoto)) {
            if (!"Submit".equals(textFromUpdate)) {
                List<String> buttons = List.of("Submit");
                List<LString> messageLines = new ArrayList<>();
                messageLines.add(LString.builder().title(Constants.Messages.INCORRECT_ACTION).build());
                messageLines.add(LString.builder().title(Constants.Messages.SUBMIT_MESSAGE_IMAGE).build());
                ContextUtils.resetLocationToPreviousStep(context);
                return List.of(MessageUtils.buildKeyboardHolder("", messageLines, buttons));
            }

            sendMessageService.sendPhotoToUsers(submittedPhoto, recipients, department);
        }

        messages.add(LString.builder().title(Constants.Messages.ALL_MESSAGES_SENT).build());
        messages.add(LString.builder().title(Constants.Messages.SELECT_ACTION).build());
        ContextUtils.resetLocationToDashboard(context);
        return List.of(MessageUtils.buildDashboardHolder("", messages, strategyKey));
    }
}
