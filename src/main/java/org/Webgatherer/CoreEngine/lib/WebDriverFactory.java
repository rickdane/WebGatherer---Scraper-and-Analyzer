package org.Webgatherer.CoreEngine.lib;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * @author Rick Dane
 */
public class WebDriverFactory {

    //TODO properties
    private static final int timeoutSeconds = 5;

    public WebDriver createNewWebDriver() {
        WebDriver driver = new HtmlUnitDriver();
        //WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(timeoutSeconds, TimeUnit.SECONDS);
        return driver;
    }

    public Wait createWebDriverWait(WebDriver driver, int crawlerDelay) {
        return new WebDriverWait(driver, crawlerDelay);
    }
}
