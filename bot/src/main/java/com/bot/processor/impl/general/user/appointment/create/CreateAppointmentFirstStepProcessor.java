package com.bot.processor.impl.general.user.appointment.create;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.BusySlot;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CreateAppointmentFirstStepProcessor implements IProcessor {

    private final IProcessor nextStepProcessor;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        List<CustomerService> services = department.getServices();
        Context context = request.getContext();
        Update update = request.getUpdate();
        if (services.size() == 0) {
            throw new RuntimeException("Department should have at least one service");
        }
        if (services.size() == 1) {
            MessageUtils.setTextToUpdate(update, services.get(0).getName());
            ContextUtils.addNextStepToLocation(context, Constants.ANY, department);
            return nextStepProcessor.processRequest(request);
        }
        List<String> serviceNames = services.stream().map(CustomerService::getName).collect(Collectors.toList());
        BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(serviceNames);
        return List.of(MessageUtils.holder("Select service", ButtonsType.KEYBOARD, holderRequest));
    }
}
