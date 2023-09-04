package com.bot.processor.impl.general.admin.dayoff.view.period;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.MessageTemplate;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.admin.dayoff.DayOffFourthStepProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Appointment;
import com.commons.model.Department;
import com.commons.service.IAppointmentService;
import com.commons.utils.DateUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class ViewPeriodDayOffFourthStepProcessor extends DayOffFourthStepProcessor implements IProcessor {

    public ViewPeriodDayOffFourthStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildCommonResponse(request);
    }

    @Override
    protected List<MessageHolder> buildCustomResponse(ProcessRequest request, int finishDay) {
        Department department = request.getDepartment();
        Context context = request.getContext();

        int finishYear = ContextUtils.getIntParam(context, Constants.SELECTED_YEAR);
        int finishMonth = ContextUtils.getIntParam(context, Constants.SELECTED_MONTH);

        int startMonth = ContextUtils.getIntParam(context, "startMonth");
        int startDay = ContextUtils.getIntParam(context, "startDay");
        int startYear = ContextUtils.getIntParam(context, "startYear");

        String selectedSpecialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);

        long appStartDate = DateUtils.getStartOrEndOfDayWithYear(startYear, startMonth, startDay, false, department);
        long appFinishDate = DateUtils.getStartOrEndOfDayWithYear(finishYear, finishMonth, finishDay, true, department);

        List<Appointment> dayOffs = appointmentService.getAppointmentsBySpecialist(department, selectedSpecialist,
                appStartDate, appFinishDate);
        dayOffs.removeIf(a -> !Constants.DAY_OFF.equals(a.getService()));

        return getHolders(dayOffs, request);
    }

    protected List<MessageHolder> getHolders(List<Appointment> appointments, ProcessRequest request) {
        Context context = request.getContext();
        Department department = request.getDepartment();
        List<LString> messagesToLocalize = new ArrayList<>();
        if (appointments == null || appointments.size() == 0) {
            messagesToLocalize.add(LString.builder().title("You have no days off for selected period").build());
            ContextUtils.resetLocationToStep(context, "periodDayOffStart");
            return MessageUtils.buildCustomKeyboardHolders("", Constants.ADMIN_APPOINTMENT_BUTTONS,
                    KeyBoardType.TWO_ROW,messagesToLocalize, true);
        }
        messagesToLocalize.add(LString.builder().title("Your days off:").build());
        messagesToLocalize.add(LString.empty());
        for (Appointment appointment : appointments) {
            MessageUtils.fillMessagesToLocalize(messagesToLocalize, appointment, null,
                    MessageTemplate.DAY_OFF_ALL_FIELDS, department);
            messagesToLocalize.add(LString.empty());
        }
        ContextUtils.resetLocationToStep(context, "periodDayOffStart");
        return MessageUtils.buildCustomKeyboardHolders("", Constants.ADMIN_APPOINTMENT_BUTTONS,
                KeyBoardType.TWO_ROW,messagesToLocalize, true);
    }
}
