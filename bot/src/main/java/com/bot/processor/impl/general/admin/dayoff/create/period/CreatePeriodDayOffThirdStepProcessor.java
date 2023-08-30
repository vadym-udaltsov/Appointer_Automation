package com.bot.processor.impl.general.admin.dayoff.create.period;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.admin.dayoff.AbstractGetCalendarPeriodDayOff;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.utils.DateUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.ZonedDateTime;
import java.util.List;

public class CreatePeriodDayOffThirdStepProcessor extends AbstractGetCalendarPeriodDayOff implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        String message = Constants.Messages.SELECT_START_DATE;
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
            return buildResponse(department, context, months.indexOf(selectedDate) + 1, message, prevYear + yearOffset);
        }
        List<String> availableDates = (List<String>) context.getParams().get(Constants.AVAILABLE_DATES);
        if (!availableDates.contains(selectedDate)) {
            ContextUtils.resetLocationToPreviousStep(context);
            ZonedDateTime nowTime = DateUtils.nowZoneDateTime(department);
            int currentMonth = nowTime.getMonth().getValue();
            int year = nowTime.getYear();
            return buildResponse(department, context, currentMonth, Constants.Messages.INCORRECT_DATE, year);
        }
        int selectedMonth = ContextUtils.getIntParam(context, Constants.SELECTED_MONTH);
        int selectedYear = ContextUtils.getIntParam(context, Constants.SELECTED_YEAR);
        context.getParams().put("startMonth", selectedMonth);
        context.getParams().put("startDay", Integer.parseInt(selectedDate));
        context.getParams().put("startYear", selectedYear);
        return buildResponse(department, context, selectedMonth, Constants.Messages.SELECT_END_DATE, selectedYear);
    }
}
