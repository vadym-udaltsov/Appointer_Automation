package engine;


import com.codeborne.selenide.Configuration;
import engine.enums.Browser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.util.concurrent.TimeUnit;

import static enums.EnumUtil.searchEnum;
import static utils.PropertiesReader.getProperty;

public final class MaestroWebDriverFactory {
    private static final String WEB_DRIVER_TYPE_PROPERTY = "web.driver.type";
    private static final String DRIVER_MAXIMIZE_PROPERTY = "web.driver.maximize";

    public WebDriver getDriver() {
        var driverType = searchEnum(Browser.class, getProperty(WEB_DRIVER_TYPE_PROPERTY));
        WebDriver driver;
        switch (driverType) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                chromeConfiguration();
                var options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(options);
                break;
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case EDGE:
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case SAFARI:
                driver = new SafariDriver();
                break;
            default:
                throw new IllegalArgumentException(
                        "No implementation for '" + driverType + "'. Available: " +
                                Browser.getValues());
        }

        if (Boolean.parseBoolean(getProperty(DRIVER_MAXIMIZE_PROPERTY)))
            driver.manage().window().maximize();
        return driver;
    }

    private static void chromeConfiguration() {
        Configuration.browser = "chrome";
        Configuration.driverManagerEnabled = true;
        Configuration.browserSize = "1920x1080";
        Configuration.headless = true;
        Configuration.holdBrowserOpen = false;
    }
}
