package com.bot.model;

import com.commons.model.Department;
import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
@Builder
public class ProcessRequest {

    private Update update;
    private Context context;
    private Department department;
}
