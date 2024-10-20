package com.bot.util;

import com.bot.model.Context;
import com.commons.model.FreeSlot;
import com.bot.model.Role;
import com.bot.model.Strategy;
import com.commons.model.Department;
import com.commons.model.DepartmentType;
import com.commons.utils.JsonUtils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContextUtils {

    public static int getIntParam(Context context, String paramName) {
        return ((BigDecimal) context.getParams().get(paramName)).intValue();
    }

    public static long getLongParam(Context context, String paramName) {
        Object paramValue = context.getParams().get(paramName);
        if (paramValue == null) {
            return 0;
        }
        return ((BigDecimal) paramValue).longValue();
    }

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

    public static void addNextStepToLocation(Context context, String nextStepKey, Department department) {
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        Strategy nextStep = StrategyProvider.getStrategyByLocationAndKey(context.getNavigation(), nextStepKey, strategyKey);
        context.getNavigation().add(nextStep.getName());
    }

    public static void resetLocationToPreviousStep(Context context) {
        List<String> navigation = context.getNavigation();
        navigation.remove(navigation.size() - 1);
    }

    public static void resetLocationToDashboard(Context context) {
        context.setParams(Map.of());
        context.setNavigation(List.of(Constants.Processors.ASK_LANG, Constants.Processors.SET_LANG_ASK_CONT,
                Constants.Processors.SET_CONTACT, Constants.Processors.START_DASH));
    }

    public static void resetLocationToStep(Context context, String step) {
        context.setParams(Map.of());
        List<String> navigation = context.getNavigation();
        List<String> newNavigation = navigation.subList(0, navigation.indexOf(step) + 1);
        context.setNavigation(newNavigation);
    }

    public static void resetLocationToStepSaveParams(Context context, String step) {
        List<String> navigation = context.getNavigation();
        List<String> newNavigation = navigation.subList(0, navigation.indexOf(step) + 1);
        context.setNavigation(newNavigation);
    }

    public static String getStrategyKey(Context context, Department department) {
        DepartmentType departmentType = department.getType();
        Role role = getRole(context, department);
        return String.format("%s::%s", departmentType.name(), role.name());
    }

    public static Role getRole(Context context, Department department) {
        String phoneNumber = context.getPhoneNumber();
        List<String> adminsList = department.getAdmins();
        if (phoneNumber != null && !"n/a".equalsIgnoreCase(phoneNumber) && adminsList != null) {
            return adminsList.contains(phoneNumber) ? Role.ADMIN : Role.USER;
        }
        return Role.USER;
    }

}
