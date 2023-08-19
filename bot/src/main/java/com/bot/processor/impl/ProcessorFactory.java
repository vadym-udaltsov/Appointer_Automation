package com.bot.processor.impl;

import com.bot.model.CommandType;
import com.bot.model.Context;
import com.bot.model.Strategy;
import com.bot.processor.IProcessor;
import com.bot.processor.IProcessorFactory;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.bot.util.StrategyProvider;
import com.commons.model.Department;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
@Slf4j
public class ProcessorFactory implements IProcessorFactory {

    private final Map<CommandType, IProcessor> processorsMap;
    private IContextService contextService;

    public ProcessorFactory(Map<CommandType, IProcessor> processorsMap, IContextService contextService) {
        this.processorsMap = processorsMap;
        this.contextService = contextService;
    }

    @Override
    public IProcessor getProcessor(Update update, Context context, Department department) {
        if (context == null) {
            return processorsMap.get(CommandType.ASK_LANGUAGE);
        }
        if (context.isBlocked()) {
            contextService.resetLocationToDashboard(context);
            return processorsMap.get(CommandType.START_DASH);
        }
        String commandKey = MessageUtils.getTextFromUpdate(update);
        log.info("Command key ---------------- " + commandKey);
        if (Constants.HOME.equalsIgnoreCase(commandKey)) {
            contextService.resetLocationToDashboard(context);
            return processorsMap.get(CommandType.START_DASH);
        }
        List<String> location = context.getNavigation();
        if (Constants.BACK.equals(commandKey)) {
            location.remove(location.size() - 1);
            contextService.updateContext(context);
        }
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        Strategy currentStrategy = StrategyProvider.getStrategyByLocationAndKey(location, commandKey, strategyKey);
        log.info("Current Strategy name --------------------- " + currentStrategy.getName());
        IProcessor processor = resolveProcessor(currentStrategy);
        if (currentStrategy.getName().equals(location.get(location.size() - 1)) || Constants.BACK.equals(commandKey)) {
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
