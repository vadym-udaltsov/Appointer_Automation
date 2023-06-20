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
    CANCEL_APP4(Constants.Processors.CANCEL_APP4);

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
