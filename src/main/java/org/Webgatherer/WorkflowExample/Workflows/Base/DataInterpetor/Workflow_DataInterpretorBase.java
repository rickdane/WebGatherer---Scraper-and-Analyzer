package org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor;

import com.google.inject.Injector;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunicationBase;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParser;
import org.Webgatherer.WorkflowExample.DataHolders.ContainerBase;
import org.Webgatherer.WorkflowExample.DataHolders.DataHolder;
import org.Webgatherer.WorkflowExample.DataHolders.DataHolderImpl;
import org.Webgatherer.WorkflowExample.Status.StatusIndicator;
import org.Webgatherer.WorkflowExample.Workflows.Base.Common.WorkflowBase;
import org.ardverk.collection.PatriciaTrie;
import org.ardverk.collection.StringKeyAnalyzer;
import org.ardverk.collection.Trie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Rick Dane
 */
public abstract class Workflow_DataInterpretorBase extends WorkflowBase {

    protected Trie<String, DataHolder> trie = new PatriciaTrie<String, DataHolder>(StringKeyAnalyzer.INSTANCE);
    protected List<String> negativeMatchUrlList = new ArrayList<String>();
    protected DataHolder dataHolder;
    protected String curEntryKey;
    protected String curPageBaseUrl;
    protected String curCategory;
    protected String curScrapedPage;
    protected String curPageBaseDomainUrl;

    protected FinalOutputContainer finalOutputContainer;
    protected ThreadCommunication threadCommunication;

    protected List<String> trackSentBackLinks = new ArrayList<String>();
    protected HtmlParser htmlParser;
    protected String curWebPageText;

    protected int containerDefaultMaxEntries;
    protected int containerDefaultMaxAttempts;

    /**
     * This is meant to be called each time the runWorfklow() method is called, it combines functionality that different workflows
     * will need to avoid having to insert boilerplate into each custom runWorkflow() method
     *
     * @param workflowParams
     */
    protected void runWorkflowSetup(Map<String, Object> workflowParams) {

        containerDefaultMaxEntries = Integer.parseInt(properties.getProperty("workflow_DataInterpretorBase_containerDefaultMaxEntries"));
        containerDefaultMaxAttempts = Integer.parseInt(properties.getProperty("workflow_DataInterpretorBase_containerDefaultMaxAttempts"));

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

    protected void setUp(Map<String, Object> workflowParams) {
        threadCommunication = (ThreadCommunication) workflowParams.get("threadCommunication");
        finalOutputContainer = (FinalOutputContainer) workflowParams.get("finalOutputContainer");

    }

    protected void addPageToDataHolder(String label, String parsedHtml) {
        if (dataHolder != null && dataHolder.checkIfContainerAvailable(label) != StatusIndicator.AVAILABLE) {
            return;
        }

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

    protected boolean determineIfPageContains(String[] mustContainAtLeastOne, String[] mustContainAllEntries, String searchInText) {

        searchInText = searchInText.toLowerCase();

        for (String curEntry : mustContainAllEntries) {
            if (!searchInText.contains(curEntry.toLowerCase())) {
                return false;
            }
        }

        for (String curEntry : mustContainAtLeastOne) {
            if (searchInText.contains(curEntry.toLowerCase())) {
                return true;
            }
        }
        return true;
    }

    protected void addToFinalOutputContainer() {
        while (!dataHolder.isFinishedContainerQueueEmpty()) {
            ContainerBase cb = dataHolder.pullFromFinishedContainerQueue();
            finalOutputContainer.addToFinalOutputContainer(curEntryKey + "." + cb.getIdentifier(), cb);
        }
    }
}
