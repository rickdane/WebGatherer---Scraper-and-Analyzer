package org.Webgatherer.ExperimentalLabs.Scraper.Deprecated;

import org.Webgatherer.Utility.Service.WebServiceClient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rick Dane
 */
public class ScraperBaseStatic {

    private static int delay = 100;
    //TODO DI, properties
    protected static WebServiceClient webServiceClient = new WebServiceClient("http://localhost:8080/springservicesmoduleroo/entrys");
    protected static String webServiceContentType = "application/json";

    protected static void sleep() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    protected static String prepareBaseDomainUrl(String url) {
        int index = url.indexOf("/");

        if (index != -1) {
            url = url.substring(0, index);
        }
        return url;
    }


    protected static String pullCompanyUrl(WebDriver driver, String[] origUrl, String companyName) {
        List<String> returnUrls = new ArrayList<String>();

        driver.get(origUrl[0]);
        List<WebElement> links = driver.findElements(By.tagName("a"));
        for (WebElement curElement : links) {
            String matchUrl = curElement.getAttribute("href");
            if (matchUrl != null) {
                String checkVar = null;
                if (origUrl[1].length() >= 5) {
                    checkVar = origUrl[1].substring(0, 4);
                }
                if (checkVar != null && !matchUrl.contains(companyName) && matchUrl.toLowerCase().contains(checkVar)) {
                    return matchUrl;
                }
            }
        }

        return null;
    }

    protected static String convertToUrl(String inputStr, String baseUrl) {
        String[] split = inputStr.split("-");
        String retString = "";
        if (split != null && split.length > 0) {
            retString = split[0];
        } else {
            retString = inputStr;
        }
        retString = retString.trim().replace(" ", "-").toLowerCase();

        return baseUrl + retString;
    }
}
