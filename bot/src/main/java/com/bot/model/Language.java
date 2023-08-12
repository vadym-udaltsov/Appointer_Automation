package com.bot.model;

import java.util.Locale;

public enum Language {
    UA("\uD83C\uDDFA\uD83C\uDDE6", Locale.forLanguageTag("UA"), "localization/localization_ua.json", "keys/keys_ua.json"),
    PL("\uD83C\uDDF5\uD83C\uDDF1", Locale.forLanguageTag("PL"), "localization/localization_pl.json", "keys/keys_pl.json"),
    CRN("\uD83C\uDDF2\uD83C\uDDEA", Locale.forLanguageTag("CRN"), "localization/localization_crn.json", "keys/keys_crn.json"),
    US("\uD83C\uDDFA\uD83C\uDDF8", Locale.forLanguageTag("USA"), "localization.json", ""),
    RU("\uD83C\uDDF7\uD83C\uDDFA", Locale.forLanguageTag("RU"), "localization/localization_ru.json", "keys/keys_ru.json");

    private final String value;
    private final Locale locale;
    private final String localizationFilePath;
    private final String deLocalizationPath;

    Language(String value, Locale locale, String localizationFilePath, String deLocalizationPath) {
        this.value = value;
        this.locale = locale;
        this.localizationFilePath = localizationFilePath;
        this.deLocalizationPath = deLocalizationPath;
    }

    public String getValue() {
        return value;
    }
    public Locale getLocale() {
        return locale;
    }

    public String getLocalizationFilePath() {
        return localizationFilePath;
    }

    public String getDeLocalizationPath() {
        return deLocalizationPath;
    }

    public static Language fromValue(String value) {
        for (Language language : values()) {
            if (language.getValue().equalsIgnoreCase(value)) {
                return language;
            }
        }
        return null;
    }
}
