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
    public static final String EMPTY_DATE = "--";
    public static final String IGNORE = "ignore";

    public static final String TODAY = "Today";
    public static final String TOMORROW = "Tomorrow";
    public static final int DAYS_IN_WEEK = 7;

    public static final String NEXT_MONTH = "nextMonth";
    public static final String CURRENT_MONTH = "currentMonth";
    public static final String MONTH = "month";

    public static final String SELECTED_SERVICE = "selectedService";
    public static final String SELECTED_SPEC = "selectedSpec";
    public static final String SELECTED_DAY = "selectedDay";

    public static final String DEPARTMENT = "department";
    public static final String CONTEXT = "context";
    public static final String IS_NEXT_MONTH = "isNextMonth";
    public static final String AVAILABLE_DATES = "availableDates";
    public static final String AVAILABLE_SPECIALISTS = "availableSpecialists";
    public static final String AVAILABLE_SERVICES = "availableServices";
    public static final String AVAILABLE_SLOTS = "availableSlots";

    public static final String USER_APPOINTMENTS = "userAppointments";

    public static final List<String> DAY_TITLES = List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");

    public static final Map<String, List<String>> DASHBOARD_BUTTONS = Map.of(
            "GENERAL::ADMIN", List.of("Appointments list", "Specialists list"),
            "GENERAL::USER", List.of("Create appointment", "My appointments", "Cancel appointment")
    );

    public static class Processors {
        //start
        public static final String ASK_LANG = "askLang";
        public static final String SET_LANG_ASK_CONT = "setLangAskContact";
        public static final String SET_CONT_START_DASH = "setContactStartDash";

        //appointment
        public static final String START_CREATE_APP = "createApp1";
        public static final String SET_APP_DATE_ASK_SPEC = "createApp2";
        public static final String SET_SPEC_ASK_SERVICE = "createApp3";
        public static final String SET_SERVICE_ASK_SLOT = "createApp4";
        public static final String CREATE_APPOINTMENT = "createApp5";

        public static final String MY_APP_1 = "myApps1";
        public static final String MY_APP_2 = "myApps2";

        public static final String CANCEL_APP1 = "cancelApp1";
        public static final String CANCEL_APP2 = "cancelApp2";
        public static final String CANCEL_APP3 = "cancelApp3";
        public static final String CANCEL_APP4 = "cancelApp4";
    }

    public static class Numbers {
        public static final Map<String, String> SPEC_NUMBERS = new HashMap<>();

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
        }
    }

    public static class Messages {
        public static final String SHARE_CONTACT = "Please share your phone number";

    }
}
