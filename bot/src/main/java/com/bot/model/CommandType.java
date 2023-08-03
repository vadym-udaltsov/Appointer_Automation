package com.bot.model;

import com.bot.util.Constants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
@RequiredArgsConstructor
@Getter
public enum CommandType {
    //start
    ASK_LANGUAGE(Constants.Processors.ASK_LANG),
    SET_LANG_ASK_CONT(Constants.Processors.SET_LANG_ASK_CONT),
    SET_CONT(Constants.Processors.SET_CONTACT),
    START_DASH(Constants.Processors.START_DASH),

    //appointment
    CREATE_APP_1(Constants.Processors.START_CREATE_APP),
    CREATE_APP_2(Constants.Processors.SET_APP_DATE_ASK_SPEC),
    CREATE_APP_3(Constants.Processors.SET_SPEC_ASK_SERVICE),
    CREATE_APP_4(Constants.Processors.SET_SERVICE_ASK_SLOT),
    CREATE_APP_5(Constants.Processors.CREATE_APPOINTMENT),

    MY_APP_1(Constants.Processors.MY_APP_1),
    MY_APP_2(Constants.Processors.MY_APP_2),

    CANCEL_APP1(Constants.Processors.CANCEL_APP1),
    CANCEL_APP2(Constants.Processors.CANCEL_APP2),
    CANCEL_APP3(Constants.Processors.CANCEL_APP3),
    CANCEL_APP4(Constants.Processors.CANCEL_APP4),

    //admin
    START_APP_DASH(Constants.Processors.START_APP_DASH),
    ALL_APP_BY_DATE_1(Constants.Processors.GET_ALL_APP_BY_DATE_1),
    ALL_APP_BY_DATE_2(Constants.Processors.GET_ALL_APP_BY_DATE_2),
    ALL_APP_TODAY_TOMORROW(Constants.Processors.GET_ALL_APP_TODAY_TOMORROW),
    GET_ALL_APPOINTMENTS_1(Constants.Processors.GET_ALL_APPOINTMENTS_1),
    GET_ALL_APPOINTMENTS_2(Constants.Processors.GET_ALL_APPOINTMENTS_2),

    DAY_OFF_START(Constants.Processors.DAY_OFF_START),
    DAY_OFF_CREATE1(Constants.Processors.DAY_OFF_CREATE1),
    DAY_OFF_CREATE2(Constants.Processors.DAY_OFF_CREATE2),
    DAY_OFF_CREATE3(Constants.Processors.DAY_OFF_CREATE3),
    DAY_OFF_CREATE4(Constants.Processors.DAY_OFF_CREATE4),
    DAY_OFF_CREATE5(Constants.Processors.DAY_OFF_CREATE5),

    DAY_OFF_VIEW1(Constants.Processors.DAY_OFF_VIEW1),
    DAY_OFF_VIEW2(Constants.Processors.DAY_OFF_VIEW2),
    DAY_OFF_VIEW3(Constants.Processors.DAY_OFF_VIEW3),

    DAY_OFF_CANCEL1(Constants.Processors.DAY_OFF_CANCEL1),
    DAY_OFF_CANCEL2(Constants.Processors.DAY_OFF_CANCEL2),
    DAY_OFF_CANCEL3(Constants.Processors.DAY_OFF_CANCEL3),
    DAY_OFF_CANCEL4(Constants.Processors.DAY_OFF_CANCEL4);


    private final String value;

    public static CommandType fromValue(String value) {
        for (CommandType commandType : values()) {
            if (commandType.getValue().equalsIgnoreCase(value)) {
                return commandType;
            }
        }
        throw new IllegalArgumentException("Command not found for value " + value);
    }
}
