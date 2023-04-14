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
    SET_CONT_START_DASH(Constants.Processors.SET_CONT_START_DASH),

    //appointment
    START_CREATE_APP(Constants.Processors.START_CREATE_APP),
    SET_APP_DATE_ASK_SPECIALIST(Constants.Processors.SET_APP_DATE_ASK_SPEC),
    MY_APPOINTMENTS(Constants.Processors.MY_APPS);

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
