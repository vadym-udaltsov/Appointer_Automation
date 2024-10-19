package tests;

import com.codeborne.selenide.Selenide;
import engine.MaestroWebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;
import static constants.CommonProperties.URL;
import static utils.PropertiesReader.getProperty;

public class BaseWebTest {
    protected boolean useConfigLanguage = true;
    protected WebDriver webDriver;

    @BeforeMethod(alwaysRun = true)
    public void startBrowser(Method method) {
        System.out.println("Running test: " + method);
        System.out.println("Use config language (should be FALSE): " + useConfigLanguage);
        webDriver = new MaestroWebDriverFactory().getDriver();
        setWebDriver(webDriver);
        Selenide.open(getProperty(URL));
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        try {
            getWebDriver().close();
            getWebDriver().quit();
        } catch (Exception ignored) {

        }
    }
}
