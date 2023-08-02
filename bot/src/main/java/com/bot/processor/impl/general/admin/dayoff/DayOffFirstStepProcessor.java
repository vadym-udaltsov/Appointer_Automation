package com.bot.processor.impl.general.admin.dayoff;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.model.Specialist;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DayOffFirstStepProcessor {

    private final IProcessor nextStepProcessor;

    public List<MessageHolder> buildFirstStepDayOffResponse(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        Context context = request.getContext();
        Update update = request.getUpdate();
        List<Specialist> specialists = department.getAvailableSpecialists();
        String text = MessageUtils.getTextFromUpdate(update);
        if (Constants.BACK.equals(text) && specialists.size() == 1) {
            ContextUtils.setPreviousStep(context);
            return MessageUtils.buildCustomKeyboardHolders("Select action", List.of("Create", "View", "Delete"),
                    KeyBoardType.TWO_ROW, true);
        }
        if (specialists.size() == 1) {
            MessageUtils.setTextToUpdate(update, specialists.get(0).getName());
            ContextUtils.addNextStepToLocation(context, Constants.ANY, department);
            return nextStepProcessor.processRequest(request);
        }
        List<String> specialistNames = specialists.stream().map(Specialist::getName).collect(Collectors.toList());
        return MessageUtils.buildCustomKeyboardHolders(Constants.Messages.SELECT_SPECIALIST, specialistNames,
                KeyBoardType.VERTICAL, true);
    }
}
