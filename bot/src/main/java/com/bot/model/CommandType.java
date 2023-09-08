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
    CREATE_APP_6(Constants.Processors.CREATE_APPOINTMENT6),

    MY_APP_1(Constants.Processors.MY_APP_1),
    MY_APP_2(Constants.Processors.MY_APP_2),

    CANCEL_APP1(Constants.Processors.CANCEL_APP1),
    CANCEL_APP2(Constants.Processors.CANCEL_APP2),
    CANCEL_APP3(Constants.Processors.CANCEL_APP3),
    CANCEL_APP4(Constants.Processors.CANCEL_APP4),

    CANCEL_PHONE_APP1(Constants.Processors.CANCEL_PHONE_APP1),
    CANCEL_PHONE_APP2(Constants.Processors.CANCEL_PHONE_APP2),
    CANCEL_PHONE_APP3(Constants.Processors.CANCEL_PHONE_APP3),
    CANCEL_PHONE_APP4(Constants.Processors.CANCEL_PHONE_APP4),

    ADMIN_PROFILE(Constants.Processors.ADMIN_PROFILE),
    USER_PROFILE(Constants.Processors.USER_PROFILE),

    CHANGE_LANG1(Constants.Processors.CHANGE_LANG1),
    CHANGE_LANG2(Constants.Processors.CHANGE_LANG2),

    LOCATION_DASH(Constants.Processors.LOCATION_DASH),

    LOCATION_CREATE1(Constants.Processors.LOCATION_CREATE1),
    LOCATION_CREATE2(Constants.Processors.LOCATION_CREATE2),

    LOCATION_DELETE1(Constants.Processors.LOCATION_DELETE1),
    LOCATION_DELETE2(Constants.Processors.LOCATION_DELETE2),

    LOCATION_VIEW1(Constants.Processors.LOCATION_VIEW1),

    ADMIN_APP_DASH(Constants.Processors.ADMIN_APP_DASH),

    LOCATION_VIEW_USER(Constants.Processors.LOCATION_VIEW_USER),
    SALON_INFO_USER(Constants.Processors.SALON_INFO_USER),

    //admin
    START_APP_DASH(Constants.Processors.START_APP_DASH),
    ALL_APP_BY_DATE_1(Constants.Processors.GET_ALL_APP_BY_DATE_1),
    ALL_APP_BY_DATE_2(Constants.Processors.GET_ALL_APP_BY_DATE_2),
    ALL_APP_TODAY_TOMORROW(Constants.Processors.GET_ALL_APP_TODAY_TOMORROW),
    GET_ALL_APPOINTMENTS_1(Constants.Processors.GET_ALL_APPOINTMENTS_1),
    GET_ALL_APPOINTMENTS_2(Constants.Processors.GET_ALL_APPOINTMENTS_2),

    COMMENTS_DASH(Constants.Processors.COMMENTS_DASH),
    CREATE_COMMENT1(Constants.Processors.CREATE_COMMENT1),
    CREATE_COMMENT2(Constants.Processors.CREATE_COMMENT2),
    CREATE_COMMENT3(Constants.Processors.CREATE_COMMENT3),

    VIEW_COMMENT1(Constants.Processors.VIEW_COMMENT1),
    VIEW_COMMENT2(Constants.Processors.VIEW_COMMENT2),

    DELETE_COMMENT1(Constants.Processors.DELETE_COMMENT1),
    DELETE_COMMENT2(Constants.Processors.DELETE_COMMENT2),
    DELETE_COMMENT3(Constants.Processors.DELETE_COMMENT3),

    SEND_MESSAGE_DASH(Constants.Processors.SEND_MESSAGE_DASH),
    SEND_MESSAGE_TEXT_1(Constants.Processors.SEND_MESSAGE_TEXT_1),
    SEND_MESSAGE_TEXT_2(Constants.Processors.SEND_MESSAGE_TEXT_2),
    SEND_MESSAGE_TEXT_3(Constants.Processors.SEND_MESSAGE_TEXT_3),
    SEND_MESSAGE_PHOTO_1(Constants.Processors.SEND_MESSAGE_PHOTO_1),
    SEND_MESSAGE_PHOTO_2(Constants.Processors.SEND_MESSAGE_PHOTO_2),
    SEND_MESSAGE_PHOTO_3(Constants.Processors.SEND_MESSAGE_PHOTO_3),

    DAY_OFF_START(Constants.Processors.DAY_OFF_START),
    DAILY_DAY_OFF_START(Constants.Processors.DAILY_DAY_OFF_START),
    PERIOD_DAY_OFF_START(Constants.Processors.PERIOD_DAY_OFF_START),

    PERIOD_DAY_OFF_DELETE1(Constants.Processors.PERIOD_DAY_OFF_DELETE1),
    PERIOD_DAY_OFF_DELETE2(Constants.Processors.PERIOD_DAY_OFF_DELETE2),
    PERIOD_DAY_OFF_DELETE3(Constants.Processors.PERIOD_DAY_OFF_DELETE3),
    PERIOD_DAY_OFF_DELETE4(Constants.Processors.PERIOD_DAY_OFF_DELETE4),

    PERIOD_DAY_OFF_CREATE1(Constants.Processors.PERIOD_DAY_OFF_CREATE1),
    PERIOD_DAY_OFF_CREATE2(Constants.Processors.PERIOD_DAY_OFF_CREATE2),
    PERIOD_DAY_OFF_CREATE3(Constants.Processors.PERIOD_DAY_OFF_CREATE3),
    PERIOD_DAY_OFF_CREATE4(Constants.Processors.PERIOD_DAY_OFF_CREATE4),
    PERIOD_DAY_OFF_CREATE5(Constants.Processors.PERIOD_DAY_OFF_CREATE5),

    PERIOD_DAY_OFF_VIEW1(Constants.Processors.PERIOD_DAY_OFF_VIEW1),
    PERIOD_DAY_OFF_VIEW2(Constants.Processors.PERIOD_DAY_OFF_VIEW2),
    PERIOD_DAY_OFF_VIEW3(Constants.Processors.PERIOD_DAY_OFF_VIEW3),
    PERIOD_DAY_OFF_VIEW4(Constants.Processors.PERIOD_DAY_OFF_VIEW4),

    DAY_OFF_CREATE_START(Constants.Processors.DAY_OFF_CREATE_START),
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
    DAY_OFF_CANCEL4(Constants.Processors.DAY_OFF_CANCEL4),

    BLOCK_USER_DASH(Constants.Processors.BLOCK_USER_DASH),

    UNBLOCK_USER1(Constants.Processors.UNBLOCK_USER1),
    UNBLOCK_USER2(Constants.Processors.UNBLOCK_USER2),
    UNBLOCK_USER3(Constants.Processors.UNBLOCK_USER3),

    BLOCK_USER1(Constants.Processors.BLOCK_USER1),
    BLOCK_USER2(Constants.Processors.BLOCK_USER2),
    BLOCK_USER3(Constants.Processors.BLOCK_USER3);


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
