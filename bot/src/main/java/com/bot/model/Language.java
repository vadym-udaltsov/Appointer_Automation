package com.bot.model;

public enum Language {
    UA("\uD83C\uDDFA\uD83C\uDDE6"),
    PL("\uD83C\uDDF5\uD83C\uDDF1"),
    CRN("\uD83C\uDDF2\uD83C\uDDEA"),
    US("\uD83C\uDDFA\uD83C\uDDF8"),

    DE("\uD83C\uDDE9\uD83C\uDDEA"),
    FR("\uD83C\uDDEB\uD83C\uDDF7"),
    CZ("\uD83C\uDDE8\uD83C\uDDFF"),
    IT("\uD83C\uDDEE\uD83C\uDDF9"),
    BE("\uD83C\uDDE7\uD83C\uDDEA"),
    BY("\uD83C\uDDE7\uD83C\uDDFE"),
    HR("\uD83C\uDDED\uD83C\uDDF7"),
    SK("\uD83C\uDDF8\uD83C\uDDF0"),
    PT("\uD83C\uDDF5\uD83C\uDDF9"),
    ES("\uD83C\uDDEA\uD83C\uDDF8"),
    TR("\uD83C\uDDF9\uD83C\uDDF7"),

    RU("\uD83C\uDDF7\uD83C\uDDFA");

    private final String value;

    Language(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getLocalizationFilePath() {
        return String.format("%s/%s/localization_%s.json", "%s", this.name().toLowerCase(), this.name().toLowerCase());
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
