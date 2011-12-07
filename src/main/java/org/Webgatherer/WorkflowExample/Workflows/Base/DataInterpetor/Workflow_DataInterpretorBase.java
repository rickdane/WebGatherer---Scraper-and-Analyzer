package org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor;

import com.google.inject.Injector;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunicationBase;
import org.Webgatherer.CoreEngine.Core.Threadable.WebGather.WebGather;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParser;
import org.Webgatherer.WorkflowExample.DataHolders.ContainerBase;
import org.Webgatherer.WorkflowExample.DataHolders.DataHolder;
import org.Webgatherer.WorkflowExample.DataHolders.DataHolderImpl;
import org.Webgatherer.WorkflowExample.Status.StatusIndicator;
import org.Webgatherer.WorkflowExample.Workflows.Base.Common.WorkflowBase;
import org.ardverk.collection.PatriciaTrie;
import org.ardverk.collection.StringKeyAnalyzer;
import org.ardverk.collection.Trie;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Rick Dane
 */
public abstract class Workflow_DataInterpretorBase extends WorkflowBase {

    protected Trie<String, DataHolder> trie = new PatriciaTrie<String, DataHolder>(StringKeyAnalyzer.INSTANCE);
    protected DataHolder dataHolder;
    protected String curEntryKey;
    protected String curPageBaseUrl;
    protected String curCategory;
    protected String curScrapedPage;
    protected String curPageBaseDomainUrl;

    protected FinalOutputContainer finalOutputContainer;
    protected ThreadCommunication threadCommunication;

    protected final int CONTAINER_DEFAULT_MAX_ENTRIES = 1;
    protected final int CONTAINER_DEFAULT_MAX_ATTEMPTS = 1;

    protected List<String> trackSentBackLinks = new ArrayList<String>();
    protected HtmlParser htmlParser;
    protected String curWebPageText;

    /**
     * This is meant to be called each time the runWorfklow() method is called, it combines functionality that different workflows
     * will need to avoid having to insert boilerplate into each custom runWorkflow() method
     *
     * @param workflowParams
     */
    protected void runWorkflowSetup(Map<String, Object> workflowParams) {
        //TODO refactor this as it doesn't need to be called with each workflow iteration
        setUp(workflowParams);

        String[] curEntry = threadCommunication.getFromPageQueue();

        curEntryKey = curEntry[ThreadCommunicationBase.PageQueueEntries.KEY.ordinal()];
        curScrapedPage = curEntry[ThreadCommunicationBase.PageQueueEntries.SCRAPED_PAGE.ordinal()];
        curPageBaseUrl = curEntry[ThreadCommunicationBase.PageQueueEntries.BASE_URL.ordinal()];
        curCategory = curEntry[ThreadCommunicationBase.PageQueueEntries.CATEGORY.ordinal()];

        curWebPageText = htmlParser.getText(curScrapedPage);

        curPageBaseDomainUrl = prepareBaseDomainUrl(curEntryKey);
    }

    /**
     * The url being passed in should not have http:// or anything prefixing it already so we just need to check for a backslash and remove
     * it and anything trailing it
     *
     * @param url
     * @return
     */
    private String prepareBaseDomainUrl(String url) {
        int index = url.indexOf("/");

        if (index != -1) {
            url = url.substring(0, index);
        }
        return "http://" + url;
    }


    public Workflow_DataInterpretorBase(Injector injector) {
        super(injector);
    }

    protected abstract Map<String, int[]> prepareInitParams();

    protected void setUp(Map<String, Object> workflowParams) {
        threadCommunication = (ThreadCommunication) workflowParams.get("threadCommunication");
        finalOutputContainer = (FinalOutputContainer) workflowParams.get("finalOutputContainer");

    }

    protected void addPageToDataHolder(String label, String parsedHtml) {
        if (dataHolder != null && dataHolder.checkIfContainerAvailable(label) != StatusIndicator.AVAILABLE) {
            return;
        }

        //TODO fix this, shouldn't be hard-coded
        int containerDefaultMaxEntries = 1;
        int containerDefaultMaxAttempts = 15;

        dataHolder = trie.get(curEntryKey);
        if (dataHolder == null) {
            dataHolder = new DataHolderImpl();


            dataHolder.createContainer(label, containerDefaultMaxEntries, containerDefaultMaxAttempts);
            trie.put(curEntryKey, dataHolder);
        }
        StatusIndicator status = dataHolder.checkIfContainerAvailable(label);
        if (status == StatusIndicator.DOESNOTEXIST) {
            dataHolder.createContainer(label, containerDefaultMaxEntries, containerDefaultMaxAttempts);
        }
        dataHolder.addEntryToContainer(label, parsedHtml);
    }

    @Override
    public void destroyCleanly() {
        dataHolder.destroyRetrieveFinalData();
        addToFinalOutputContainer();
    }

    protected void addToFinalOutputContainer() {
        while (!dataHolder.isFinishedContainerQueueEmpty()) {
            ContainerBase cb = dataHolder.pullFromFinishedContainerQueue();
            finalOutputContainer.addToFinalOutputContainer(curEntryKey + "." + cb.getIdentifier(), cb);
        }
    }

    protected void initializeDataHolder(Map<String, int[]> initParams) {
//        dataHolder = trie.get(curEntryKey);
//        if (dataHolder == null) {
//
//        for (Map.Entry<String, int[]> curEntry : initParams.entrySet()) {
//            curEntryKey = curEntry.getKey();
//            dataHolder = new DataHolderImpl();
//            int[] numbers = curEntry.getValue();
//            int containerDefaultMaxEntries = numbers[0];
//            int containerDefaultMaxAttempts = numbers[1];
//
//            dataHolder.createContainer(curEntry.getKey(), containerDefaultMaxEntries, containerDefaultMaxAttempts);
//            trie.put(curEntryKey, dataHolder);
//        }
        //}
    }

}
