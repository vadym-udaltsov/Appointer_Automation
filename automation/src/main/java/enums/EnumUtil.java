package enums;

public final class EnumUtil {

    private EnumUtil() {
    }

    public static <T extends Enum<?>> T searchEnum(final Class<T> enumeration,
                                                   final String search) {
        for (T singleEnum : enumeration.getEnumConstants()) {
            if (singleEnum.toString().equalsIgnoreCase(search)) {
                return singleEnum;
            }
        }
        throw new IllegalArgumentException(String.format("No enum value was found: Enum[%s] Value[%s]",
                enumeration.getSimpleName(), search));
    }
}
