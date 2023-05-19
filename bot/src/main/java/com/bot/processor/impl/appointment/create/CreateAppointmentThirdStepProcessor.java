package com.bot.processor.impl.appointment.create;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CreateAppointmentThirdStepProcessor implements IProcessor {

    private final IContextService contextService;
    private final IProcessor nextStepProcessor;

    public CreateAppointmentThirdStepProcessor(IContextService contextService, IProcessor nextStepProcessor) {
        this.contextService = contextService;
        this.nextStepProcessor = nextStepProcessor;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        log.info("Received request: {}", JsonUtils.convertObjectToString(request));
        Department department = request.getDepartment();
        Update update = request.getUpdate();
        String specId = MessageUtils.getTextFromUpdate(update);
        Context context = request.getContext();
        List<CustomerService> services = department.getServices();
        List<Map<String, Object>> slots = ContextUtils.getSpecialistSlots(context, specId);
        context.getParams().put(Constants.SELECTED_SPEC, specId);
        List<CustomerService> availableServices = services.stream()
                .filter(serv -> slots.stream()
                        .anyMatch(sl -> (int) sl.get("durationSec") > serv.getDuration()))
                .collect(Collectors.toList());
        if (availableServices.size() == 0) {
            throw new RuntimeException("Available services list can not be empty");
        }
        if (availableServices.size() == 1) {
            MessageUtils.setTextToUpdate(update, availableServices.get(0).getName());
            contextService.skipNextStep(context, Constants.ANY);
            return nextStepProcessor.processRequest(request);
        }
        return List.of(MessageUtils.buildDashboardHolder());
    }
}
