package ui.page_objects.admin;

import com.codeborne.selenide.SelenideElement;
import ui.page_objects.BasePageObject;

import static com.codeborne.selenide.Selenide.$;

public class AdjustSalonPage extends BasePageObject {

    private final SelenideElement adjustSalonBtn = $("#department_UpdatePopup");
    private final SelenideElement logoutBtn = $("#logout_button");

    @Override
    public boolean isOpened() {
        return isPageObjectLoaded(adjustSalonBtn, logoutBtn);
    }
}
