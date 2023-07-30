package com.bot.processor.impl.general.admin.appointment;

import com.bot.model.Appointment;
import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.MessageTemplate;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.AppointmentsSecondStepProcessor;
import com.bot.service.IAppointmentService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AllAppointmentsSecondStepProcessor extends AppointmentsSecondStepProcessor implements IProcessor {
    private final IAppointmentService appointmentService;

    public AllAppointmentsSecondStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
        this.appointmentService = appointmentService;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();
        String selectedDay = MessageUtils.getTextFromUpdate(update);
        int currentMonth = DateUtils.getNumberOfCurrentMonth(department);
        int selectedMonth = ContextUtils.getIntParam(context, Constants.MONTH);
        boolean isNextMonth = currentMonth != selectedMonth;
        long startDate = DateUtils.getStartOfMonthDate(department, isNextMonth);
        long nextMonthEndDate = DateUtils.getEndOfMonthDate(department, isNextMonth);
        List<Appointment> appointments = getAppointmentSupplier(request, startDate, nextMonthEndDate).get();
        Set<String> appointmentsDates = appointments.stream()
                .map(a -> DateUtils.getDayTitle(a.getDate()))
                .collect(Collectors.toSet());
        if (Constants.NEXT_MONTH.equals(selectedDay) || Constants.CURRENT_MONTH.equals(selectedDay)
                || Constants.BACK.equals(selectedDay) || Constants.HOME.equals(selectedDay)
                || appointmentsDates.contains(selectedDay)) {
            return buildResponse(request);
        }
        ContextUtils.setPreviousStep(context);
        return MessageUtils.buildDatePicker(appointmentsDates, Constants.Messages.INCORRECT_DATE, isNextMonth);
    }

    @Override
    protected List<MessageHolder> getHolders(List<Appointment> appointments, String strategyKey) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(appointments.get(0).getDate()), ZoneId.systemDefault());
        String date = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        List<LString> messagesToLocalize = new ArrayList<>();
        messagesToLocalize.add(LString.builder().title("All appointments for ${date}:").placeholders(Map.of("date", date)).build());
        messagesToLocalize.add(LString.empty());

        for (Appointment appointment : appointments) {
            MessageUtils.fillMessagesToLocalize(messagesToLocalize, appointment, MessageTemplate.APPOINTMENT_WITHOUT_DATE_FIELD);
            messagesToLocalize.add(LString.empty());
        }
        messagesToLocalize.add(LString.builder().title("Select action").build());
        return List.of(MessageUtils.buildDashboardHolder("", messagesToLocalize, strategyKey));
    }

    @Override
    protected void resetLocationToDashboard(Context context) {
        ContextUtils.resetLocationToDashboard(context);
    }

    @Override
    protected Supplier<List<Appointment>> getAppointmentSupplier(ProcessRequest request, long start, long finish) {
        Department department = request.getDepartment();
        return () -> appointmentService.getAppointmentsByDepartment(department, start, finish).stream()
                .filter(a -> !Constants.DAY_OFF.equals(a.getService()))
                .collect(Collectors.toList());
    }
}
