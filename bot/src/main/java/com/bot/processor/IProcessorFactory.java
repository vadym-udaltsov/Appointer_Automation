package com.bot.processor;

import com.bot.model.Context;
import com.bot.service.IContextService;
import com.commons.model.Department;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public interface IProcessorFactory {

    IProcessor getProcessor(Update update, Context context, Department department);

    void setContextService(IContextService contextService);
}
