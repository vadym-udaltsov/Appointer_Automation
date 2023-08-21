package com.bot.processor.impl.general.user.appointment.cancel;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;

public class CancelAppointmentThirdStep {

    protected List<MessageHolder> buildResponse(ProcessRequest request) {
        Context context = request.getContext();
        Update update = request.getUpdate();
        String appointmentTitle = MessageUtils.getTextFromUpdate(update);

        if (!Constants.BACK.equals(appointmentTitle)) {
            Map<String, Object> params = context.getParams();
            Object selectedTitle = params.get(appointmentTitle);
            if (selectedTitle == null) {
                ContextUtils.setPreviousStep(context);
                List<String> availableAppointments = (List<String>) context.getParams().get(Constants.AVAILABLE_APPOINTMENTS);
                BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(availableAppointments);
                return List.of(MessageUtils.holder("Select option from proposed", ButtonsType.KEYBOARD, holderRequest));
            }
            String selectedAppointment = (String) selectedTitle;
            params.put(Constants.SELECTED_APPOINTMENT, selectedAppointment);
        }

        BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(List.of(Constants.SUBMIT));
        return List.of(MessageUtils.holder("Submit appointment deletion", ButtonsType.KEYBOARD, holderRequest));
    }
}
