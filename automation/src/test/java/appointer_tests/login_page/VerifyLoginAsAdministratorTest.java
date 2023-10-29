package appointer_tests.login_page;

import appointer_tests.BaseWebTest;
import io.qameta.allure.Step;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;

import static constants.ExecutionGroup.LOGIN_PAGE;
import static org.testng.Assert.assertTrue;
import static ui.helpers.LoginHelper.loginAsAdmin;
import static ui.models.CredentialsProvider.getAdminCredentials;

public class VerifyLoginAsAdministratorTest extends BaseWebTest {
    @TmsLink("4")
    @Test(description = "Verify information on the Login Page", groups = {LOGIN_PAGE})
    public void testVerifyInformationOnTheLoginPage() {
        verifyLoginPage();
    }

    @Step("Verify")
    private void verifyLoginPage() {
        var adjustSalonPage = loginAsAdmin(getAdminCredentials());
        assertTrue(adjustSalonPage.isOpened(), "Adjust Salon Page should be opened");
    }
}
