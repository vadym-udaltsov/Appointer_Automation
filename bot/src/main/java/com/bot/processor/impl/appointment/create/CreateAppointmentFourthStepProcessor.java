package com.bot.processor.impl.appointment.create;

import com.bot.model.BuildKeyboardRequest;
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
        ContextUtils.setStringParameter(context, Constants.SELECTED_SERVICE, serviceName);
        contextService.update(context);
        CustomerService service = department.getServices().stream()
                .filter(s -> serviceName.equals(s.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Service %s not found.", serviceName)));

        String specialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
        List<Map<String, Object>> slots = ContextUtils.getSpecialistSlots(context, specialist);
        List<FreeSlot> convertedSlots = slots.stream()
                .map(s -> JsonUtils.parseMapToObject(s, FreeSlot.class))
                .sorted(Comparator.comparingLong(FreeSlot::getStartPoint))
                .collect(Collectors.toList());
        List<String> slotTitles = new ArrayList<>();
        boolean wholeDayAvailable = DateUtils.isWholeDayAvailable(department, convertedSlots.get(0));
        if (wholeDayAvailable) {
            slotTitles = DateUtils.getSlotTitles(convertedSlots.get(0), service.getDuration() * 60L, 30);
        } else {
            for (FreeSlot slot : convertedSlots) {
                slotTitles.addAll(DateUtils.getSlotTitles(slot, service.getDuration() * 60L, 15));
            }
        }
        BuildKeyboardRequest holderRequest = BuildKeyboardRequest.builder()
                .type(KeyBoardType.FOUR_ROW)
                .buttonsMap(MessageUtils.buildButtons(MessageUtils.commonButtons(slotTitles), true))
                .build();
        MessageHolder holder = MessageUtils.holder("Select time", ButtonsType.KEYBOARD, holderRequest);

        return List.of(holder);
    }
}
