package com.bot.processor.impl;

import com.bot.model.CommandType;
import com.bot.model.Context;
import com.bot.model.Strategy;
import com.bot.processor.IProcessor;
import com.bot.processor.IProcessorFactory;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.MessageUtils;
import com.bot.util.StrategyProvider;
import com.commons.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public class ProcessorFactory implements IProcessorFactory {

    private final Map<CommandType, IProcessor> processorsMap;
    private IContextService contextService;

    public ProcessorFactory(Map<CommandType, IProcessor> processorsMap, IContextService contextService) {
        this.processorsMap = processorsMap;
        this.contextService = contextService;
    }

    @Override
    public IProcessor getProcessor(Update update, Context context) {
        if (context == null) {
            return processorsMap.get(CommandType.ASK_LANGUAGE);
        }
        String commandKey = MessageUtils.getTextFromUpdate(update);
        System.out.println("Command key ---------------- " + commandKey);
        if (Constants.HOME.equalsIgnoreCase(commandKey)) {
            contextService.resetLocationToDashboard(context);
            return processorsMap.get(CommandType.SET_CONT_START_DASH);
        }
        List<String> location = context.getNavigation();
        if (Constants.BACK.equals(commandKey)) {
            contextService.removeLastLocation(context);
            location.remove(location.size() - 1);
        }
        Strategy currentStrategy = StrategyProvider.getStrategyByLocationAndKey(location, commandKey);
        System.out.println("Current Strategy name --------------------- " + currentStrategy.getName());
        IProcessor processor = resolveProcessor(currentStrategy);
        if (processor == null) {
            throw new IllegalStateException("Processor not found for key " + commandKey);
        }
        if (Constants.BACK.equals(commandKey)) {
            return processor;
        }
        contextService.updateLocation(context, currentStrategy.getName());
        context.getNavigation().add(currentStrategy.getName());
        return processor;
    }

    private IProcessor resolveProcessor(Strategy strategy) {
        String commandTypeName = strategy.getName();
        CommandType commandType = CommandType.fromValue(commandTypeName);
        return processorsMap.get(commandType);
    }

    public void setContextService(IContextService contextService) {
        this.contextService = contextService;
    }
}
