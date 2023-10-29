package ui.models;

import static constants.CommonProperties.EMAIL;
import static constants.CommonProperties.PASSWORD;
import static utils.PropertiesReader.getProperty;

public class CredentialsProvider {

    public static Credentials getAdminCredentials() {
        return new Credentials(getProperty(EMAIL), getProperty(PASSWORD));
    }
}
