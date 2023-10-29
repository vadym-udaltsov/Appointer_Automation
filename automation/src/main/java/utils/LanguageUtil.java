package utils;

import static constants.CommonProperties.LANGUAGE;
import static utils.PropertiesReader.getProperty;

public class LanguageUtil {

    public static String getAppLanguage() {
        var locale = getProperty(LANGUAGE);

        if (locale.isEmpty()) {
            locale = "en-US";
        }
        return locale.toLowerCase();
    }
}
