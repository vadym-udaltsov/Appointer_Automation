package engine.enums;

import java.util.Arrays;

public enum Browser {

    CHROME,
    FIREFOX,
    EDGE,
    IE,
    SAFARI;

    public static String getValues() {
        final var values = new StringBuilder();
        Arrays.stream(values())
                .forEach(v -> values.append("-").append(v));
        return values.toString();
    }
}
