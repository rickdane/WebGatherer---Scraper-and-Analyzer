package org.Webgatherer.Controller;

import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rick Dane
 */
public class Entry_ExampleMain {

    /**
     * FOR TESTING ONLY
     *
     * @param args
     */
    public static void main(String[] args) {

        testDriver();

    }

    private static String prepareBaseDomainUrl(String url) {
        int index = url.indexOf("/");

        if (index != -1) {
            url = url.substring(0, index);
        }
        return url;
    }

    private static void testDriver() {
        String url = "http://www.crunchbase.com/maps/search?range=140&geo=san+francisco%2C+ca";

        WebDriverFactory webDriverFactory = new WebDriverFactory();
        WebDriver driver = webDriverFactory.createNewWebDriver();
        driver.get(url);
        driver.get(url);



        List<WebElement> links;
        List<String[]> initialUrls = new ArrayList<String[]>();
        List<String> urls = new ArrayList<String>();

        links = driver.findElements(By.tagName("a"));

        for (WebElement link : links) {
            String onclick = null;
            try {
                onclick = link.getAttribute("onclick");
                if (onclick.startsWith("snap_to_marker")) {
                    String title = link.getAttribute("title");
                    Thread.sleep(500);
                    link.click();
                    String[] tmpArray = {convertToUrl(title), title};
                    initialUrls.add(tmpArray);
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }

        for (String[] curEntry : initialUrls) {

            String pulledUrl = pullCompanyUrl(driver, curEntry);
            if (pulledUrl != null) {
                urls.add(pulledUrl);
                System.out.println("<a href='" + pulledUrl + "'>" + pulledUrl + "</a>");
            }
        }

        driver.close();
    }

    private static String pullCompanyUrl(WebDriver driver, String[] origUrl) {
        List<String> returnUrls = new ArrayList<String>();

        driver.get(origUrl[0]);
        List<WebElement> links = driver.findElements(By.tagName("a"));
        for (WebElement curElement : links) {
            String matchUrl = curElement.getAttribute("href");
            if (matchUrl != null && !matchUrl.contains("crunchbase") && matchUrl.contains(origUrl[1].substring(0, 6))) {
                return matchUrl;
            }
        }

        return null;
    }

    private static String convertToUrl(String inputStr) {
        String[] split = inputStr.split("-");
        String retString = "";
        if (split != null && split.length > 0) {
            retString = split[0];
        } else {
            retString = inputStr;
        }
        retString = retString.trim().replace(" ", "-").toLowerCase();

        return "http://www.crunchbase.com/company/" + retString;
    }
}
