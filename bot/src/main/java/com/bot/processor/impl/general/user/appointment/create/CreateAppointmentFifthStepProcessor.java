package com.bot.processor.impl.general.user.appointment.create;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import com.bot.service.ISendMessageService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Appointment;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.service.IAppointmentService;
import com.commons.utils.DateUtils;
import com.commons.utils.JsonUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class CreateAppointmentFifthStepProcessor extends BaseCreateAppointmentProcessor implements IProcessor {

    public CreateAppointmentFifthStepProcessor(IContextService contextService, IAppointmentService appointmentService,
                                               ISendMessageService sendMessageService) {
        super(contextService, appointmentService, sendMessageService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();
        String specialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
        String serviceName = ContextUtils.getStringParam(context, Constants.SELECTED_SERVICE);
        int month = ContextUtils.getIntParam(context, Constants.MONTH);
        String day = ContextUtils.getStringParam(context, Constants.SELECTED_DAY);
        String timeString = MessageUtils.getTextFromUpdate(update);
        List<String> availableSlots = (List<String>) context.getParams().get(Constants.AVAILABLE_SLOT_TITLES);
        if (!availableSlots.contains(timeString)) {
            ContextUtils.resetLocationToPreviousStep(context);
            return MessageUtils.buildCustomKeyboardHolders("Select time from proposed", availableSlots,
                    KeyBoardType.FOUR_ROW, true);
        }
        int year = DateUtils.getNumberOfCurrentYear(department);
        String[] timeParts = timeString.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        LocalDateTime localDateTime = LocalDateTime.of(year, month, Integer.parseInt(day), hour, minute);
        long appointmentDate = ZonedDateTime.of(localDateTime, ZoneId.of(department.getZone())).toEpochSecond();
        CustomerService selectedService = department.getServices().stream()
                .filter(s -> serviceName.equals(s.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find service with name " + serviceName));
        Appointment appointment = Appointment.builder()
                .id(String.format("%s::%s", specialist, department.getId()))
                .service(serviceName)
                .specialist(specialist)
                .userId(context.getUserId())
                .departmentId(department.getId())
                .date(appointmentDate)
                .duration(selectedService.getDuration())
                .build();
        if (department.getAdmins().contains(context.getPhoneNumber())) {
            ContextUtils.setStringParameter(context, "appointment", JsonUtils.convertObjectToString(appointment));
            return MessageUtils.buildCustomKeyboardHolders("Enter client name using keyboard", List.of(),
                    KeyBoardType.TWO_ROW, true);
        }
        return createAppointment(appointment, context, department, null);
    }
}
