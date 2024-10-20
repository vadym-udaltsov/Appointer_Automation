package com.bot.processor.impl.general.admin.block;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import com.commons.service.IAppointmentService;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class BlockUserThirdStepProcessor extends AbstractBlockUserThirdStepProcessor implements IProcessor {

    private final IAppointmentService appointmentService;

    public BlockUserThirdStepProcessor(IAppointmentService appointmentService, IContextService contextService) {
        super(contextService);
        this.appointmentService = appointmentService;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildResponse(request);
    }

    @Override
    protected void executeContextAction(Context selectedContext) {
        long userId = selectedContext.getUserId();
        appointmentService.deleteAppointmentsByClientId(userId);
        selectedContext.setBlocked(true);
    }

    @Override
    protected String getSuccessMessage() {
        return "Client was blocked";
    }
}
