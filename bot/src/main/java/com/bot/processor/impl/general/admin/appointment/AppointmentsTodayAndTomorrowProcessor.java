package com.bot.processor.impl.general.admin.appointment;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.service.IAppointmentService;
import com.commons.utils.DateUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AppointmentsTodayAndTomorrowProcessor extends AppointmentsAdminProcessor implements IProcessor {

    public AppointmentsTodayAndTomorrowProcessor(IAppointmentService appointmentService, IContextService contextService) {
        super(appointmentService, contextService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Message message = update.getMessage();
        Department department = request.getDepartment();
        long currentDate = DateUtils.nowZone(department);
        String textFromUpdate = MessageUtils.getTextFromUpdate(update);
        ZonedDateTime nowZdt = DateUtils.nowZoneDateTime(department);
        if (textFromUpdate.equals("Tomorrow")) {
            currentDate = currentDate + TimeUnit.DAYS.toSeconds(1);
            nowZdt.plusDays(1);
        }
        context.getParams().put(Constants.SELECTED_YEAR, nowZdt.getYear());
        String selectedDay = DateUtils.getDayTitle(currentDate, department);
        BigDecimal selectedMonth = BigDecimal.valueOf(Integer.parseInt(DateUtils.getMonthTitle(currentDate, department)));
        message.setText(selectedDay);
        update.setMessage(message);
        request.setUpdate(update);
        context.getParams().put(Constants.MONTH, selectedMonth);
        request.setContext(context);
        return buildResponse(request);
    }

    @Override
    protected void resetLocationToDashboard(Context context) {
    }
}
