package com.bot.processor.impl.general.admin.dayoff.cancel;

import com.commons.model.Appointment;
import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.AppointmentsSecondStepProcessor;
import com.commons.service.IAppointmentService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.commons.utils.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.utils.JsonUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CancelDayOffThirdStepProcessor extends AppointmentsSecondStepProcessor implements IProcessor {

    private final IAppointmentService appointmentService;
    private final IProcessor previousProcessor;

    public CancelDayOffThirdStepProcessor(IAppointmentService appointmentService, IProcessor previousProcessor) {
        super(appointmentService);
        this.appointmentService = appointmentService;
        this.previousProcessor = previousProcessor;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Context context = request.getContext();
        Set<String> availableDates = (Set<String>) context.getParams().get(Constants.AVAILABLE_DATES);
        Update update = request.getUpdate();
        String selectedDay = MessageUtils.getTextFromUpdate(update);
        if (Constants.BACK.equals(selectedDay)) {
            List<String> dayOffTitles = (List<String>) context.getParams().get(Constants.AVAILABLE_APPOINTMENTS);
            return MessageUtils.buildCustomKeyboardHolders("Select day off period", dayOffTitles,
                    KeyBoardType.VERTICAL, true);
        }
        if (!availableDates.contains(selectedDay)) {
            ContextUtils.setPreviousStep(context);
            String selectedSpecialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
            MessageUtils.setTextToUpdate(update, selectedSpecialist);
            return previousProcessor.processRequest(request);
        }

        return buildResponse(request);
    }

    @Override
    protected Supplier<List<Appointment>> getAppointmentSupplier(ProcessRequest request, long start, long finish) {
        Context context = request.getContext();
        String selectedSpecialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
        return () -> {
            List<Appointment> dayOffs = appointmentService.getAppointmentsBySpecialist(request.getDepartment(),
                            selectedSpecialist, start, finish).stream()
                    .filter(a -> Constants.DAY_OFF.equals(a.getService()))
                    .collect(Collectors.toList());
            dayOffs.forEach(a -> context.getParams().put(getDayOffTitle(a), JsonUtils.convertObjectToString(a)));
            List<String> titles = dayOffs.stream().map(this::getDayOffTitle).collect(Collectors.toList());
            context.getParams().put(Constants.AVAILABLE_APPOINTMENTS, titles);
            return dayOffs;
        };
    }

    @Override
    protected List<MessageHolder> getHolders(List<Appointment> appointments, String strategyKey) {
        List<String> dayOffTitles = appointments.stream().map(this::getDayOffTitle)
                .collect(Collectors.toList());
        return MessageUtils.buildCustomKeyboardHolders("Select day off period", dayOffTitles,
                KeyBoardType.VERTICAL, true);
    }

    private String getDayOffTitle(Appointment appointment) {
        long date = appointment.getDate();
        String dateTitle = DateUtils.getDateTitle(date);
        long periodsCount = appointment.getDuration() / 30;
        return dateTitle + ", " + Constants.Numbers.PERIOD_TITLES.get((int) periodsCount - 1);
    }
}
