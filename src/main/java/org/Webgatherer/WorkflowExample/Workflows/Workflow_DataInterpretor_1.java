package org.Webgatherer.WorkflowExample.Workflows;

import com.google.inject.Injector;
import com.google.inject.Provider;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Core.Threadable.DataInterpreatation.DataInterpretor;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParser;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParserImpl;
import org.Webgatherer.WorkflowExample.DataHolders.DataHolder;
import org.Webgatherer.WorkflowExample.DataHolders.DataHolderImpl;
import org.Webgatherer.WorkflowExample.Status.StatusIndicator;
import org.Webgatherer.WorkflowExample.Workflows.Base.WorkflowBase;
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
    private Provider<HtmlCleaner> htmlCleanerProvider;
    private DataHolder dataHolder;
    //TODO DI
    HtmlCleaner htmlCleaner = htmlCleanerProvider.get();
    HtmlParser htmlParser;

    public Workflow_DataInterpretor_1(Injector injector) {
        super(injector);
        htmlParser = new HtmlParserImpl(htmlCleaner);

    }

    public void runWorkflow(Map<String, Object> workflowParams) {
        this.htmlCleanerProvider = injector.getProvider(HtmlCleaner.class);

        DataInterpretor dataInterpretor = (DataInterpretor) workflowParams.get("dataInterpretor");
        ThreadCommunication threadCommunication = (ThreadCommunication) workflowParams.get("threadCommunication");
        FinalOutputContainer finalOutputContainer = (FinalOutputContainer) workflowParams.get("finalOutputContainer");

        String[] curEntry = threadCommunication.getFromPageQueue();

        String parsedHtml = htmlParser.getText(curEntry[1]);

        dataHolder = trie.get(curEntry[0]);
        if (dataHolder == null) {
            dataHolder = new DataHolderImpl();
            dataHolder.createContainer("aboutus", 1, 20);
            trie.put(curEntry[0], dataHolder);
        }

        if (curEntry.equals("aboutus") && dataHolder.checkIfContainerAvailable("aboutus") == StatusIndicator.AVAILABLE) {

        }

        addEmailAddresses(parsedHtml);

    }


    private void addPageToDataHolder(String label, String parsedHtml) {
        StatusIndicator status = dataHolder.checkIfContainerAvailable(label);
        if (status == StatusIndicator.DOESNOTEXIST) {
            dataHolder.createContainer(label, 1, 20);
        }
        dataHolder.addEntryToContainer("aboutus", parsedHtml);
    }


    //TODO some methods below should eventually be put into common 'utility' class for re-use

    private void addEmailAddresses(String parsedHtml) {

        //todo determine if its an email address

        dataHolder.addEmailAddress(parsedHtml);
    }

    /**
     * Extracts links from a page that match one from the list passed in, sends to sendback object with specified internal label
     *
     * @param hrefLabel
     * @param internaLabel
     */
    private void extractLinksForSendback(String parsedHtml, List<String> hrefLabel, String internaLabel) {

    }

}
