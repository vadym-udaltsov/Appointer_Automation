package com.bot.processor.impl.general.admin.dayoff.create;

import com.bot.model.Appointment;
import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.MessageTemplate;
import com.bot.service.IAppointmentService;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CreateDayOffProcessor {

    private final IAppointmentService appointmentService;
    private final IContextService contextService;

    protected List<MessageHolder> createDayOff(Department department, Context context, String specialist, int year,
                                               int month, int day, int hour, int minute, int duration) {
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute);
        long appointmentDate = localDateTime.toEpochSecond(ZoneOffset.UTC);
        Appointment appointment = Appointment.builder()
                .specialist(specialist)
                .service(Constants.DAY_OFF)
                .userId(0)
                .departmentId(department.getId())
                .date(appointmentDate)
                .duration(duration)
                .build();
        appointmentService.save(appointment);
        contextService.resetLocationToDashboard(context);
        List<LString> messagesToLocalize = new ArrayList<>();
        messagesToLocalize.add(LString.builder().title("Day off created:").build());
        messagesToLocalize.add(LString.empty());
        MessageUtils.fillMessagesToLocalize(messagesToLocalize, appointment, MessageTemplate.DAY_OFF_ALL_FIELDS);
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        return List.of(MessageUtils.buildDashboardHolder("", messagesToLocalize, strategyKey));
    }
}
