package com.bot.processor.impl.general.user.appointment.cancel;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;

public class CancelAppointmentThirdStepProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Context context = request.getContext();
        Update update = request.getUpdate();
        String appointmentTitle = MessageUtils.getTextFromUpdate(update);
        Map<String, Object> params = context.getParams();
        long appointmentDate = (long) params.get(appointmentTitle);
        params.put("selectedAppointmentDate", appointmentDate);

        BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(List.of("Submit"));
        return List.of(MessageUtils.holder("Submit appointment deletion", ButtonsType.KEYBOARD, holderRequest));
    }
}
