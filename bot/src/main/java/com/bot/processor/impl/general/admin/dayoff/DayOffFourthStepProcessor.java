package com.bot.processor.impl.general.admin.dayoff;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.service.IAppointmentService;
import com.commons.utils.DateUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.ZonedDateTime;
import java.util.List;

public abstract class DayOffFourthStepProcessor extends AbstractGetCalendarPeriodDayOff {

    public DayOffFourthStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
    }

    protected List<MessageHolder> buildCommonResponse (ProcessRequest request) {
        String message = Constants.Messages.SELECT_END_DATE;
        Department department = request.getDepartment();
        Context context = request.getContext();
        Update update = request.getUpdate();
        String selectedDate = MessageUtils.getTextFromUpdate(update);

        List<String> months = DateUtils.monthNames();
        if (months.contains(selectedDate)) {
            ContextUtils.resetLocationToPreviousStep(context);
            int prevMonth = ContextUtils.getIntParam(context, Constants.SELECTED_MONTH);
            int prevYear = ContextUtils.getIntParam(context, Constants.SELECTED_YEAR);
            int yearOffset = DateUtils.getYearOffset(prevMonth, selectedDate);

            int month = months.indexOf(selectedDate) + 1;
            int year = prevYear + yearOffset;
            return buildResponse(department, context, month, message, year);
        }
        List<String> availableDates = (List<String>) context.getParams().get(Constants.AVAILABLE_DATES);
        if (!availableDates.contains(selectedDate)) {
            ContextUtils.resetLocationToPreviousStep(context);
            ZonedDateTime now = DateUtils.nowZoneDateTime(department);
            int month = now.getMonth().getValue();
            int year = now.getYear();
            return buildResponse(department, context, month, Constants.Messages.INCORRECT_DATE, year);
        }
        int finishYear = ContextUtils.getIntParam(context, Constants.SELECTED_YEAR);
        int finishMonth = ContextUtils.getIntParam(context, Constants.SELECTED_MONTH);
        int finishDay = Integer.parseInt(selectedDate);

        int startMonth = ContextUtils.getIntParam(context, "startMonth");
        int startDay = ContextUtils.getIntParam(context, "startDay");
        int startYear = ContextUtils.getIntParam(context, "startYear");

        if (finishYear < startYear
                || (finishYear == startYear && finishMonth < startMonth)
                || (finishYear == startYear && finishMonth == startMonth && finishDay <= startDay)) {
            ContextUtils.resetLocationToPreviousStep(context);
            ZonedDateTime now = DateUtils.nowZoneDateTime(department);
            int month = now.getMonth().getValue();
            int year = now.getYear();
            return buildResponse(department, context, month, "Finish date should be after start date\nSelect " +
                    "correct finish date", year);
        }

        return buildCustomResponse(request, finishDay);
    }

    protected abstract List<MessageHolder> buildCustomResponse(ProcessRequest request, int finishDay);
}
