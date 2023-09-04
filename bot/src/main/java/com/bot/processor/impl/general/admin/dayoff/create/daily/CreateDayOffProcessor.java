package com.bot.processor.impl.general.admin.dayoff.create.daily;

import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.MessageTemplate;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Appointment;
import com.commons.model.Department;
import com.commons.service.IAppointmentService;
import lombok.RequiredArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CreateDayOffProcessor {

    private final IAppointmentService appointmentService;
    private final IContextService contextService;

    protected List<MessageHolder> createDayOff(Department department, Context context, String specialist, int year,
                                               int month, int day, int hour, int minute, int duration) {
        long appointmentDate = ZonedDateTime.of(year, month, day, hour, minute, 0, 0,
                ZoneId.of(department.getZone())).toEpochSecond();
        Appointment appointment = Appointment.builder()
                .id(String.format("%s::%s", specialist, department.getId()))
                .service(Constants.DAY_OFF)
                .specialist(specialist)
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
        MessageUtils.fillMessagesToLocalize(messagesToLocalize, appointment, context, MessageTemplate.DAY_OFF_ALL_FIELDS, department);
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        return List.of(MessageUtils.buildDashboardHolder("", messagesToLocalize, strategyKey));
    }
}
