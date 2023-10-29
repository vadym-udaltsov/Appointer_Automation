package ui.page_objects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;

import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.String.format;
import static java.util.stream.LongStream.range;
import static utils.PropertiesReader.getProperty;

public abstract class BasePageObject {

    public final long shortWait = Long.parseLong(getProperty("web.driver.short_wait"));
    public final long longWait = Long.parseLong(getProperty("web.driver.long_wait"));
    private final SelenideElement spinner = $("#spinner_loading");

    protected BasePageObject() {
    }

    public abstract boolean isOpened();

    protected void waitUntilElementCondition(SelenideElement element, Condition condition, long waitTimeOut) {
        try {
            element.shouldBe(condition, Duration.ofMillis(waitTimeOut));
        } catch (AssertionError ignored) {

        }
    }

    protected boolean isElementCondition(SelenideElement element, Condition condition) {
        return isElementCondition(element, condition, shortWait);
    }

    protected boolean isElementCondition(SelenideElement element, Condition condition, long waitTimeOut) {
        if (condition == exist) {
            waitUntilElementCondition(element, condition, waitTimeOut);
        } else {
            try {
                waitUntilElementCondition(element, condition, waitTimeOut);
            } catch (NoSuchElementException e) {
                return false;
            }
        }
        return element.is(condition);
    }

    public boolean isElementWithTextDisplayed(String expectedText) {
        return isElementDisplayed($x(format("//*[contains(text(), \"%1$s\") " +
                "or contains(@value, \"%1$s\") " +
                "or contains(@text, \"%1$s\")]", expectedText)));
    }

    private boolean isElementDisplayed(SelenideElement element) {
        return isElementCondition(element, visible, shortWait);
    }

    public void setValueToTextField(SelenideElement element, String s) {
        var fieldValue = element.shouldBe(visible).getValue();
        range(0, Objects.requireNonNull(fieldValue).length()).forEach(i -> element.scrollIntoView(false)
                .sendKeys(Keys.BACK_SPACE));
        element.sendKeys(s);
        sleep(2000);
    }

    protected boolean isPageObjectLoaded(SelenideElement elementOne, SelenideElement elementTwo) {
        var elOneDisplayed = isElementCondition(elementOne, exist, longWait);
        var elTwoDisplayed = isElementCondition(elementTwo, exist, longWait);
        if (!elOneDisplayed) {
            System.out.println(elementOne.toString() + " is not displayed");
        }
        if (!elTwoDisplayed) {
            System.out.println(elementTwo.toString() + " is not displayed");
        }
        return elOneDisplayed && elTwoDisplayed;
    }

    public void waitForSpinnerToDisappear() {
        Selenide.sleep(1000);
        waitUntilElementCondition(spinner, disappear, longWait * 14);
    }
}
