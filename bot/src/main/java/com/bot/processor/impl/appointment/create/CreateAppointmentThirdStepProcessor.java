package com.bot.processor.impl.appointment.create;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.Button;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.FreeSlot;
import com.bot.model.KeyBoardType;
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

import java.math.BigDecimal;
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

        List<String> availableSpecialists = (List<String>) context.getParams().get(Constants.AVAILABLE_SPECIALISTS);
        if (!availableSpecialists.contains(specId) && !Constants.BACK.equals(specId)) {
            contextService.setPreviousStep(context);
            BuildKeyboardRequest holderRequest = MessageUtils.getHolderRequest(availableSpecialists);
            return List.of(MessageUtils.holder("Select specialist from proposed", ButtonsType.INLINE, holderRequest));
        }
        if (Constants.BACK.equals(specId)) {
            specId = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
        }
        List<CustomerService> services = department.getServices();
        List<FreeSlot> slots = ContextUtils.getSpecialistSlotsConverted(context, specId);
        List<CustomerService> availableServices = services.stream()
                .filter(serv -> slots.stream()
                        .anyMatch(sl ->  sl.getDurationSec() >= serv.getDuration() * 60L))
                .collect(Collectors.toList());
        if (availableServices.size() == 0) {
            throw new RuntimeException("Available services list can not be empty");
        }

        if (services.size() == 1) {
            MessageUtils.setTextToUpdate(update, availableServices.get(0).getName());
            contextService.skipNextStep(context, Constants.ANY);
            return nextStepProcessor.processRequest(request);
        }

        List<String> availableServiceNames = availableServices.stream()
                .map(CustomerService::getName)
                .collect(Collectors.toList());

        context.getParams().put(Constants.AVAILABLE_SERVICES, availableServiceNames);
        ContextUtils.setStringParameter(context, Constants.SELECTED_SPEC, specId);
        contextService.updateContext(context);
        List<Button> buttons = availableServices.stream()
                .map(cs -> Button.builder()
                        .value(cs.getName())
                        .build())
                .collect(Collectors.toList());
        BuildKeyboardRequest holderRequest = BuildKeyboardRequest.builder()
                .type(KeyBoardType.VERTICAL)
                .buttonsMap(MessageUtils.buildButtons(buttons, false))
                .build();

        return List.of(MessageUtils.holder("Select service", ButtonsType.KEYBOARD, holderRequest));
    }
}
