package com.bot.localization;

import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.commons.model.Department;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface ILocalizer {

    void localizeResponseMessage(List<MessageHolder> holders, Context context, Department department);

    void localizeResponseButtons(SendMessage method, Context context, Department department);

    void localizeRequest(Update update, Context context, Department department);

    String localizeMessage(List<LString> lines, Context context, Department department);
}
