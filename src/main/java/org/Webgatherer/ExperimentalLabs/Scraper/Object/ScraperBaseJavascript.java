package org.Webgatherer.ExperimentalLabs.Scraper.Object;

import com.google.inject.Inject;
import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParser;
import org.Webgatherer.Persistence.InputOutput.PersistenceImpl_WriteToFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rick Dane
 */
public class ScraperBaseJavascript extends ScraperBaseDepr {

    private int delay = 100;
    private int pageNum = 1;
    private int maxPages = 67;
    private String fileOutput;


    @Inject
    public ScraperBaseJavascript(WebDriverFactory webDriverFactory, HtmlParser htmlParser) {
        super(webDriverFactory, htmlParser);
    }

    protected void configure(String fileOutput) {
        this.fileOutput = fileOutput;
    }

    protected void sleep() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    protected String prepareBaseDomainUrl(String url) {
        int index = url.indexOf("/");

        if (index != -1) {
            url = url.substring(0, index);
        }
        return url;
    }

    protected String pullCompanyUrl(String[] origUrl, String companyName) {
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

    public void getLinksFromOnclick(String searchUrl, String baseUrl, String key) {
        String url = searchUrl + pageNum;
        driver.get(url);

        List<WebElement> links;
        List<String[]> initialUrls = new ArrayList<String[]>();
        List<String> urls = new ArrayList<String>();

        links = driver.findElements(By.tagName("a"));

        for (WebElement link : links) {
            getLinkFromOnclickElementInner_One(link, baseUrl, initialUrls);
        }

        for (String[] curEntry : initialUrls) {

            String pulledUrl = pullCompanyUrl(curEntry, key);
            if (pulledUrl != null) {
                urls.add(pulledUrl);
                System.out.println(pulledUrl);
                PersistenceImpl_WriteToFile.appendToFile(fileOutput, pulledUrl + "\n");
            }
        }
    }


    //THE METHODS BELOW ARE ESSENTIALLY "WORKFLOW" METHODS AS, AT LEAST FOR NOW, THEY HAVE TO BE CUSTOMIZED BASED ON WHAT IS BEING SCRAPED

    private void getLinkFromOnclickElementInner_One(WebElement link, String baseUrl, List<String[]> initialUrls) {

        String onclick = null;
        try {
            onclick = link.getAttribute("onclick");
            if (onclick.startsWith("snap_to_marker")) {
                String title = link.getAttribute("title");
                sleep();
                link.click();
                String[] tmpArray = {convertToUrl_One(baseUrl, title).toLowerCase(), title.toLowerCase()};
                initialUrls.add(tmpArray);
            }
        } catch (Exception e) {
        }
    }

    protected String convertToUrl_One(String inputStr, String baseUrl) {
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
