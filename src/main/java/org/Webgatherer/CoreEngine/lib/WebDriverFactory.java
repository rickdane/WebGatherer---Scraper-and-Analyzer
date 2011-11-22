package org.Webgatherer.CoreEngine.lib;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Rick Dane
 */
public class WebDriverFactory {

    public WebDriver createNewWebDriver() {
        return new FirefoxDriver();
    }

    public Wait createWebDriverWait(WebDriver driver, int crawlerDelay) {
        return new WebDriverWait(driver, crawlerDelay);
    }
}
