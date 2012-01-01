package org.Webgatherer.ExperimentalLabs.Scraper.Core;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.Webgatherer.Common.Properties.PropertiesContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunicationBase;
import org.Webgatherer.CoreEngine.Core.Threadable.WebGather.PageRetrieverThreadManager;
import org.Webgatherer.CoreEngine.Core.Threadable.WebGather.ThreadCommunicationPageRetriever;
import org.Webgatherer.CoreEngine.Core.Threadable.WebGather.ThreadRetrievePage;
import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.Webgatherer.ExperimentalLabs.Scraper.Indeed.ThreadRetrievePageIndeed;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Rick Dane
 */
public class PageRetrieverThreadManagerScraper extends PageRetrieverThreadManager {

    protected WebDriver driver;
    public static int sizeOfStringArrayEnum = 9;
    protected Provider<ThreadRetrievePageIndeed> threadRetrievePageIndeedProvider;

    @Inject
    public PageRetrieverThreadManagerScraper(Provider<ThreadRetrievePage> threadRetrievePageProvider, PropertiesContainer propertiesContainer, ThreadCommunicationPageRetriever threadCommunicationPageRetriever, WebDriverFactory webDriverFactory, Provider<ThreadRetrievePageIndeed> threadRetrievePageIndeedProvider) {
        super(threadRetrievePageProvider, propertiesContainer, threadCommunicationPageRetriever);
        driver = webDriverFactory.createNewWebDriver();
        this.threadRetrievePageIndeedProvider = threadRetrievePageIndeedProvider;
    }

    @Override
    protected void launchThread(String[] entry, int retrieveType) {
        ThreadRetrievePageIndeed threadRetrievePage = threadRetrievePageIndeedProvider.get();
        threadRetrievePage.configure(entry, threadCommunication, retrieveType, threadCommunicationPageRetriever);
        threadRetrievePage.start();
    }

    @Override
    protected boolean determineIfCanStartThreadImmediately(String[] entry) {
        return true;
    }

    /**
     * work in progress, meant to be called just after configure
     *
     * @param intialPageUrl
     * @return
     */
    public Queue getInitialJavascriptLinksAddToPageQueue(String intialPageUrl, String javascriptLinkIdentifier, String customAttributeLabel) {
        driver.get(intialPageUrl);

        List<WebElement> links;
        List<String[]> initialUrls = new ArrayList<String[]>();
        List<String> urls = new ArrayList<String>();

        links = driver.findElements(By.tagName("a"));

        Queue<String[]> queue = new ConcurrentLinkedQueue<String[]>();

        for (WebElement link : links) {
            String customAttribute = null;
            try {
                customAttribute = link.getAttribute(customAttributeLabel);
                if (customAttribute.contains(javascriptLinkIdentifier)) {
                    customAttribute = customLogic(customAttribute);
                    String[] testEntry = new String[sizeOfStringArrayEnum];
                    testEntry[ThreadCommunicationBase.PageQueueEntries.BASE_URL.ordinal()] = intialPageUrl;
                    testEntry[ThreadCommunicationBase.PageQueueEntries.CUSTOM_PARAM.ordinal()] = customAttribute;
                    queue.add(testEntry);
                }
            } catch (Exception e) {
            }
        }
        return queue;
    }

    private String customLogic(String link) {
        String[] pieces = link.split("=");

        String retString = "/rc/clk?jk=" + pieces[1];
        return retString;
    }
}
