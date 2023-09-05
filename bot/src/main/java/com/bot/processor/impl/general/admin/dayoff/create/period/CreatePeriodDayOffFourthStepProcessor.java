package com.bot.processor.impl.general.admin.dayoff.create.period;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.MessageTemplate;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.admin.dayoff.DayOffFourthStepProcessor;
import com.bot.service.IContextService;
import com.bot.service.ISendMessageService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Appointment;
import com.commons.model.Department;
import com.commons.model.Specialist;
import com.commons.service.IAppointmentService;
import com.commons.utils.DateUtils;
import com.commons.utils.DepartmentUtils;
import com.commons.utils.JsonUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CreatePeriodDayOffFourthStepProcessor extends DayOffFourthStepProcessor implements IProcessor {

    private final IAppointmentService appointmentService;
    private final IContextService contextService;
    private final ISendMessageService sendMessageService;

    public CreatePeriodDayOffFourthStepProcessor(IAppointmentService appointmentService, IContextService contextService,
                                                 ISendMessageService sendMessageService) {
        super(appointmentService);
        this.contextService = contextService;
        this.sendMessageService = sendMessageService;
        this.appointmentService = appointmentService;
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
        List<Appointment> appointments = appointmentService.getAppointmentsBySpecialist(department, selectedSpecialist,
                appStartDate, appFinishDate);
        Map<Boolean, List<Appointment>> appointmentsByOrder = appointments.stream()
                .filter(a -> !Constants.DAY_OFF.equals(a.getService()))
                .collect(Collectors.groupingBy(Appointment::isPhoneOrder));
        Map<Long, Context> contextMap = contextService.getContextListByAppointments(appointments)
                .stream()
                .collect(Collectors.toMap(Context::getUserId, Function.identity()));

        List<LString> phoneAppointmentsTitles = fillMessagesWithPhoneAppointments(appointmentsByOrder, contextMap, department);
        List<LString> botAppointmentsTitles = fillMessagesWithBotAppointments(appointmentsByOrder, contextMap, department);

        List<Appointment> botAppointments = appointmentsByOrder.get(Boolean.FALSE);
        sendMessagesToUsers(botAppointments, contextMap, department);
        appointmentService.delete(appointments);

        if (botAppointments != null) {
            List<String> userContexts = botAppointments.stream()
                    .map(a -> JsonUtils.convertObjectToString(contextMap.get(a.getUserId())))
                    .collect(Collectors.toList());
            context.getParams().put("contexts", userContexts);
        }

        String adminMessage = "Day off period was created";
        List<LString> adminMessages = new ArrayList<>();
        adminMessages.addAll(phoneAppointmentsTitles);
        adminMessages.addAll(botAppointmentsTitles);
        if (botAppointmentsTitles.size() != 0) {
            adminMessage = "You can send the reason of cancelling appointments?\nType on keyboard";
        }
        adminMessages.add(LString.builder().title(adminMessage).build());

        ZonedDateTime start = ZonedDateTime.of(startYear, startMonth, startDay, 0, 1, 0,
                0, ZoneId.of(department.getZone()));
        ZonedDateTime finish = ZonedDateTime.of(finishYear, finishMonth, finishDay, 23, 59, 0,
                0, ZoneId.of(department.getZone()));

        createDaysOff(start, finish, selectedSpecialist, department);

        if (botAppointmentsTitles.size() == 0) {
            ContextUtils.resetLocationToDashboard(context);
            String strategyKey = ContextUtils.getStrategyKey(context, department);
            return List.of(MessageUtils.buildDashboardHolder(Constants.Messages.SELECT_ACTION, adminMessages, strategyKey));
        }

        return MessageUtils.buildCustomKeyboardHolders("", List.of(), KeyBoardType.VERTICAL, adminMessages, true);
    }

    private void createDaysOff(ZonedDateTime start, ZonedDateTime finish, String specialistName, Department department) {
        List<Appointment> daysOff = new ArrayList<>();
        Specialist specialist = DepartmentUtils.getSelectedSpecialist(department, specialistName);

        while (start.isBefore(finish)) {
            ZonedDateTime appDate = ZonedDateTime.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(),
                    department.getStartWork(), 0, 0, 0, ZoneId.of(department.getZone()));
            int duration = (department.getEndWork() - department.getStartWork()) * 60;
            Appointment appointment = Appointment.builder()
                    .id(String.format("%s::%s", specialist.getId(), department.getId()))
                    .service(Constants.DAY_OFF)
                    .userId(0)
                    .departmentId(department.getId())
                    .date(appDate.toEpochSecond())
                    .expiration(DateUtils.getExpirationDate(department))
                    .duration(duration)
                    .phoneOrder(false)
                    .build();
            daysOff.add(appointment);
            start = start.plusDays(1);
        }
        appointmentService.createAppointments(daysOff);
    }

    private void sendMessagesToUsers(List<Appointment> botAppointments, Map<Long, Context> contextMap,
                                     Department department) {
        if (botAppointments != null && botAppointments.size() > 0) {
            botAppointments.parallelStream().forEach(a -> {
                List<LString> messages = new ArrayList<>();
                messages.add(LString.builder().title("Your appointment was cancelled").build());
                Context userContext = contextMap.get(a.getUserId());
                MessageUtils.fillMessagesToLocalize(messages, a, userContext,
                        MessageTemplate.APPOINTMENT_ALL_WITH_CLIENT, department);
                sendMessageService.sendNotificationToUsers(messages, List.of(userContext), department);
            });
        }
    }

    private List<LString> fillMessagesWithBotAppointments(Map<Boolean, List<Appointment>> appointmentsByOrder,
                                                          Map<Long, Context> contextMap, Department department) {
        List<LString> messages = new ArrayList<>();
        List<Appointment> botAppointments = appointmentsByOrder.get(Boolean.FALSE);
        if (botAppointments != null && botAppointments.size() > 0) {
            messages.add(LString.builder().title("Cancelled appointments made by bot.").build());
            for (Appointment botAppointment : botAppointments) {
                Context userContext = contextMap.get(botAppointment.getUserId());
                MessageUtils.fillMessagesToLocalize(messages, botAppointment, userContext,
                        MessageTemplate.APPOINTMENT_ALL_WITH_CLIENT, department);
                messages.add(LString.empty());
            }
            messages.add(LString.empty());
        }
        return messages;
    }

    private List<LString> fillMessagesWithPhoneAppointments(Map<Boolean, List<Appointment>> appointmentsByOrder,
                                                            Map<Long, Context> contextMap, Department department) {
        List<LString> phoneAppointmentsTitles = new ArrayList<>();
        List<Appointment> phoneAppointments = appointmentsByOrder.get(Boolean.TRUE);
        if (phoneAppointments != null && phoneAppointments.size() > 0) {
            phoneAppointmentsTitles.add(LString.builder().title("Cancelled appointments made by phone.").build());
            for (Appointment phoneAppointment : phoneAppointments) {
                Context userContext = contextMap.get(phoneAppointment.getUserId());
                MessageUtils.fillMessagesToLocalize(phoneAppointmentsTitles, phoneAppointment, userContext,
                        MessageTemplate.APPOINTMENT_ALL_WITH_CLIENT, department);
                phoneAppointmentsTitles.add(LString.empty());
            }
            phoneAppointmentsTitles.add(LString.empty());
        }
        return phoneAppointmentsTitles;
    }
}
