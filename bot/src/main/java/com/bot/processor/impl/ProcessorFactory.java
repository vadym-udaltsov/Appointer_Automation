package com.bot.processor.impl;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.bot.model.CommandType;
import com.bot.model.Context;
import com.bot.model.Strategy;
import com.bot.processor.IProcessor;
import com.bot.processor.IProcessorFactory;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.MessageUtils;
import com.commons.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public class ProcessorFactory implements IProcessorFactory {

    private final Map<CommandType, IProcessor> processorsMap;
    private final IContextService contextService;
    private final Strategy strategy;

    public ProcessorFactory(Map<CommandType, IProcessor> processorsMap, IContextService contextService) {
        this.processorsMap = processorsMap;
        this.contextService = contextService;
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("strategy.json");
        this.strategy = JsonUtils.parseInputStreamToObject(resourceAsStream, new TypeReference<>() {
        });
    }

    @Override
    public IProcessor getProcessor(Update update, Context context) {
        if (context == null) {
            return processorsMap.get(CommandType.ASK_LANGUAGE);
        }
        String commandKey = MessageUtils.getTextFromUpdate(update);
        if (Constants.HOME.equalsIgnoreCase(commandKey)) {
            contextService.resetLocationToDashboard(context);
            return processorsMap.get(CommandType.SET_CONT_START_DASH);
        }
        List<String> location = context.getNavigation();
        if (Constants.BACK.equals(commandKey)) {
            contextService.removeLastLocation(context);
            location.remove(location.size() - 1);
        }
        Strategy currentStrategy = getStrategyByLocationAndKey(strategy, location, commandKey);
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

    private Strategy getStrategyByLocationAndKey(Strategy strategy, List<String> location, String key) {
        Strategy current = strategy;
        for (String locationName : location) {
            current = getCurrentStrategy(current, locationName);
        }
        if ("Back".equals(key)) {
            return current;
        }

        List<Strategy> nested = current.getNested();
        if (nested.size() == 1) {
            return nested.get(0);
        }
        return nested.stream()
                .filter(s -> key.equals(s.getKey()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Failed to find strategy with key: " + key));
    }

    private Strategy getCurrentStrategy(Strategy strategy, String location) {
        String name = strategy.getName();
        if (location.equals(name)) {
            return strategy;
        }
        return strategy.getNested().stream()
                .filter(s -> location.equals(s.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Failed to find strategy with name: " + location));
    }
}
