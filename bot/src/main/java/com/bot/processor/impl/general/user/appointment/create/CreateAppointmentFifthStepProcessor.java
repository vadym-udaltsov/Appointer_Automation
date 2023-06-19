package com.bot.processor.impl.general.user.appointment.create;

import com.bot.model.Appointment;
import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IAppointmentService;
import com.bot.service.IContextService;
import com.bot.service.ISendMessageService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CreateAppointmentFifthStepProcessor implements IProcessor {

    private final IContextService contextService;
    private final IAppointmentService appointmentService;
    private final ISendMessageService sendMessageService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();
        String specialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
        String serviceName = ContextUtils.getStringParam(context, Constants.SELECTED_SERVICE);
        String month = ContextUtils.getStringParam(context, Constants.MONTH);
        String day = ContextUtils.getStringParam(context, Constants.SELECTED_DAY);
        String timeString = MessageUtils.getTextFromUpdate(update);
        List<String> availableSlots = (List<String>) context.getParams().get(Constants.AVAILABLE_SLOTS);
        if (!availableSlots.contains(timeString)) {
            BuildKeyboardRequest holderRequest = BuildKeyboardRequest.builder().type(KeyBoardType.FOUR_ROW).buttonsMap(MessageUtils.buildButtons(MessageUtils.commonButtons(availableSlots), true)).build();
            MessageHolder holder = MessageUtils.holder("Select time from proposed", ButtonsType.KEYBOARD, holderRequest);
            ContextUtils.setPreviousStep(context);
            return List.of(holder);
        }
        int year = DateUtils.getNumberOfCurrentYear(department);
        String[] timeParts = timeString.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        LocalDateTime localDateTime = LocalDateTime.of(year, Integer.parseInt(month), Integer.parseInt(day), hour, minute);
        long appointmentDate = localDateTime.toEpochSecond(ZoneOffset.UTC);
        CustomerService selectedService = department.getServices().stream().filter(s -> serviceName.equals(s.getName())).findFirst().orElseThrow(() -> new RuntimeException("Could not find service with name " + serviceName));
        Appointment appointment = Appointment.builder().specialist(specialist).service(serviceName).userId(context.getUserId()).departmentId(department.getId()).date(appointmentDate).duration(selectedService.getDuration()).build();
        appointmentService.save(appointment);
        contextService.resetLocationToDashboard(context);
        List<LString> messagesToLocalize = new ArrayList<>();
        messagesToLocalize.add(LString.builder().title("Created appointment:").build());
        messagesToLocalize.add(LString.empty());
        MessageUtils.fillMessagesToLocalize(messagesToLocalize, appointment);
        List<LString> adminMessages = createAdminMessage(messagesToLocalize, context, department);
        sendMessageService.sendNotificationToAdmins(adminMessages, department);
        return List.of(MessageUtils.buildDashboardHolder(messagesToLocalize));
    }

    private List<LString> createAdminMessage(List<LString> messagesToLocalize, Context context, Department department) {
        List<LString> adminMessages = new ArrayList<>();
        adminMessages.add(LString.builder().title(Constants.STAR_SIGN).build());
        adminMessages.addAll(messagesToLocalize);
        adminMessages.add(LString.builder().title("Client: ${client}").placeholders(Map.of("client", context.getName())).build());
        adminMessages.add(LString.builder().title("Phone Number: ${phone}").placeholders(Map.of("phone", context.getPhoneNumber())).build());
        adminMessages.add(LString.builder().title("Department: ${department}").placeholders(Map.of("department", department.getName())).build());
        return adminMessages;
    }
}
