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

import java.time.Duration;

import static enums.EnumUtil.searchEnum;
import static utils.PropertiesReader.getProperty;

public final class MaestroWebDriverFactory {
    private static final String WEB_DRIVER_TYPE_PROPERTY = "web.driver.type";

    public WebDriver getDriver() {
        var driverType = searchEnum(Browser.class, getProperty(WEB_DRIVER_TYPE_PROPERTY));
        WebDriver driver;
        switch (driverType) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(getChromeOptions());
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
                        "No implementation for '" + driverType + "'. Available: " + driverType.name());
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(Configuration.timeout));

        return driver;
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--use-fake-ui-for-media-stream");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");

        if (isRunningInGitHubActions()) {
            options.addArguments("--no-sandbox");
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
        }
        return options;
    }

    private boolean isRunningInGitHubActions() {
        return "true".equals(System.getenv("GITHUB_ACTIONS"));
    }
}
