package com.bot.processor;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public interface IProcessor {

    List<MessageHolder> processRequest(Update update, Context context) throws TelegramApiException;

}
