package com.bot.processor.impl.general.admin.appointment;

import com.commons.model.Appointment;
import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.commons.service.IAppointmentService;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.commons.utils.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AppointmentsByDateSecondStepProcessor extends AppointmentsAdminProcessor implements IProcessor {

    public AppointmentsByDateSecondStepProcessor(IAppointmentService appointmentService, IContextService contextService) {
        super(appointmentService, contextService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();
        String selectedDay = MessageUtils.getTextFromUpdate(update);
        int currentMonth = DateUtils.getNumberOfCurrentMonth(department);
        int selectedMonth = ContextUtils.getIntParam(context, Constants.MONTH);

        boolean isNextMonth = currentMonth < selectedMonth;
        long startDate = DateUtils.getStartOfMonthDate(department, isNextMonth);
        long endDate = DateUtils.getEndOfMonthDate(department, isNextMonth);

        List<Appointment> appointments = getAppointmentSupplier(request, startDate, endDate).get();
        Set<String> appointmentsDates = appointments.stream()
                .map(a -> DateUtils.getDayTitle(a.getDate(), department))
                .collect(Collectors.toSet());
        if (Constants.NEXT_MONTH.equals(selectedDay) || Constants.CURRENT_MONTH.equals(selectedDay)
                || Constants.BACK.equals(selectedDay) || Constants.HOME.equals(selectedDay)
                || appointmentsDates.contains(selectedDay)) {
            return buildResponse(request);
        }
        ContextUtils.resetLocationToPreviousStep(context);
        return MessageUtils.buildDatePicker(appointmentsDates, Constants.Messages.INCORRECT_DATE, isNextMonth,
                department, context);
    }
}
