package com.bot.processor.impl.start;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class SetContactStartDashboard implements IProcessor {

    private final IContextService contextService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();
        String text = MessageUtils.getTextFromUpdate(update);
        if (Constants.BACK.equals(text) || Constants.HOME.equals(text)) {
            String strategyKey = ContextUtils.getStrategyKey(context, department);
            return List.of(MessageUtils.buildDashboardHolderByKey(strategyKey));
        }
        Contact contact = MessageUtils.getPhoneNumberFromUpdate(update);
        if (contact == null) {
            contextService.removeLastLocation(context);
            return List.of(MessageUtils.getContactsMessageHolder());
        }
        context.setPhoneNumber("+" + contact.getPhoneNumber());
        context.setName(contact.getFirstName() + " " + contact.getLastName());
        return List.of(MessageUtils.buildDashboardHolderByKey(ContextUtils.getStrategyKey(context, department)));
    }
}
