package com.bot.util;

import java.util.List;

public class Constants {

    public static final String ANY = "any";
    public static final String BACK = "Back";
    public static final String HOME = "Home";
    public static final String UNAVAILABLE_DATE = "❌";
    public static final List<String> DAY_TITLES = List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");

    public static class Processors {
        //start
        public static final String ASK_LANG = "askLang";
        public static final String SET_LANG_ASK_CONT = "setLangAskContact";
        public static final String SET_CONT_START_DASH = "setContactStartDash";

        //appointment
        public static final String START_CREATE_APP = "startCreateApp";
        public static final String SET_APP_DATE_ASK_SPEC = "setAppDateAskSpec";
        public static final String MY_APPS = "startMyApps";
    }

    public static class Messages {
        public static final String SHARE_CONTACT = "Please share your phone number";

    }
}
