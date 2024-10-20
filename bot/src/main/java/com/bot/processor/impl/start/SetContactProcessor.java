package com.bot.processor.impl.start;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class SetContactProcessor implements IProcessor {

    private static final String EMPTY = "";

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();
        Contact contact = MessageUtils.getPhoneNumberFromUpdate(update);
        if (contact == null) {
            ContextUtils.resetLocationToPreviousStep(context);
            return List.of(MessageUtils.getContactsMessageHolder());
        }

        String phoneNumber = contact.getPhoneNumber();
        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+" + phoneNumber;
        }
        context.setPhoneNumber(phoneNumber);
        String firstName = contact.getFirstName() == null ? EMPTY : contact.getFirstName();
        String lastName = contact.getLastName() == null ? EMPTY : contact.getLastName();
        String delimiter = lastName.equals(EMPTY) || firstName.equals(EMPTY) ? EMPTY : " ";
        context.setName(firstName + delimiter + lastName);
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        ContextUtils.addNextStepToLocation(context, "startDash", department);
        return List.of(MessageUtils.buildDashboardHolder(Constants.Messages.SELECT_ACTION, List.of(), strategyKey));
    }
}
