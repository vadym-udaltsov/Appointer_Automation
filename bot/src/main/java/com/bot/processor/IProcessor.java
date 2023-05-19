package com.bot.processor;

import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public interface IProcessor {

    List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException;

}
