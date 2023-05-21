package com.bot.util;

import com.bot.model.Context;
import com.bot.model.FreeSlot;
import com.commons.utils.JsonUtils;
import software.amazon.awssdk.services.sqs.endpoints.internal.Value;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContextUtils {

    public static String getStringParam(Context context, String paramName) {
        return (String) context.getParams().get(paramName);
    }

    public static void setStringParameter(Context context, String paramName, String paramValue) {
        context.getParams().put(paramName, paramValue);
    }

    public static List<FreeSlot> getSpecialistSlotsConverted(Context context, String specialist) {
        List<Map<String, Object>> slotsList = (List<Map<String, Object>>) context.getParams().get(specialist);
        return slotsList.stream()
                .map(s -> JsonUtils.parseMapToObject(s, FreeSlot.class))
                .sorted(Comparator.comparingLong(FreeSlot::getStartPoint))
                .collect(Collectors.toList());
    }

    public static void putSlotsToContextParams(Map<String, List<FreeSlot>> slots, Context context) {
        Map<String, Object> params = context.getParams();
        for (Map.Entry<String, List<FreeSlot>> entry : slots.entrySet()) {
            String specId = entry.getKey();
            List<FreeSlot> value = entry.getValue();
            List<Map<String, Object>> freeSlots = value.stream()
                    .map(sl -> {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("specialist", sl.getSpecialist());
                        map.put("startPoint", sl.getStartPoint());
                        map.put("durationSec", sl.getDurationSec());
                        return map;
                    })
                    .collect(Collectors.toList());

            params.put(specId, freeSlots);
        }
    }
}
