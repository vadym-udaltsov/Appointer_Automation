package com.bot.lambda;

import com.bot.localization.ILocalizer;
import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.processor.IProcessor;
import com.bot.processor.IProcessorFactory;
import com.bot.service.IContextService;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.service.IDepartmentService;
import com.commons.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final IProcessorFactory factory;
    private final IContextService contextService;
    private final IDepartmentService departmentService;
    private final ILocalizer localizer;
    private Department department;
    private String botToken;

    public void processBotMessage(String departmentId, Update update) {
        department = departmentService.getDepartmentById(departmentId);
        botToken = department.getToken();
        onUpdateReceived(update);
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Received update -------------- " + JsonUtils.convertObjectToString(update));
        long userId = MessageUtils.getUserIdFromUpdate(update);
        try {
            Context context = contextService.getContext(update);
            localizer.localizeRequest(update, context);
            IProcessor processor = factory.getProcessor(update, context);
            System.out.println("processor got ----------- " + processor.getClass().getSimpleName());

            List<MessageHolder> result = processor.processRequest(update, context);
            localizer.localizeResponseMessage(result, context);
            for (MessageHolder holder : result) {
                SendMessage method = MessageUtils.buildMessage(holder, userId);
                localizer.localizeResponseButtons(method, context);
                execute(method);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
//            messages.add(ExceptionHandler.handle(e, userId));
            e.printStackTrace();
        }

    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return "";
    }
}
