package com.bot.processor.impl.appointment.create;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IAppointmentService;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.DateUtils;
import com.commons.model.Department;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
public class CreateAppointmentFirstStepProcessor extends AbstractGetCalendarProcessor implements IProcessor {

    private final IContextService contextService;

    public CreateAppointmentFirstStepProcessor(IAppointmentService appointmentService, IContextService contextService) {
        super(appointmentService);
        this.contextService = contextService;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        Context context = request.getContext();
        int numberOfCurrentMonth = DateUtils.getNumberOfCurrentMonth(department);
        ContextUtils.setStringParameter(context, Constants.MONTH, String.valueOf(numberOfCurrentMonth));
        contextService.update(context);
        return buildResponse(department, false, StringUtils.EMPTY);
    }
}
