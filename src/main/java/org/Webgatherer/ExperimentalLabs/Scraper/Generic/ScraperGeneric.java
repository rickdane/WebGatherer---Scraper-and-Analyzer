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

    //protected String urlPrefix = "http://sfbay.craigslist.org/search/?areaID=1&catAbb=jjj&query=";
    protected String urlPrefix;
    protected String urlPostfix;
    protected String urlPatternWildcard = "#";

    protected HtmlParser htmlParser;
    //protected String baseDomainName = "http://craigslist.org";
    protected String baseDomainName;
    protected int pageIncrementAmnt;


    @Inject
    public ScraperGeneric(PageRetrieverThreadManagerScraper pageRetrieverThreadManager, HtmlParser htmlParser) {
        super(pageRetrieverThreadManager);
        this.htmlParser = htmlParser;

    }

    @Override
    public void configure(String urlPrefix, String urlPostfix, String baseDomainName, int pageIncrementAmnt) {
        this.baseDomainName = baseDomainName;
        this.urlPostfix = urlPostfix;
        this.urlPrefix = urlPrefix;
        this.pageIncrementAmnt = pageIncrementAmnt;
    }


    private String prepareUrlString(String searchStr, int pgNum) {
        StringBuilder strBld = new StringBuilder();
        strBld.append(urlPrefix);
        strBld.append(searchStr);
        strBld.append(urlPostfix);
        String retStr = strBld.toString().replace(urlPatternWildcard, String.valueOf(pgNum));
        return retStr;
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
        int pgNum = i * pageIncrementAmnt;

        String urlPrepared = prepareUrlString(searchString, pgNum);

        driver.get(urlPrepared);

        String pageSource = driver.getPageSource();

        Queue<String[]> queue = new ConcurrentLinkedQueue<String[]>();


        Map<String, String> links = htmlParser.extractLinks(baseDomainName, pageSource);

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
