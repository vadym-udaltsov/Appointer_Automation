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
import com.commons.model.Specialist;
import com.commons.service.IAppointmentService;
import com.commons.utils.DateUtils;
import com.commons.utils.DepartmentUtils;
import lombok.RequiredArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CreateDayOffProcessor {

    private final IAppointmentService appointmentService;
    private final IContextService contextService;

    protected List<MessageHolder> createDayOff(Department department, Context context, String specialistName, int year,
                                               int month, int day, int hour, int minute, int duration) {
        long appointmentDate = ZonedDateTime.of(year, month, day, hour, minute, 0, 0,
                ZoneId.of(department.getZone())).toEpochSecond();

        Specialist specialist = DepartmentUtils.getSelectedSpecialist(department, specialistName);

        Appointment appointment = Appointment.builder()
                .id(String.format("%s::%s", specialist.getId(), department.getId()))
                .service(Constants.DAY_OFF)
                .userId(0)
                .departmentId(department.getId())
                .date(appointmentDate)
                .expiration(DateUtils.getExpirationDate(department))
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
