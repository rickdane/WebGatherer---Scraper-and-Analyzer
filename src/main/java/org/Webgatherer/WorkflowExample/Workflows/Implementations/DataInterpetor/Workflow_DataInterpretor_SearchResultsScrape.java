package org.Webgatherer.WorkflowExample.Workflows.Implementations.DataInterpetor;

import com.google.inject.Injector;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunicationBase;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParserImpl;
import org.Webgatherer.WorkflowExample.Status.StatusIndicator;
import org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor.TextExtraction;
import org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor.Workflow_DataInterpretorBase;
import org.htmlcleaner.HtmlCleaner;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Rick Dane
 */
public final class Workflow_DataInterpretor_SearchResultsScrape extends Workflow_DataInterpretorBase {

    private TextExtraction textExtraction;

    private String curCustomLabel;

    public Workflow_DataInterpretor_SearchResultsScrape(Injector injector) {
        super(injector);
        htmlParser = new HtmlParserImpl(htmlCleaner);
        textExtraction = injector.getInstance(TextExtraction.class);
        htmlCleanerProvider = injector.getProvider(HtmlCleaner.class);

        //initializeDataHolder(prepareInitParams());
    }

    @Override
    protected void runWorkflowSetup(Map<String, Object> workflowParams) {
        //TODO refactor this as it doesn't need to be called with each workflow iteration
        setUp(workflowParams);

        String[] curEntry = threadCommunication.getFromPageQueue();

        curCustomLabel = curEntry[ThreadCommunicationBase.PageQueueEntries.KEY.ordinal()];
        curEntryKey = "searchresults";
        curPageBaseUrl = null;

        curCategory = curEntry[ThreadCommunicationBase.PageQueueEntries.CATEGORY.ordinal()];
        curPageBaseUrl = curEntry[ThreadCommunicationBase.PageQueueEntries.BASE_URL.ordinal()];
    }

    @Override
    public void runWorkflow(Map<String, Object> workflowParams) {

        runWorkflowSetup(workflowParams);

        addPageToDataHolder(curCustomLabel, curPageBaseUrl);

        //move any finished containers to the finished queue
        if (!dataHolder.isFinishedContainerQueueEmpty()) {
            addToFinalOutputContainer();
        }
    }

    protected Map<String, int[]> prepareInitParams() {
        //0 = containerDefaultMaxEntries
        //1 = containerDefaultMaxAttempts

        Map<String, int[]> initParams = new HashMap<String, int[]>();
        int[] one = {50, 100};
        initParams.put(curCustomLabel, one);

        return initParams;
    }

    @Override
    public void destroyCleanly() {
        while (!threadCommunication.isPageQueueEmpty()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        dataHolder.destroyRetrieveFinalData();
        addToFinalOutputContainer();

    }
}
