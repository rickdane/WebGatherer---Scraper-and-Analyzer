package org.Webgatherer.ExperimentalLabs.Scraper.Indeed;

import com.google.inject.Inject;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.ExperimentalLabs.Scraper.Core.PageRetrieverThreadManagerScraper;
import org.Webgatherer.ExperimentalLabs.Scraper.Core.ScraperBase;
import org.Webgatherer.WorkflowExample.Workflows.Implementations.WebGatherer.EnumUrlRetrieveOptions;

import java.util.Queue;

/**
 * @author Rick Dane
 */
public class ScraperIndeed extends ScraperBase {

    protected String urlPrefix = "http://www.indeed.com/jobs?q=";
    protected String urlSecond = "%2C+ca&start=";
    protected String javaScriptLinkIdentifier = "/rc/clk?jk=";

    @Inject
    public ScraperIndeed(PageRetrieverThreadManagerScraper pageRetrieverThreadManager) {
        super(pageRetrieverThreadManager);
    }

    protected void customRunActions(int i, ThreadCommunication threadCommunication, String searchString) {
        int pgNum = i * numberResultsPerPage;
        Queue queue = pageRetrieverThreadManager.getInitialJavascriptLinksAddToPageQueue(urlPrefix + searchString + urlSecond + pgNum, javaScriptLinkIdentifier, URL_IDENTIFIER);
        threadCommunication.setPageQueue(queue);
        while (!threadCommunication.isPageQueueEmpty()) {
            try {
                Thread.sleep(miniSleepDuration);
            } catch (InterruptedException e) {
            }
            pageRetrieverThreadManager.run(EnumUrlRetrieveOptions.HTMLPAGE.ordinal());
        }
        System.out.println("iterate main loop");
        i++;
    }


    protected String parseUrl(String inputUrl) {
        String url = null;
        String[] split1 = inputUrl.split("\\~");
        if (split1.length >= 2) {
            String[] split2 = split1[1].split("\\#");

            if (split1.length >= 1) {
                url = split2[0];
            }
        }
        return url;
    }
}
