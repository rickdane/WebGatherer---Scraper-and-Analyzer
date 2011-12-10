package org.Webgatherer.ExperimentalLabs.Scraper;

import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.Webgatherer.Persistence.InputOutput.PersistenceImpl_WriteToFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rick Dane
 */
public class Scraper2 {
    private static int pageNum = 1;
    private static int maxPages = 67;
    private static String fileOutput = "/home/user/Dropbox/Rick/WebGatherer/Output/crunchbase";
    private static int delay = 100;

    public static void main(String[] args) {

        WebDriverFactory webDriverFactory = new WebDriverFactory();
        WebDriver driver = webDriverFactory.createNewWebDriver();

        while (pageNum <= maxPages) {
            sleep();
            testDriver(driver);
            pageNum++;
        }
        driver.close();
    }

    private static void sleep() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static String prepareBaseDomainUrl(String url) {
        int index = url.indexOf("/");

        if (index != -1) {
            url = url.substring(0, index);
        }
        return url;
    }

    private static void testDriver(WebDriver driver) {

        String url = "http://www.crunchbase.com/maps/search?range=140&geo=san+francisco%2C+ca&page=" + pageNum;
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
                    sleep();
                    link.click();
                    String[] tmpArray = {convertToUrl(title).toLowerCase(), title.toLowerCase()};
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
                System.out.println(pulledUrl);
                PersistenceImpl_WriteToFile.appendToFile(fileOutput, pulledUrl + "\n");
            }
        }
    }

    private static String pullCompanyUrl(WebDriver driver, String[] origUrl) {
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
                if (checkVar != null && !matchUrl.contains("crunchbase") && matchUrl.toLowerCase().contains(checkVar)) {
                    return matchUrl;
                }
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
