package com.bot.processor.impl.general.admin.dayoff.cancel.period;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
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

import java.util.List;

public class DeletePeriodDayOffFourthStepProcessor extends DayOffFourthStepProcessor implements IProcessor {

    public DeletePeriodDayOffFourthStepProcessor(IAppointmentService appointmentService) {
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
        appointmentService.delete(dayOffs);
        ContextUtils.resetLocationToDashboard(context);
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        return List.of(MessageUtils.buildDashboardHolder("Period day off was deleted", List.of(), strategyKey));
    }
}
