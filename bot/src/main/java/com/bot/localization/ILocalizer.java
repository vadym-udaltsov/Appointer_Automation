package com.bot.localization;

import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface ILocalizer {

    void localizeResponseMessage(List<MessageHolder> holders, Context context);

    void localizeResponseButtons(SendMessage method, Context context);

    void localizeRequest(Update update, Context context);

    String localizeMessage(List<LString> lines, Context context);
}
