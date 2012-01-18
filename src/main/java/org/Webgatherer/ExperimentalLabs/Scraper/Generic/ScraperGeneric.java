package org.Webgatherer.ExperimentalLabs.Scraper.Generic;

import com.google.inject.Inject;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunicationBase;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParser;
import org.Webgatherer.ExperimentalLabs.Scraper.Core.PageRetrieverThreadManagerScraper;
import org.Webgatherer.ExperimentalLabs.Scraper.Core.ScraperBase;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Rick Dane
 */
public class ScraperGeneric extends ScraperBase {

    protected String urlPrefix = "http://sfbay.craigslist.org/search/?areaID=1&catAbb=jjj&query=";
    protected String urlSecond = "";

    protected int numberResultsPerPageCustom = 100;
    protected HtmlParser htmlParser;

    @Inject
    public ScraperGeneric(PageRetrieverThreadManagerScraper pageRetrieverThreadManager, HtmlParser htmlParser) {
        super(pageRetrieverThreadManager);
        this.htmlParser = htmlParser;
    }

    /**
     * This is for pages where the links are right on the page, no cleverness is required to get them, such as using JavaScript, so we
     * are cutting out steps that would happen in another scraper, such as the indeed scraper, for example
     *
     * @param i
     * @param threadCommunication
     * @param searchString
     */
    @Override
    protected void customRunActions(int i, ThreadCommunication threadCommunication, String searchString) {
        int pgNum = i * numberResultsPerPageCustom;

        driver.get(urlPrefix + searchString + urlSecond);

        String pageSource = driver.getPageSource();

        Queue<String[]> queue = new ConcurrentLinkedQueue<String[]>();


        Map<String, String> links = htmlParser.extractLinks("http://craigslist.org", pageSource);

        for (Map.Entry<String, String> curEntry : links.entrySet()) {
            String[] outputEntry = new String[PageRetrieverThreadManagerScraper.sizeOfStringArrayEnum];
            outputEntry[ThreadCommunicationBase.PageQueueEntries.CUSTOM_RET_VALUE.ordinal()] = curEntry.getValue();
            threadCommunication.addToOutputDataHolder(outputEntry);
        }

        i++;
    }


    protected String parseUrl(String inputUrl) {
        return inputUrl;
    }

}
