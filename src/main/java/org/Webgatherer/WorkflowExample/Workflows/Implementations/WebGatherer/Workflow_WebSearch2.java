package org.Webgatherer.WorkflowExample.Workflows.Implementations.WebGatherer;

import com.google.inject.Injector;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunicationBase;
import org.Webgatherer.CoreEngine.Core.Threadable.WebGather.WebGather;
import org.Webgatherer.WorkflowExample.Workflows.Base.Common.WorkflowBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Rick Dane
 */
public class Workflow_WebSearch2 extends WorkflowBase {

    public Workflow_WebSearch2(Injector injector) {
        super(injector);
    }

    public void runWorkflow(Map<String, Object> workflowParams) {

        WebGather webGather = (WebGather) workflowParams.get("webGather");

        ThreadCommunication threadCommunication = (ThreadCommunication) workflowParams.get("threadCommunication");
        FinalOutputContainer finalOutputContainer = (FinalOutputContainer) workflowParams.get("finalOutputContainer");

        String[] curEntry = threadCommunication.getFromPageQueue();
        if (curEntry == null) {
            return;
        }

        String searchString = curEntry[ThreadCommunicationBase.PageQueueEntries.CUSTOM_PARAM.ordinal()];
        String key = curEntry[ThreadCommunicationBase.PageQueueEntries.KEY.ordinal()];
        String customLabel = curEntry[ThreadCommunicationBase.PageQueueEntries.CUSTOM_LABEL.ordinal()];
        int numberOfPagesToScrape = Integer.parseInt(curEntry[ThreadCommunicationBase.PageQueueEntries.NUM_PAGES_TOSCRAPE.ordinal()]);

        int i = 1;
        while (i <= numberOfPagesToScrape) {
            int start = i * 10 - 10;
            String url = "https://www.google.com/search?gcx=c&sourceid=chrome&ie=UTF-8&q=google+places#q=" + searchString + "&hl=en&tbm=plcs&prmd=imvns&start=" + start;

            //have to get new instance each time or there is a problem with WebElements, not sure why and can hopefully correct this at some point
            WebDriver driver = webGather.getNewWebDriver();

            driver.get(url);

            List<WebElement> links;
            links = driver.findElements(By.tagName("a"));
            for (WebElement link : links) {
                String linkStr = null;
                try {
                    linkStr = link.getAttribute("href");
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                if (linkStr != null && checkIfMatch(linkStr)) {
                    String[] entry = {key, linkStr, null, null, null, null, customLabel};
                    threadCommunication.addToOutputDataHolder(entry);
                }
            }
            i++;
            driver.close();
        }

        if (threadCommunication.isPageQueueEmpty()) {
            threadCommunication.setIsWebGathererThreadFinished(true);
        }
    }

    private boolean checkIfMatch(String linkStr) {
        List<String> negativeMatches = new ArrayList<String>();
        negativeMatches.add("google");
        negativeMatches.add("youtube");

        List<String> positiveMatches = new ArrayList<String>();
        positiveMatches.add("http");

        for (String curMatch : negativeMatches) {
            if (linkStr.contains(curMatch)) {
                return false;
            }
        }

        for (String curMatch : positiveMatches) {
            if (!linkStr.contains(curMatch)) {
                return false;
            }
        }

        return true;
    }
}
