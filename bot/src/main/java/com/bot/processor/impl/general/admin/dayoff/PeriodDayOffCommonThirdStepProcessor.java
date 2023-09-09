package com.bot.processor.impl.general.admin.dayoff;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.admin.dayoff.AbstractGetCalendarPeriodDayOff;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.service.IAppointmentService;
import com.commons.utils.DateUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.ZonedDateTime;
import java.util.List;

public class PeriodDayOffCommonThirdStepProcessor extends AbstractGetCalendarPeriodDayOff implements IProcessor {

    public PeriodDayOffCommonThirdStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        String message = Constants.Messages.SELECT_START_DATE;
        Department department = request.getDepartment();
        Context context = request.getContext();
        Update update = request.getUpdate();
        String selectedDate = MessageUtils.getTextFromUpdate(update);
        List<String> months = DateUtils.monthNames();
        System.out.println("Months ---------------- " + months);
        System.out.println("Selected date --------------------" + selectedDate);
        if (months.contains(selectedDate.toUpperCase())) {
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
            ZonedDateTime nowTime = DateUtils.nowZoneDateTime(department);
            int month = nowTime.getMonth().getValue();
            int year = nowTime.getYear();
            return buildResponse(department, context, month, Constants.Messages.INCORRECT_DATE, year);
        }
        int selectedMonth = ContextUtils.getIntParam(context, Constants.SELECTED_MONTH);
        int selectedYear = ContextUtils.getIntParam(context, Constants.SELECTED_YEAR);
        context.getParams().put("startMonth", selectedMonth);
        context.getParams().put("startDay", Integer.parseInt(selectedDate));
        context.getParams().put("startYear", selectedYear);
        return buildResponse(department, context, selectedMonth, Constants.Messages.SELECT_END_DATE, selectedYear);
    }
}
