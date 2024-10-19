package tests.login_page;

import tests.BaseWebTest;
import io.qameta.allure.Step;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import ui.page_objects.login.LoginPage;

import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.visible;
import static constants.ExecutionGroup.LOGIN_PAGE;

public class VerifyInformationOnTheLoginPageTest extends BaseWebTest {

    @TmsLink("1")
    @Test(description = "Verify information on the Login Page", groups = {LOGIN_PAGE})
    public void testVerifyInformationOnTheLoginPage() {
        verifyLoginPage();
    }

    @Step("Verify")
    private void verifyLoginPage() {
        var loginPage = new LoginPage();
        var softAssert = new SoftAssert();

        softAssert.assertTrue(loginPage.isTitleCondition(visible), "Title should be displayed");
        softAssert.assertTrue(loginPage.isTitleCondition(matchText("Appointer")),
                "Title should be equals 'Appointer'");
        softAssert.assertTrue(loginPage.isSubTitleCondition(visible), "Subtitle should be displayed");
        softAssert.assertTrue(loginPage.isSubTitleCondition(matchText("Login or register from here to access.")),
                "Subtitle should be equals 'Login or register from here to access.'");
        softAssert.assertTrue(loginPage.isEmailInputCondition(visible), "Email input should be displayed");
        softAssert.assertTrue(loginPage.isPasswordInputCondition(visible), "Password input should be displayed");
        softAssert.assertTrue(loginPage.isLoginButtonCondition(visible), "Login button should be displayed");
        softAssert.assertTrue(loginPage.isRegisterButtonCondition(visible), "Register button should be displayed");
        softAssert.assertTrue(loginPage.isForgotPasswordLinkCondition(visible),
                "Forgot password link should be displayed");
        softAssert.assertAll();
    }

}
