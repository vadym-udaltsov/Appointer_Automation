package com.bot.processor.impl.general.admin.location.create;

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
import com.commons.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@RequiredArgsConstructor
public class CreateLocationSecondStepProcessor implements IProcessor {

    private final IDepartmentService departmentService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();
        Location location = MessageUtils.getLocationFromUpdate(update);
        if (location == null) {
            ContextUtils.resetLocationToPreviousStep(context);
            return MessageUtils.buildCustomKeyboardHolders("Send your current location", List.of(),
                    KeyBoardType.TWO_ROW, true);
        }
        department.setLocation(location);
        System.out.println("In processor ------------------ " + JsonUtils.convertObjectToString(department));
        departmentService.updateDepartment(department);
        ContextUtils.resetLocationToStep(context, "locationDash");
        return MessageUtils.buildCustomKeyboardHolders("Location added", Constants.ADMIN_APPOINTMENT_BUTTONS,
                KeyBoardType.TWO_ROW, true);
    }
}
