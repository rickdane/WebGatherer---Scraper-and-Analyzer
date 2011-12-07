package org.Webgatherer.ExperimentalLabs.Scraper;

import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rick Dane
 */
public class PlacesScraper {


    public static void main(String[] args) {

        WebDriverFactory webDriverFactory = new WebDriverFactory();
        WebDriver driver = webDriverFactory.createNewWebDriver();
        Wait<WebDriver> wait = webDriverFactory.createWebDriverWait(driver, 2);

        run(driver, "livermore");
        driver = webDriverFactory.createNewWebDriver();
        run(driver, "san leandro");

    }

    private static void run(WebDriver driver, String searchStr) {

        String searchString = searchStr + ",+ca+software+company";
        int pageNumber = 2;
        int start = pageNumber * 10 - 10;

        String url = "https://www.google.com/search?gcx=c&sourceid=chrome&ie=UTF-8&q=google+places#q=" + searchString + "&hl=en&tbm=plcs&prmd=imvns&start=" + start;


        driver.get(url);

        List<WebElement> links;
        links = driver.findElements(By.tagName("a"));
        for (WebElement link : links) {
            String linkStr = link.getAttribute("href");
            if (linkStr != null && checkIfMatch(linkStr)) {
                System.out.println(linkStr);
            }
        }
        //driver.quit();
    }

    private static boolean checkIfMatch(String linkStr) {
        List<String> negativeMatches = new ArrayList<String>();
        negativeMatches.add("google");
        negativeMatches.add("youtube");

        List<String> positiveMatches = new ArrayList<String>();
        positiveMatches.add("http");

        for (String curMatch : negativeMatches) {
            if (linkStr.contains(curMatch)) {
                return false;
            }
        }

        for (String curMatch : positiveMatches) {
            if (!linkStr.contains(curMatch)) {
                return false;
            }
        }

        return true;
    }

}
