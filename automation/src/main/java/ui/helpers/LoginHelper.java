package ui.helpers;

import lombok.experimental.UtilityClass;
import ui.models.Credentials;
import ui.page_objects.admin.AdjustSalonPage;
import ui.page_objects.login.LoginPage;

@UtilityClass
public class LoginHelper {

    public static AdjustSalonPage loginAsAdmin(Credentials credentials) {
        login(credentials);
        return new AdjustSalonPage();
    }

    public void login(Credentials credentials) {
        var loginPage = new LoginPage();
        loginPage.setEmail(credentials.getEmail())
                .setPassword(credentials.getPassword())
                .clickLogin();
    }
}
