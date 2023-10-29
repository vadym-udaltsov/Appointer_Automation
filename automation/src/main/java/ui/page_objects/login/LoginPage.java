package ui.page_objects.login;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ui.page_objects.BasePageObject;
import ui.page_objects.admin.AdjustSalonPage;
import ui.page_objects.register.RegisterModal;
import ui.page_objects.reset_password.ResetPasswordModal;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class LoginPage extends BasePageObject {

    private final SelenideElement emailInput = $("input#email");
    private final SelenideElement passwordInput = $("#pass");
    private final SelenideElement loginBtn = $("#log");
    private final SelenideElement registerBtn = $x("//button[@data-target='#emailModal']");
    private final SelenideElement forgotPassword = $("#sendCode_label");

    private final SelenideElement footerInformation = $(".login-main-text");
    private final SelenideElement title = footerInformation.$("h2");
    private final SelenideElement subTitle = footerInformation.$("p");


    @Override
    public boolean isOpened() {
        return isPageObjectLoaded(emailInput, passwordInput);
    }

    public boolean isEmailInputCondition(Condition condition) {
        return isElementCondition(emailInput, condition);
    }

    public boolean isPasswordInputCondition(Condition condition) {
        return isElementCondition(passwordInput, condition);
    }

    public boolean isLoginButtonCondition(Condition condition) {
        return isElementCondition(loginBtn, condition);
    }

    public boolean isRegisterButtonCondition(Condition condition) {
        return isElementCondition(registerBtn, condition);
    }

    public boolean isForgotPasswordLinkCondition(Condition condition) {
        return isElementCondition(forgotPassword, condition);
    }

    public LoginPage setEmail(String email) {
        setValueToTextField(emailInput, email);
        return this;
    }

    public LoginPage setPassword(String password) {
        setValueToTextField(passwordInput, password);
        return this;
    }

    public boolean isTitleCondition(Condition condition) {
        return isElementCondition(title, condition);
    }

    public boolean isSubTitleCondition(Condition condition) {
        return isElementCondition(subTitle, condition);
    }

    public AdjustSalonPage clickLogin() {
        loginBtn.shouldBe(Condition.visible).click();
        waitForSpinnerToDisappear();
        return new AdjustSalonPage();
    }

    public RegisterModal clickRegister() {
        registerBtn.shouldBe(Condition.visible).click();
        return new RegisterModal();
    }

    public ResetPasswordModal clickResetPassword() {
        forgotPassword.shouldBe(Condition.visible).click();
        return new ResetPasswordModal();
    }

}
