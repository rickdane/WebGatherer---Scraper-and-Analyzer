package org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor;

import com.google.inject.Injector;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
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
    protected DataHolder dataHolder;
    protected String curEntryKey;
    protected String curPageBaseUrl;

    protected FinalOutputContainer finalOutputContainer;
    protected ThreadCommunication threadCommunication;

    protected final int CONTAINER_DEFAULT_MAX_ENTRIES = 1;
    protected final int CONTAINER_DEFAULT_MAX_ATTEMPTS = 1;

    protected List<String> trackSentBackLinks = new ArrayList<String>();
    protected HtmlParser htmlParser;

    protected enum PageQueueEntries {
        KEY, BASE_URL, CATEGORY, SCRAPED_PAGE;
    }

    public Workflow_DataInterpretorBase(Injector injector) {
        super(injector);
    }

    protected void setUp(Map<String, Object> workflowParams) {
        threadCommunication = (ThreadCommunication) workflowParams.get("threadCommunication");
        finalOutputContainer = (FinalOutputContainer) workflowParams.get("finalOutputContainer");
    }

    protected void addPageToDataHolder(String label, String parsedHtml) {
        StatusIndicator status = dataHolder.checkIfContainerAvailable(label);
        if (status == StatusIndicator.DOESNOTEXIST) {
            dataHolder.createContainer(label, CONTAINER_DEFAULT_MAX_ENTRIES, CONTAINER_DEFAULT_MAX_ATTEMPTS);
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

    protected void initializeDataHolder(String[] containerNamesToCreate) {
        dataHolder = trie.get(curEntryKey);
        if (dataHolder == null) {
            dataHolder = new DataHolderImpl();
            for (String curName : containerNamesToCreate) {
                dataHolder.createContainer(curName, CONTAINER_DEFAULT_MAX_ENTRIES, CONTAINER_DEFAULT_MAX_ATTEMPTS);
            }
            trie.put(curEntryKey, dataHolder);
        }
    }

}
