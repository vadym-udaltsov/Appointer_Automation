package com.bot.processor.impl.general;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Currency;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class ViewPriceProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Context context = request.getContext();
        Department department = request.getDepartment();
        List<CustomerService> services = department.getServices();
        if (services.stream().allMatch(s -> s.getPrice() == 0)) {
            ContextUtils.resetLocationToStep(context, Constants.Processors.SALON_INFO_USER);
            return MessageUtils.buildCustomKeyboardHolders("Price is not ready", Constants.SALON_INFO_BUTTONS,
                    KeyBoardType.TWO_ROW, true);
        }
        List<LString> messages = new ArrayList<>();
        messages.add(LString.builder().title(Constants.STAR + " Our prices " + Constants.STAR).build());
        for (CustomerService service : services) {
            messages.add(LString.builder()
                    .title(Constants.PIN + " " + service.getName() + " - " + service.getPrice() +
                            Currency.getSignFromTitle(department.getCurrency()))
                    .build());
        }

        boolean isAdmin = department.getAdmins().contains(context.getPhoneNumber());
        String step = isAdmin
                ? Constants.Processors.ADMIN_PROFILE
                : Constants.Processors.SALON_INFO_USER;
        ContextUtils.resetLocationToStep(context, step);
        return MessageUtils.buildCustomKeyboardHolders("", isAdmin
                        ? Constants.ADMIN_PROFILE_BUTTONS
                        : Constants.SALON_INFO_BUTTONS,
                KeyBoardType.TWO_ROW, messages, true);
    }
}
