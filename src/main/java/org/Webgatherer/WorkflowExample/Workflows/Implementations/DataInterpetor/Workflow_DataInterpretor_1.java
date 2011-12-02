package org.Webgatherer.WorkflowExample.Workflows.Implementations.DataInterpetor;

import com.google.inject.Injector;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Core.Threadable.DataInterpreatation.DataInterpretor;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParserImpl;
import org.Webgatherer.WorkflowExample.DataHolders.ContainerBase;
import org.Webgatherer.WorkflowExample.DataHolders.DataHolderImpl;
import org.Webgatherer.WorkflowExample.Status.StatusIndicator;
import org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor.TextExtraction;
import org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor.Workflow_DataInterpretorBase;
import org.htmlcleaner.HtmlCleaner;
import sun.plugin.dom.core.Text;

import java.util.*;

/**
 * @author Rick Dane
 */
public final class Workflow_DataInterpretor_1 extends Workflow_DataInterpretorBase {

    private TextExtraction textExtraction;

    public Workflow_DataInterpretor_1(Injector injector) {
        super(injector);
        htmlParser = new HtmlParserImpl(htmlCleaner);
        textExtraction = injector.getInstance(TextExtraction.class);
        htmlCleanerProvider = injector.getProvider(HtmlCleaner.class);

        initializeDataHolder(prepareInitParams());
    }


    @Override
    public void runWorkflow(Map<String, Object> workflowParams) {

        //TODO refactor this as it doesn't need to be called with each workflow iteration
        setUp(workflowParams);

        String[] curEntry = threadCommunication.getFromPageQueue();

        curEntryKey = curEntry[PageQueueEntries.KEY.ordinal()];
        String curScrapedPage = curEntry[PageQueueEntries.SCRAPED_PAGE.ordinal()];
        curPageBaseUrl = null;

        String curCategory = curEntry[PageQueueEntries.CATEGORY.ordinal()];
        curPageBaseUrl = curEntry[PageQueueEntries.BASE_URL.ordinal()];


        if (curCategory != null && curCategory.equals("aboutus") && dataHolder.checkIfContainerAvailable("aboutus") == StatusIndicator.AVAILABLE) {
            addPageToDataHolder("aboutus", curScrapedPage);

        }

        //move any finished containers to the finished queue
        if (!dataHolder.isFinishedContainerQueueEmpty()) {
            addToFinalOutputContainer();
        }

        LinkedList<String> tokenstoCheckFor = new LinkedList<String>();
        tokenstoCheckFor.add("about us");
        tokenstoCheckFor.add("info");
        textExtraction.extractLinksForSendbackThatMatchKeys(this, tokenstoCheckFor, curScrapedPage, "aboutus");
    }

    protected Map<String, int[]> prepareInitParams() {
        Map<String, int[]> initParams = new HashMap<String, int[]>();
        int[] one = {1, 2};
        initParams.put("aboutus", one);

        int[] two = {5, 25};
        initParams.put("email", two);
        return initParams;
    }
}
