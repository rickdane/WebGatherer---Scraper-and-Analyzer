package org.Webgatherer.WorkflowExample.Workflows;

import com.google.inject.Injector;
import com.google.inject.Provider;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Core.Threadable.DataInterpreatation.DataInterpretor;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParser;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParserImpl;
import org.Webgatherer.WorkflowExample.DataHolders.ContainerBase;
import org.Webgatherer.WorkflowExample.DataHolders.DataHolder;
import org.Webgatherer.WorkflowExample.DataHolders.DataHolderImpl;
import org.Webgatherer.WorkflowExample.Status.StatusIndicator;
import org.Webgatherer.WorkflowExample.Workflows.Base.WorkflowBase;
import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.ardverk.collection.PatriciaTrie;
import org.ardverk.collection.StringKeyAnalyzer;
import org.ardverk.collection.Trie;
import org.htmlcleaner.HtmlCleaner;

import java.util.*;

/**
 * @author Rick Dane
 */
public class Workflow_DataInterpretor_1 extends WorkflowBase {

    private Trie<String, DataHolder> trie = new PatriciaTrie<String, DataHolder>(StringKeyAnalyzer.INSTANCE);

    private DataHolder dataHolder;
    private String curEntryKey;
    private String curPageBaseUrl;
    private FinalOutputContainer finalOutputContainer;
    private ThreadCommunication threadCommunication;

    private final int CONTAINER_DEFAULT_MAX_ENTRIES = 1;
    private final int CONTAINER_DEFAULT_MAX_ATTEMPTS = 1;

    private List<String> trackSentBackLinks = new ArrayList<String>();
    //TODO DI

    HtmlParser htmlParser;

    private enum PageQueueEntries {
        KEY, BASE_URL, CATEGORY, SCRAPED_PAGE;
    }

    public Workflow_DataInterpretor_1(Injector injector) {
        super(injector);
        htmlParser = new HtmlParserImpl(htmlCleaner);

    }

    public void runWorkflow(Map<String, Object> workflowParams) {
        this.htmlCleanerProvider = injector.getProvider(HtmlCleaner.class);

        DataInterpretor dataInterpretor = (DataInterpretor) workflowParams.get("dataInterpretor");
        threadCommunication = (ThreadCommunication) workflowParams.get("threadCommunication");
        finalOutputContainer = (FinalOutputContainer) workflowParams.get("finalOutputContainer");

        String[] curEntry = threadCommunication.getFromPageQueue();

        curEntryKey = curEntry[PageQueueEntries.KEY.ordinal()];
        String curScrapedPage = curEntry[PageQueueEntries.SCRAPED_PAGE.ordinal()];
        curPageBaseUrl = null;

        String curCategory = curEntry[PageQueueEntries.CATEGORY.ordinal()];
        curPageBaseUrl = curEntry[PageQueueEntries.BASE_URL.ordinal()];

        dataHolder = trie.get(curEntryKey);
        if (dataHolder == null) {
            dataHolder = new DataHolderImpl();
            dataHolder.createContainer("aboutus", CONTAINER_DEFAULT_MAX_ENTRIES, CONTAINER_DEFAULT_MAX_ATTEMPTS);
            trie.put(curEntryKey, dataHolder);
        }


        if (curCategory != null && curCategory.equals("aboutus") && dataHolder.checkIfContainerAvailable("aboutus") == StatusIndicator.AVAILABLE) {
            addPageToDataHolder("aboutus", curScrapedPage);
            //addEmailAddresses(curScrapedPage);

        }

        //move any finished containers to the finished queue
        if (!dataHolder.isFinishedContainerQueueEmpty()) {
            addToFinalOutputContainer();
        }

        LinkedList<String> tokenstoCheckFor = new LinkedList<String>();
        tokenstoCheckFor.add("about us");
        tokenstoCheckFor.add("info");
        extractLinksForSendbackThatMatchKeys(tokenstoCheckFor, curScrapedPage, "aboutus");
    }


    private void addPageToDataHolder(String label, String parsedHtml) {
        StatusIndicator status = dataHolder.checkIfContainerAvailable(label);
        if (status == StatusIndicator.DOESNOTEXIST) {
            dataHolder.createContainer(label, CONTAINER_DEFAULT_MAX_ENTRIES, CONTAINER_DEFAULT_MAX_ATTEMPTS);
        }
        dataHolder.addEntryToContainer(label, parsedHtml);
    }


    //TODO some methods below should eventually be put into common 'utility' class for re-use

    private void addEmailAddresses(String parsedHtml) {

        //todo determine if its an email address

        dataHolder.addEmailAddress(parsedHtml);
    }

    /**
     * Extracts links from a page that match one from the list passed in, sends to sendback object with specified internal label
     */
    private void extractLinksForSendbackThatMatchKeys(LinkedList<String> keys, String parsedHtml, String internalLabel) {

        Map<String, String> links = htmlParser.extractLinks(curPageBaseUrl, parsedHtml);

        for (Map.Entry<String, String> entry : links.entrySet()) {
            String curLinkLabel = entry.getKey();
            if (!keys.contains(curLinkLabel.toLowerCase())) {
                continue;
            }
            String url = entry.getValue();
            if (!trackSentBackLinks.contains(curLinkLabel)) {
                String[] strHolder = {curEntryKey, url, internalLabel, null};
                threadCommunication.addToSendbackDataHolder(strHolder);
                trackSentBackLinks.add(curLinkLabel);
            }
        }
    }

    @Override
    public void destroyCleanly() {
        dataHolder.destroyRetrieveFinalData();
        addToFinalOutputContainer();
    }

    private void addToFinalOutputContainer() {
        while (!dataHolder.isFinishedContainerQueueEmpty()) {
            ContainerBase cb = dataHolder.pullFromFinishedContainerQueue();
            finalOutputContainer.addToFinalOutputContainer(curEntryKey + "." + cb.getIdentifier(), cb);
        }
    }

}
