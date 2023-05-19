package com.bot.util;

import com.bot.model.Context;
import software.amazon.awssdk.services.sqs.endpoints.internal.Value;

import java.util.List;
import java.util.Map;

public class ContextUtils {

    public static String getStringParam(Context context, String paramName) {
        return (String) context.getParams().get(paramName);
    }

    public static void setStringParameter(Context context, String paramName, String paramValue) {
        context.getParams().put(paramName, paramValue);
    }

    public static List<Map<String, Object>> getSpecialistSlots(Context context, String specialist) {
        return (List<Map<String, Object>>) context.getParams().get(specialist);
    }
}
