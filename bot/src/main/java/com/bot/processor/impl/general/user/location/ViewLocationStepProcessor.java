package com.bot.processor.impl.general.user.location;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.ISendMessageService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@RequiredArgsConstructor
public class ViewLocationStepProcessor implements IProcessor {

    private final ISendMessageService sendMessageService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Context context = request.getContext();
        Department department = request.getDepartment();
        Location location = department.getLocation();
        String message = "Location is not set yet";
        if (location.getLatitude() != 0 && location.getLongitude() != 0) {
            message = "Salon location";
            sendMessageService.sendLocationToUser(department, context.getUserId());
        }
        ContextUtils.resetLocationToStep(context, "salonDash");
        return MessageUtils.buildCustomKeyboardHolders(message, Constants.SALON_INFO_BUTTONS,
                KeyBoardType.TWO_ROW, true);
    }
}
