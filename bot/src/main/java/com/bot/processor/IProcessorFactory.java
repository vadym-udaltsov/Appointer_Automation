package com.bot.processor;

import com.bot.model.Context;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public interface IProcessorFactory {

    IProcessor getProcessor(Update update, Context context);
}
