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
import com.bot.util.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CreateAppointmentFourthStepProcessor implements IProcessor {

    private final IContextService contextService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();

        String serviceName = MessageUtils.getTextFromUpdate(update);
        List<String> availableServices = (List<String>) context.getParams().get(Constants.AVAILABLE_SERVICES);
        if (!availableServices.contains(serviceName) && !Constants.BACK.equals(serviceName)) {
            contextService.setPreviousStep(context);
            BuildKeyboardRequest holderRequest = buildRequest(availableServices);
            return List.of(MessageUtils.holder("Select service from proposed", ButtonsType.KEYBOARD, holderRequest));
        }

        if (Constants.BACK.equals(serviceName)) {
            serviceName = ContextUtils.getStringParam(context, Constants.SELECTED_SERVICE);
        }

        ContextUtils.setStringParameter(context, Constants.SELECTED_SERVICE, serviceName);
        String finalServiceName = serviceName;
        CustomerService service = department.getServices().stream()
                .filter(s -> finalServiceName.equals(s.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Service %s not found.", finalServiceName)));

        String specialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
        List<FreeSlot> convertedSlots = ContextUtils.getSpecialistSlotsConverted(context, specialist);
        List<String> slotTitles = new ArrayList<>();
        boolean wholeDayAvailable = DateUtils.isWholeDayAvailable(department, convertedSlots.get(0));
        if (wholeDayAvailable) {
            slotTitles = DateUtils.getSlotTitles(convertedSlots.get(0), service.getDuration() * 60L, 30);
        } else {
            for (FreeSlot slot : convertedSlots) {
                slotTitles.addAll(DateUtils.getSlotTitles(slot, service.getDuration() * 60L, 15));
            }
        }
        context.getParams().put(Constants.AVAILABLE_SLOTS, slotTitles);
        BuildKeyboardRequest holderRequest = BuildKeyboardRequest.builder()
                .type(KeyBoardType.FOUR_ROW)
                .buttonsMap(MessageUtils.buildButtons(MessageUtils.commonButtons(slotTitles), true))
                .build();
        MessageHolder holder = MessageUtils.holder("Select time", ButtonsType.KEYBOARD, holderRequest);

        contextService.updateContext(context);
        return List.of(holder);
    }

    private BuildKeyboardRequest buildRequest(List<String> availableServices) {
        List<Button> buttons = availableServices.stream()
                .map(s -> Button.builder()
                        .value(s)
                        .build())
                .collect(Collectors.toList());
        return BuildKeyboardRequest.builder()
                .type(KeyBoardType.VERTICAL)
                .buttonsMap(MessageUtils.buildButtons(buttons, false))
                .build();
    }
}
