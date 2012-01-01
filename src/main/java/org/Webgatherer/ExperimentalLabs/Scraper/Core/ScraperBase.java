package org.Webgatherer.ExperimentalLabs.Scraper.Core;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.Webgatherer.ExperimentalLabs.DependencyInjection.DependencyBindingModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rick Dane
 */
public abstract class ScraperBase {

    private int delay = 100;
    protected WebDriver driver;

    protected int numberResultsPerPage = 10;
    protected int miniSleepDuration = 25;
    protected int afterRunSleepDuration = 5000;

    protected final String URL_IDENTIFIER = "href";

    protected PageRetrieverThreadManagerScraper pageRetrieverThreadManager;

    @Inject
    public ScraperBase(PageRetrieverThreadManagerScraper pageRetrieverThreadManager) {
        this.pageRetrieverThreadManager = pageRetrieverThreadManager;
    }

    public List<String[]> run(String searchString, int startPageNumber, int endPageNumber) {

        Injector injector = Guice.createInjector(new DependencyBindingModule());
        WebDriverFactory webDriverFactory = injector.getInstance(WebDriverFactory.class);
        driver = webDriverFactory.createNewWebDriver();

        ThreadCommunication threadCommunication = injector.getInstance(ThreadCommunication.class);

        pageRetrieverThreadManager.configure(threadCommunication);

        int i = startPageNumber;
        while (i <= endPageNumber) {
            customRunActions(i, threadCommunication, searchString);
            i++;
        }

        try {
            Thread.sleep(afterRunSleepDuration);
        } catch (InterruptedException e) {
        }

        List<String[]> retList = new ArrayList<String[]>();

        while (!threadCommunication.isOutputDataHolderEmpty()) {

            String[] resultEntry = threadCommunication.getFromOutputDataHolder();

            String url = parseUrl(resultEntry[8]);
            if (url != null) {
                resultEntry[1] = url;
                retList.add(resultEntry);
            }
        }

        return retList;
    }

    protected abstract String parseUrl(String inputUrl);

    protected abstract void customRunActions(int i, ThreadCommunication threadCommunication, String searchString);

    public void sleep() {
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


    protected String pullCompanyUrl(WebDriver driver, String[] origUrl, String companyName) {
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

    protected String convertToUrl(String inputStr, String baseUrl) {
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
