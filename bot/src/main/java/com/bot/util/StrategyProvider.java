package com.bot.util;

import com.bot.model.Strategy;
import com.commons.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StrategyProvider {

    private final static Map<String, Strategy> strategyMap;

    static {
        strategyMap = new HashMap<>();
        strategyMap.put("GENERAL::USER", getStrategyFile("strategy/general/user.json"));
        strategyMap.put("GENERAL::ADMIN", getStrategyFile("strategy/general/admin.json"));
    }

    public static Strategy getStrategyByLocationAndKey(List<String> location, String key, String strategyKey) {
        Strategy current = strategyMap.get(strategyKey);
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

    private static Strategy getCurrentStrategy(Strategy strategy, String location) {
        String name = strategy.getName();
        if (location.equals(name)) {
            return strategy;
        }
        return strategy.getNested().stream()
                .filter(s -> location.equals(s.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Failed to find strategy with name: " + location));
    }

    private static Strategy getStrategyFile(String filePath) {
        InputStream resourceAsStream = StrategyProvider.class.getClassLoader().getResourceAsStream(filePath);
        return JsonUtils.parseInputStreamToObject(resourceAsStream, new TypeReference<>() {
        });
    }
}
