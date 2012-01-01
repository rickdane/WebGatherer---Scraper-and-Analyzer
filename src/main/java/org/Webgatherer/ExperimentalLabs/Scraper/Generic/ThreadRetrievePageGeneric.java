package org.Webgatherer.ExperimentalLabs.Scraper.Generic;

import com.google.inject.Inject;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunicationBase;
import org.Webgatherer.CoreEngine.Core.Threadable.WebGather.ThreadRetrievePage;
import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParser;
import org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor.EmailExtractor;
import org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor.TextExtraction;

import java.util.Map;

/**
 * @author Rick Dane
 */
public class ThreadRetrievePageGeneric extends ThreadRetrievePage {

    protected EmailExtractor emailExtractor;
    private static String delimeter = "#";
    private static String delimeter2nd = "~";

    @Inject
    public ThreadRetrievePageGeneric(WebDriverFactory webDriverFactory, TextExtraction textExtraction, HtmlParser htmlParser, EmailExtractor emailExtractor) {
        super(webDriverFactory, textExtraction, htmlParser);
        this.emailExtractor = emailExtractor;
    }

    /**
     * In this case the method is misnamed, as its really just extracting links from the original page
     * TODO: need to rework this so it makes more sense when used in this context
     */
    @Override
    protected void getPage() {

        String scrapedPage = entry[ThreadCommunicationBase.PageQueueEntries.SCRAPED_PAGE.ordinal()];
        String baseUrl = entry[ThreadCommunicationBase.PageQueueEntries.BASE_URL.ordinal()];

        Map<String, String> links = htmlParser.extractLinks(baseUrl, scrapedPage);

        for (Map.Entry<String, String> curEntry : links.entrySet()) {
            entry[ThreadCommunicationBase.PageQueueEntries.CUSTOM_RET_VALUE.ordinal()] = curEntry.getValue();
        }

        threadCommunication.addToOutputDataHolder(entry);
        System.out.println("thread added to data output");
    }


    @Override
    protected boolean actionIfUrlValid
            () {
        return true;
    }
}
