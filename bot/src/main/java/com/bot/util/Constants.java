package com.bot.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

    public static final String ANY = "any";
    public static final String BACK = "Back";
    public static final String HOME = "Home";

    public static final String UNAVAILABLE_DATE = "❌";
    public static final String STAR_SIGN = "⭐⭐⭐⭐⭐";
    public static final String LONE_STAR_SIGN = "⭐";
    public static final String EMPTY_DATE = "--";
    public static final String IGNORE = "ignore";
    public static final String SUBMIT = "Submit";

    public static final String TODAY = "Today";
    public static final String TOMORROW = "Tomorrow";
    public static final String WHOLE_DAY = "Whole day";
    public static final String DAY_OFF = "DayOff";
    public static final int DAYS_IN_WEEK = 7;

    public static final String NEXT_MONTH = "nextMonth";
    public static final String CURRENT_MONTH = "currentMonth";
    public static final String MONTH = "month";

    public static final String SELECTED_SERVICE = "selectedService";
    public static final String SELECTED_SPEC = "selectedSpec";
    public static final String SELECTED_TITLE = "selectedTitle";
    public static final String SELECTED_DAY = "selectedDay";
    public static final String SELECTED_HOUR = "selectedHour";
    public static final String SELECTED_MINUTE = "selectedMinute";
    public static final String SELECTED_APPOINTMENT = "selectedAppointment";

    public static final String DEPARTMENT = "department";
    public static final String CONTEXT = "context";
    public static final String IS_NEXT_MONTH = "isNextMonth";
    public static final String AVAILABLE_DATES = "availableDates";
    public static final String AVAILABLE_TITLES = "availableTitles";
    public static final String AVAILABLE_SPECIALISTS = "availableSpecialists";
    public static final String AVAILABLE_SLOT_TITLES = "availableSlotTitles";
    public static final String AVAILABLE_SLOTS = "availableSlots";
    public static final String AVAILABLE_APPOINTMENTS = "availableAppointments";
    public static final String AVAILABLE_DURATIONS = "availableDurations";
    public static final String AVAILABLE_BUTTONS = "availableButtons";

    public static final String TEXT_FOR_SUBMIT = "textForSubmit";

    public static final String NO_DAYS_OFF = "You have no non working days for current and next months";

    public static final String USER_APPOINTMENTS = "userAppointments";

    public static final List<String> DAY_TITLES = List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");

    public static final List<String> PROFILE_BUTTONS = List.of("Change language");
    public static final List<String> VIEW_ADMIN_APP_BUTTONS = List.of("Today", "Tomorrow", "By Date");
    public static final List<String> ADMIN_APPOINTMENT_BUTTONS = List.of("View", "Create", "Delete");
    public static final List<String> START_BLOCK_BUTTONS = List.of("Block user", "Unblock user");

    public static final Map<String, List<String>> DASHBOARD_BUTTONS = Map.of(
            "GENERAL::ADMIN", List.of("Appointments", "DayOff", "Block", "Mailing", "Profile"),
            "GENERAL::USER", List.of("Create appointment", "My appointments", "Cancel appointment", "Profile")
    );

    public static class Processors {
        //start
        public static final String ASK_LANG = "askLang";
        public static final String SET_LANG_ASK_CONT = "setLangAskContact";
        public static final String SET_CONTACT = "setContact";
        public static final String START_DASH = "startDash";

        //appointment
        public static final String START_CREATE_APP = "createApp1";
        public static final String SET_APP_DATE_ASK_SPEC = "createApp2";
        public static final String SET_SPEC_ASK_SERVICE = "createApp3";
        public static final String SET_SERVICE_ASK_SLOT = "createApp4";
        public static final String CREATE_APPOINTMENT = "createApp5";
        public static final String CREATE_APPOINTMENT6 = "createApp6";

        public static final String MY_APP_1 = "myApps1";
        public static final String MY_APP_2 = "myApps2";

        public static final String CANCEL_APP1 = "cancelApp1";
        public static final String CANCEL_APP2 = "cancelApp2";
        public static final String CANCEL_APP3 = "cancelApp3";
        public static final String CANCEL_APP4 = "cancelApp4";

        public static final String CANCEL_PHONE_APP1 = "cancelPhoneApp1";
        public static final String CANCEL_PHONE_APP2 = "cancelPhoneApp2";
        public static final String CANCEL_PHONE_APP3 = "cancelPhoneApp3";
        public static final String CANCEL_PHONE_APP4 = "cancelPhoneApp4";

        //admin
        public static final String START_APP_DASH = "startAppointmentsDash";
        public static final String GET_ALL_APP_BY_DATE_1 = "getAppointmentsByDateFirst";
        public static final String GET_ALL_APP_BY_DATE_2 = "getAppointmentsByDateSecond";
        public static final String GET_ALL_APP_TODAY_TOMORROW = "getAppointmentsTodayTomorrow";
        public static final String GET_ALL_APPOINTMENTS_1 = "getAllAppointments";
        public static final String GET_ALL_APPOINTMENTS_2 = "getAllAppointmentsByDate";

        public static final String MASS_MESSAGING_1 = "massMessaging1";
        public static final String MASS_MESSAGING_2 = "massMessaging2";
        public static final String MASS_MESSAGING_3 = "massMessaging3";

        public static final String DAY_OFF_START = "dayOffStart";
        public static final String DAY_OFF_CREATE1 = "dayOffCreate1";
        public static final String DAY_OFF_CREATE2 = "dayOffCreate2";
        public static final String DAY_OFF_CREATE3 = "dayOffCreate3";
        public static final String DAY_OFF_CREATE4 = "dayOffCreate4";
        public static final String DAY_OFF_CREATE5 = "dayOffCreate5";

        public static final String DAY_OFF_VIEW1 = "dayOffView1";
        public static final String DAY_OFF_VIEW2 = "dayOffView2";
        public static final String DAY_OFF_VIEW3 = "dayOffView3";

        public static final String DAY_OFF_CANCEL1 = "dayOffCancel1";
        public static final String DAY_OFF_CANCEL2 = "dayOffCancel2";
        public static final String DAY_OFF_CANCEL3 = "dayOffCancel3";
        public static final String DAY_OFF_CANCEL4 = "dayOffCancel4";

        public static final String BLOCK_USER_DASH = "blockUserDash";
        public static final String UNBLOCK_USER1 = "unBlockUser1";
        public static final String UNBLOCK_USER2 = "unblockUser2";
        public static final String UNBLOCK_USER3 = "unblockUser3";
        public static final String BLOCK_USER1 = "blockUser1";
        public static final String BLOCK_USER2 = "blockUser2";
        public static final String BLOCK_USER3 = "blockUser3";

        public static final String PROFILE = "profile";
        public static final String CHANGE_LANG1 = "changeLanguage1";
        public static final String CHANGE_LANG2 = "changeLanguage2";

        public static final String ADMIN_APP_DASH = "appointments";
    }

    public static class Numbers {
        public static final Map<String, String> SPEC_NUMBERS = new HashMap<>();
        public static final Map<Integer, String> PERIOD_TITLES = new HashMap<>();

        static {
            SPEC_NUMBERS.put("01", "0️⃣1️⃣");
            SPEC_NUMBERS.put("02", "0️⃣2️⃣");
            SPEC_NUMBERS.put("03", "0️⃣3️⃣");
            SPEC_NUMBERS.put("04", "0️⃣4️⃣");
            SPEC_NUMBERS.put("05", "0️⃣5️⃣");
            SPEC_NUMBERS.put("06", "0️⃣6️⃣");
            SPEC_NUMBERS.put("07", "0️⃣7️⃣");
            SPEC_NUMBERS.put("08", "0️⃣8️⃣");
            SPEC_NUMBERS.put("09", "0️⃣9️⃣");
            SPEC_NUMBERS.put("10", "1️⃣0️⃣");
            SPEC_NUMBERS.put("11", "1️⃣1️⃣");
            SPEC_NUMBERS.put("12", "1️⃣2️⃣");
            SPEC_NUMBERS.put("13", "1️⃣3️⃣");
            SPEC_NUMBERS.put("14", "1️⃣4️⃣");
            SPEC_NUMBERS.put("15", "1️⃣5️⃣");
            SPEC_NUMBERS.put("16", "1️⃣6️⃣");
            SPEC_NUMBERS.put("17", "1️⃣7️⃣");
            SPEC_NUMBERS.put("18", "1️⃣8️⃣");
            SPEC_NUMBERS.put("19", "1️⃣9️⃣");
            SPEC_NUMBERS.put("20", "2️⃣0️⃣");
            SPEC_NUMBERS.put("21", "2️⃣1️⃣");
            SPEC_NUMBERS.put("22", "2️⃣2️⃣");
            SPEC_NUMBERS.put("23", "2️⃣3️⃣");
            SPEC_NUMBERS.put("24", "2️⃣4️⃣");
            SPEC_NUMBERS.put("25", "2️⃣5️⃣");
            SPEC_NUMBERS.put("26", "2️⃣6️⃣");
            SPEC_NUMBERS.put("27", "2️⃣7️⃣");
            SPEC_NUMBERS.put("28", "2️⃣8️⃣");
            SPEC_NUMBERS.put("29", "2️⃣9️⃣");
            SPEC_NUMBERS.put("30", "3️⃣0️⃣");
            SPEC_NUMBERS.put("31", "3️⃣1️⃣");

            PERIOD_TITLES.put(0, "0:30");
            PERIOD_TITLES.put(1, "1:00");
            PERIOD_TITLES.put(2, "1:30");
            PERIOD_TITLES.put(3, "2:00");
            PERIOD_TITLES.put(4, "2:30");
            PERIOD_TITLES.put(5, "3:00");
            PERIOD_TITLES.put(6, "3:30");
            PERIOD_TITLES.put(7, "4:00");
            PERIOD_TITLES.put(8, "4:30");
            PERIOD_TITLES.put(9, "5:00");
            PERIOD_TITLES.put(10, "5:30");
            PERIOD_TITLES.put(11, "6:00");
            PERIOD_TITLES.put(12, "6:30");
            PERIOD_TITLES.put(13, "7:00");
            PERIOD_TITLES.put(14, "7:30");
            PERIOD_TITLES.put(15, "8:00");
            PERIOD_TITLES.put(16, "8:30");
            PERIOD_TITLES.put(17, "9:00");
        }
    }

    public static class Messages {
        public static final String SHARE_CONTACT = "Please share your phone number";
        public static final String SELECT_ACTION = "Select action";
        public static final String INPUT_MESSAGE = "Input message text";
        public static final String SUBMIT_MESSAGE = "Submit message text:";
        public static final String MASS_MESSAGE_HEADER = "⭐ Attention ⭐";
        public static final String MESSAGES_SENT = "All messages sent to recipients";
        public static final String SELECT_SPECIALIST = "Select specialist";
        public static final String INCORRECT_ACTION = "Select available option";
        public static final String INCORRECT_DATE = "Select available date";
        public static final String NO_APP_FOR_DATE = "You have no appointments for selected date";
        public static final String APP_FOR_DATE = "All appointments for ${date}";
        public static final String APP_SPECIALIST = "Specialist: ${specialist}";
        public static final String APP_CLIENT_INFO = "Client: ${client}\n Phone: ${phone}";
        public static final String CLIENT_INFO = "Client: ${client}";

        public static final String INCORRECT_SPECIALIST = "Select specialist from proposed";

    }
}
