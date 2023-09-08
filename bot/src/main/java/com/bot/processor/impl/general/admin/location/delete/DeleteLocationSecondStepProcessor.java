package com.bot.processor.impl.general.admin.location.delete;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.service.IDepartmentService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@RequiredArgsConstructor
public class DeleteLocationSecondStepProcessor implements IProcessor {

    private final IDepartmentService departmentService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        Update update = request.getUpdate();
        Context context = request.getContext();
        String text = MessageUtils.getTextFromUpdate(update);
        if (!Constants.SUBMIT.equals(text)) {
            ContextUtils.resetLocationToPreviousStep(context);
            BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(List.of(Constants.SUBMIT));
            return List.of(MessageUtils.holder("Select available action", ButtonsType.KEYBOARD, holderRequest));
        }
        Location location = new Location();
        location.setLatitude(0.0);
        location.setLongitude(0.0);
        department.setLocation(location);
        departmentService.updateDepartment(department);
        ContextUtils.resetLocationToStep(context, "locationDash");
        return MessageUtils.buildCustomKeyboardHolders("Location deleted", Constants.ADMIN_APPOINTMENT_BUTTONS,
                KeyBoardType.TWO_ROW, true);
    }
}
