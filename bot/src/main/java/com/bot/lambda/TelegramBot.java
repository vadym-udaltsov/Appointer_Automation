package com.bot.lambda;

import com.bot.localization.ILocalizer;
import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.IProcessorFactory;
import com.bot.service.IContextService;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.service.IDepartmentService;
import com.commons.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final IProcessorFactory factory;
    private IContextService contextService;
    private final IDepartmentService departmentService;
    private final ILocalizer localizer;
    private Department department;
    private String botToken;

    public TelegramBot(IProcessorFactory factory, IContextService contextService, IDepartmentService departmentService,
                       ILocalizer localizer) {
        this.factory = factory;
        this.contextService = contextService;
        this.departmentService = departmentService;
        this.localizer = localizer;
    }

    public void processBotMessage(String departmentId, Update update) {
        department = departmentService.getDepartmentById(departmentId);
        botToken = department.getToken();
        onUpdateReceived(update);
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Received update --------------------------------------- " + JsonUtils.convertObjectToString(update));
        long userId = MessageUtils.getUserIdFromUpdate(update);
        try {
            Context context = contextService.getContext(update, department.getId());
            log.info("Got context --------------------------------------- " + JsonUtils.convertObjectToString(context));
            localizer.localizeRequest(update, context);
            IProcessor processor = factory.getProcessor(update, context, department);
            ProcessRequest request = ProcessRequest.builder()
                    .update(update)
                    .context(context)
                    .department(department)
                    .build();
            List<MessageHolder> result = processor.processRequest(request);
            localizer.localizeResponseMessage(result, context);
            for (MessageHolder holder : result) {
                SendMessage method = MessageUtils.buildMessage(holder, userId);
                contextService.updateContext(context);
                localizer.localizeResponseButtons(method, context);
                execute(method);
            }
            contextService.updateContext(context);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
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

    public void setContextService(IContextService contextService) {
        this.contextService = contextService;
    }

    public IProcessorFactory getFactory() {
        return factory;
    }
}
