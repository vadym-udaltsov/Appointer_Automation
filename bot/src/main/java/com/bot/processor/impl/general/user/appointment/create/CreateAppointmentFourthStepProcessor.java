package com.bot.processor.impl.general.user.appointment.create;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.FreeSlot;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CreateAppointmentFourthStepProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();
        String specName = MessageUtils.getTextFromUpdate(update);
        List<String> availableSpecialists = (List<String>) context.getParams().get(Constants.AVAILABLE_SPECIALISTS);
        if (!availableSpecialists.contains(specName) && !Constants.BACK.equals(specName)) {
            ContextUtils.setPreviousStep(context);
            BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(availableSpecialists);
            return List.of(MessageUtils.holder(Constants.Messages.INCORRECT_SPECIALIST, ButtonsType.KEYBOARD, holderRequest));
        }
        if (Constants.BACK.equals(specName)) {
            specName = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
        }
        ContextUtils.setStringParameter(context, Constants.SELECTED_SPEC, specName);
        String selectedService = ContextUtils.getStringParam(context, Constants.SELECTED_SERVICE);

        CustomerService service = department.getServices().stream()
                .filter(s -> selectedService.equals(s.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Service not found: " + selectedService));

        List<FreeSlot> convertedSlots = ContextUtils.getSpecialistSlotsConverted(context, specName);
        List<String> slotTitles = new ArrayList<>();
        boolean wholeDayAvailable = DateUtils.isWholeDayAvailable(department, convertedSlots.get(0));
        if (wholeDayAvailable) {
            slotTitles = DateUtils.getSlotTitles(convertedSlots.get(0), service.getDuration() * 60L, 30);
        } else {
            for (FreeSlot slot : convertedSlots) {
                slotTitles.addAll(DateUtils.getSlotTitles(slot, service.getDuration() * 60L, 15));
            }
        }

        context.getParams().put(Constants.AVAILABLE_SLOT_TITLES, slotTitles);
        BuildKeyboardRequest holderRequest = BuildKeyboardRequest.builder()
                .type(KeyBoardType.FOUR_ROW)
                .buttonsMap(MessageUtils.buildButtons(MessageUtils.commonButtons(slotTitles), true))
                .build();
        MessageHolder holder = MessageUtils.holder("Select time", ButtonsType.KEYBOARD, holderRequest);
        return List.of(holder);
    }
}
