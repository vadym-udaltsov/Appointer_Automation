package com.bot.processor.impl.general.admin.cancelappointment;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.cancel.CancelAppointmentFourthStep;
import com.bot.service.IContextService;
import com.bot.service.ISendMessageService;
import com.commons.service.IAppointmentService;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class CancelPhoneAppointmentFourthStepProcessor extends CancelAppointmentFourthStep implements IProcessor {

    private final IContextService contextService;

    public CancelPhoneAppointmentFourthStepProcessor(IAppointmentService appointmentService,
                                                     ISendMessageService sendMessageService,
                                                     IContextService contextService) {
        super(appointmentService, sendMessageService);
        this.contextService = contextService;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildResponse(request);
    }

    @Override
    protected void clearClientContext(Context context) {
        contextService.delete(context);
    }
}
