package com.bot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public enum MessageTemplate {
    APPOINTMENT_ALL_FIELDS(List.of("date", "time", "service", "specialist")),
    APPOINTMENT_TIME_SERVICE_CLIENT(List.of("time", "service", "clientWithPhone")),
    DAY_OFF_ALL_FIELDS(List.of("date", "time", "specialist")),
    APPOINTMENT_WITHOUT_DATE_FIELD(List.of("service", "specialist", "time"));

    private final List<String> fields;

    public void buildMessages(List<LString> messagesToLocalize, Map<String, LString> messagesMap) {
        for (String field : this.fields) {
            LString message = messagesMap.get(field);
            if (message != null) {
                messagesToLocalize.add(message);
            }
        }
    }
}
