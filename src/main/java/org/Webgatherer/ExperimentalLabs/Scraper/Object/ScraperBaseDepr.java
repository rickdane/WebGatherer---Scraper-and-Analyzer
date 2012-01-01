package org.Webgatherer.ExperimentalLabs.Scraper.Object;

import com.google.inject.Inject;
import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParser;
import org.openqa.selenium.WebDriver;

/**
 * @author Rick Dane
 */
public class ScraperBaseDepr {

    protected WebDriver driver;
    protected HtmlParser htmlParser;

    @Inject
    public ScraperBaseDepr(WebDriverFactory webDriverFactory, HtmlParser htmlParser) {
        driver = webDriverFactory.createNewWebDriver();
        this.htmlParser = htmlParser;
    }


}
