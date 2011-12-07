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

        runWorkflowSetup(workflowParams);

        if (curScrapedPage != null) {
            String[] checkFor1 = {"career", "job", "employment"};
            checkForMatchesToSendBackLink(checkFor1, "careers");

            String[] checkFor3 = {"about us", "info"};
            checkForMatchesToSendBackLink(checkFor3, "aboutus");
        }

        dataHolder = trie.get(curEntryKey);

        if (curCategory != null && curCategory.equals("aboutus")) {
            addPageToDataHolder("aboutus", curPageBaseUrl);
        }

        if (curCategory != null && curCategory.equals("careers")) {
            textExtraction.extractAllLinksFromSameSite(this, curScrapedPage, "java", curEntryKey);
            if (curWebPageText.indexOf("java") >= 0) {
                addPageToDataHolder("java", curPageBaseUrl);
            }

        }

        //move any finished containers to the finished queue
        if (dataHolder != null && !dataHolder.isFinishedContainerQueueEmpty()) {
            addToFinalOutputContainer();
        }

    }

    protected void checkForMatchesToSendBackLink(String[] matches, String label) {
        LinkedList<String> tokenstoCheckFor = new LinkedList<String>();
        for (String curMatch : matches) {
            tokenstoCheckFor.add(curMatch);
        }
        textExtraction.extractLinksForSendbackThatMatchKeys(this, tokenstoCheckFor, curScrapedPage, label, curEntryKey);
    }

    protected Map<String, int[]> prepareInitParams() {
        Map<String, int[]> initParams = new HashMap<String, int[]>();
        int[] one = {1, 14};
        initParams.put("aboutus", one);

        int[] two = {1, 18};
        initParams.put("careers", two);
        return initParams;
    }
}
