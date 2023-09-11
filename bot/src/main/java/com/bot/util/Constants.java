package com.bot.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

    public static final String ANY = "any";
    public static final String ALL = "All";
    public static final String BACK = "Back";
    public static final String HOME = "Home";

    public static final String UNAVAILABLE_DATE = "❌";
    public static final String STAR_SIGN = "⭐⭐⭐⭐⭐";
    public static final String LONE_STAR_SIGN = "⭐";
    public static final String EMPTY_DATE = "--";
    public static final String IGNORE = "ignore";
    public static final String SUBMIT = "Submit";

    public static final String DICTIONARY_PATTERN = "%s/%s/localization_%s.json";

    public static final String TODAY = "Today";
    public static final String TOMORROW = "Tomorrow";
    public static final String WHOLE_DAY = "Whole day";
    public static final String DAY_OFF = "DayOff";
    public static final int DAYS_IN_WEEK = 7;

    public static final String NEXT_MONTH = "nextMonth";
    public static final String PREV_MONTH = "prevMonth";
    public static final String CURRENT_MONTH = "currentMonth";
    public static final String SELECTED_MONTH = "selectedMonth";
    public static final String MONTH = "month";

    public static final String SELECTED_SERVICE = "selectedService";
    public static final String SELECTED_SPEC = "selectedSpec";
    public static final String SELECTED_TITLE = "selectedTitle";
    public static final String SELECTED_DAY = "selectedDay";
    public static final String SELECTED_HOUR = "selectedHour";
    public static final String SELECTED_MINUTE = "selectedMinute";
    public static final String SELECTED_APPOINTMENT = "selectedAppointment";
    public static final String SELECTED_YEAR = "selectedYear";
    public static final String SELECTED_CONTEXT = "selectedContext";

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
    public static final String AVAILABLE_NUMBERS = "availableNumbers";

    public static final String TEXT_FOR_SUBMIT = "textForSubmit";
    public static final String PHOTO_ID_FOR_SUBMIT = "photoIdForSubmit";

    public static final String LINK_TYPE = "linkType";

    public static final String NO_DAYS_OFF = "You have no non working days for current and next months";

    public static final String USER_APPOINTMENTS = "userAppointments";

    public static final List<String> DAY_TITLES = List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");

    public static final List<String> ADMIN_PROFILE_BUTTONS = List.of("Change language", "Location", "Description", "Social media");
    public static final List<String> USER_PROFILE_BUTTONS = List.of("Change language");
    public static final List<String> SALON_INFO_BUTTONS = List.of("View location", "View description", "View social media");
    public static final List<String> VIEW_ADMIN_APP_BUTTONS = List.of("Today", "Tomorrow", "By Date");
    public static final List<String> VIEW_ADMIN_MESSAGES_BUTTONS = List.of("Send Text", "Send Image");
    public static final List<String> ADMIN_APPOINTMENT_BUTTONS = List.of("View", "Create", "Delete");
    public static final List<String> LINKS_BUTTONS = List.of("Instagram", "Facebook", "Website");
    public static final List<String> START_BLOCK_BUTTONS = List.of("Block user", "Unblock user");
    public static final List<String> START_DAYOFF_BUTTONS = List.of("Daily", "Period");

    public static final Map<String, List<String>> DASHBOARD_BUTTONS = Map.of(
            "GENERAL::ADMIN", List.of("Appointments", "DayOff", "Block", "Mailing", "Comments", "Profile"),
            "GENERAL::USER", List.of("Create appointment", "My appointments", "Cancel appointment", "Profile", "Salon info")
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

        public static final String COMMENTS_DASH = "commentsDash";

        public static final String CREATE_COMMENT1 = "createComment1";
        public static final String CREATE_COMMENT2 = "createComment2";
        public static final String CREATE_COMMENT3 = "createComment3";

        public static final String VIEW_COMMENT1 = "viewComment1";
        public static final String VIEW_COMMENT2 = "viewComment2";

        public static final String DELETE_COMMENT1 = "deleteComment1";
        public static final String DELETE_COMMENT2 = "deleteComment2";
        public static final String DELETE_COMMENT3 = "deleteComment3";

        public static final String DESCRIPTION_DASH = "descriptionDash";

        public static final String CREATE_DESCRIPTION1 = "descriptionCreate1";
        public static final String CREATE_DESCRIPTION2 = "descriptionCreate2";

        public static final String DELETE_DESCRIPTION1 = "descriptionDelete1";
        public static final String DELETE_DESCRIPTION2 = "descriptionDelete2";

        public static final String VIEW_DESCRIPTION = "descriptionView";

        public static final String SEND_MESSAGE_DASH = "sendMessageDash";
        public static final String SEND_MESSAGE_TEXT_1 = "sendMessageText1";
        public static final String SEND_MESSAGE_TEXT_2 = "sendMessageText2";
        public static final String SEND_MESSAGE_TEXT_3 = "sendMessageText3";
        public static final String SEND_MESSAGE_PHOTO_1 = "sendMessagePhoto1";
        public static final String SEND_MESSAGE_PHOTO_2 = "sendMessagePhoto2";
        public static final String SEND_MESSAGE_PHOTO_3 = "sendMessagePhoto3";

        public static final String DAY_OFF_START = "dayOffStart";
        public static final String DAILY_DAY_OFF_START = "dailyDayOffStart";
        public static final String PERIOD_DAY_OFF_START = "periodDayOffStart";

        public static final String PERIOD_DAY_OFF_CREATE1 = "periodDayOffCreate1";
        public static final String PERIOD_DAY_OFF_CREATE2 = "periodDayOffCreate2";
        public static final String PERIOD_DAY_OFF_CREATE3 = "periodDayOffCreate3";
        public static final String PERIOD_DAY_OFF_CREATE4 = "periodDayOffCreate4";
        public static final String PERIOD_DAY_OFF_CREATE5 = "periodDayOffCreate5";

        public static final String PERIOD_DAY_OFF_DELETE1 = "periodDayOffCancel1";
        public static final String PERIOD_DAY_OFF_DELETE2 = "periodDayOffCancel2";
        public static final String PERIOD_DAY_OFF_DELETE3 = "periodDayOffCancel3";
        public static final String PERIOD_DAY_OFF_DELETE4 = "periodDayOffCancel4";

        public static final String PERIOD_DAY_OFF_VIEW1 = "periodDayOffView1";
        public static final String PERIOD_DAY_OFF_VIEW2 = "periodDayOffView2";
        public static final String PERIOD_DAY_OFF_VIEW3 = "periodDayOffView3";
        public static final String PERIOD_DAY_OFF_VIEW4 = "periodDayOffView4";

        public static final String DAY_OFF_CREATE_START = "createDayOffStart";
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

        public static final String ADMIN_PROFILE = "adminProfile";
        public static final String USER_PROFILE = "userProfile";
        public static final String CHANGE_LANG1 = "changeLanguage1";
        public static final String CHANGE_LANG2 = "changeLanguage2";

        public static final String LOCATION_DASH = "locationDash";
        public static final String LOCATION_CREATE1 = "locationCreate1";
        public static final String LOCATION_CREATE2 = "locationCreate2";

        public static final String LOCATION_DELETE1 = "locationDelete1";
        public static final String LOCATION_DELETE2 = "locationDelete2";

        public static final String LOCATION_VIEW1 = "locationView1";
        public static final String LOCATION_VIEW_USER = "viewLocation";
        public static final String DESCRIPTION_VIEW_USER = "viewDescription";

        public static final String SALON_INFO_USER = "salonDash";

        public static final String ADMIN_APP_DASH = "appointments";

        public static final String LINKS_DASH = "linksDash";
        public static final String LINKS_VIEW = "linksView";
        public static final String LINKS_CREATE_1 = "linksCreate1";
        public static final String LINKS_CREATE_2 = "linksCreate2";
        public static final String LINKS_CREATE_3 = "linksCreate3";
        public static final String LINKS_DELETE_1 = "linksDelete1";
        public static final String LINKS_DELETE_2 = "linksDelete2";
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
            PERIOD_TITLES.put(18, "9:30");
            PERIOD_TITLES.put(19, "10:00");
            PERIOD_TITLES.put(20, "10:30");
            PERIOD_TITLES.put(21, "11:00");
            PERIOD_TITLES.put(22, "11:30");
            PERIOD_TITLES.put(23, "12:00");
            PERIOD_TITLES.put(24, "12:30");
            PERIOD_TITLES.put(25, "13:00");
            PERIOD_TITLES.put(26, "13:30");
            PERIOD_TITLES.put(27, "14:00");
            PERIOD_TITLES.put(28, "14:30");
            PERIOD_TITLES.put(29, "15:00");
            PERIOD_TITLES.put(30, "15:30");
            PERIOD_TITLES.put(31, "16:00");
        }
    }

    public static class Messages {
        public static final String SHARE_CONTACT = "Please share your phone number";
        public static final String SELECT_ACTION = "Select action";
        public static final String INPUT_MESSAGE_TEXT = "Input message text";
        public static final String INPUT_MESSAGE_IMAGE = "Upload image";
        public static final String SUBMIT_MESSAGE_TEXT = "Submit message text:";
        public static final String SUBMIT_MESSAGE_IMAGE = "Submit image sending";
        public static final String MASS_MESSAGE_HEADER = "⭐ Attention ⭐";
        public static final String ALL_MESSAGES_SENT = "All messages sent to recipients";
        public static final String SELECT_SPECIALIST = "Select specialist";
        public static final String INCORRECT_ACTION = "Select available option";
        public static final String INCORRECT_DATE = "Select available date";
        public static final String SELECT_START_DATE = "Select start date of period";
        public static final String SELECT_END_DATE = "Select end date of period";
        public static final String NO_APP_FOR_DATE = "You have no appointments for selected date";
        public static final String APP_FOR_DATE = "All appointments for ${date}";
        public static final String APP_SPECIALIST = "Specialist: ${specialist}";
        public static final String APP_CLIENT_INFO = "Client: ${client}\n Phone: ${phone}";
        public static final String CLIENT_INFO = "Client: ${client}";
        public static final String LINK_CREATE_PROMPT = "Enter your ${media} link";
        public static final String LINK_CREATE_SUCCESS = "Your ${media} link created";
        public static final String LINK_DELETE_SUCCESS = "Your ${media} link deleted";
        public static final String LINKS_NOT_EXIST = "Any links are not set yet";

        public static final String INCORRECT_SPECIALIST = "Select specialist from proposed";

    }
}
