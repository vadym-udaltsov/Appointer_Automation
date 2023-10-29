package utils;

import exceptions.PropertyReaderException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesReader {

    private PropertiesReader() {
    }

    private static final String EXCEPTION_TEXT = "Error occurred during reading properties file: ";

    private static final String CONFIG_PROPERTIES = "configs/config.properties";

    private static final String LOCAL_PROPERTIES = "configs/local.properties";

    private static Properties configs;

    public static String getProperty(final String propertyName) {
        synchronized (PropertiesReader.class) {
            if (configs == null) {
                try {
                    InputStream readerConfig = PropertiesReader.class
                            .getClassLoader().getResourceAsStream(CONFIG_PROPERTIES);
                    final Properties properties = new Properties();
                    properties.load(readerConfig);
                    readerConfig = PropertiesReader.class
                            .getClassLoader().getResourceAsStream(LOCAL_PROPERTIES);
                    if (readerConfig != null) {
                        properties.load(readerConfig);
                    }
                    configs = properties;
                } catch (IOException ex) {
                    throw new PropertyReaderException(EXCEPTION_TEXT + ex.getMessage());
                }
            }
        }

        final String systemProperty = System.getProperty(propertyName);
        var condition = configs.getProperty(propertyName, "Property Reader Fails");
        return systemProperty == null ? condition : systemProperty;
    }


    public static void setConfigProperty(String name, String value) {
        configs.setProperty(name, value);
    }
}
