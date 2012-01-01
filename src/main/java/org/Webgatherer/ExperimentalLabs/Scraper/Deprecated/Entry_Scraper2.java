package org.Webgatherer.ExperimentalLabs.Scraper.Deprecated;

import com.google.gson.Gson;
import org.Webgatherer.Controller.EntityTransport.EntryTransport;
import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rick Dane
 */
public class Entry_Scraper2 extends ScraperBaseStatic {
    private static int pageNum = 4;
    private static int maxPages = 7;
    private static String fileOutput = "/home/user/Dropbox/Rick/WebGatherer/Output/crunchbase";

    public static void main(String[] args) {

        WebDriverFactory webDriverFactory = new WebDriverFactory();
        WebDriver driver = webDriverFactory.createNewWebDriver();

        while (pageNum <= maxPages) {
            sleep();
            getLinkFromOnclickElement(driver, "http://www.crunchbase.com/maps/search?range=140&geo=san+francisco%2C+ca&page=", "http://www.crunchbase.com/company/", "crunchbase");
            pageNum++;
        }
        driver.close();
    }

    private static void getLinkFromOnclickElement(WebDriver driver, String searchUrl, String baseUrl, String key) {

        String url = searchUrl + pageNum;
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
                    String[] tmpArray = {convertToUrl(baseUrl, title).toLowerCase(), title.toLowerCase()};
                    initialUrls.add(tmpArray);
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }

        for (String[] curEntry : initialUrls) {

            String pulledUrl = pullCompanyUrl(driver, curEntry, key);
            if (pulledUrl != null) {
                urls.add(pulledUrl);
                System.out.println(pulledUrl);
                //PersistenceImpl_WriteToFile.appendToFile(fileOutput, pulledUrl + "\n");
                persistEntry(pulledUrl);
            }
        }
    }

    /**
     * very crude, just testing the functionality of making a service call, this really should be in a different class
     * @param data
     */
    private static void persistEntry(String data) {
        EntryTransport entryTransport = new EntryTransport();
        entryTransport.setDescription(data);
        Gson gson = new Gson();
        String jsonData = gson.toJson(data);
        webServiceClient.servicePost("", jsonData, webServiceContentType);
    }
}
