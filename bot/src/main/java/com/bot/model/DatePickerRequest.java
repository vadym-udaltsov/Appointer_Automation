package com.bot.model;

import com.commons.model.Department;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DatePickerRequest {
    private Department department;
    private Context context;
    private boolean isNextMonth;
    private String message;
    private List<LString> localizedMessages;
    private String selectedService;
    private String selectedSpecialist;
}
