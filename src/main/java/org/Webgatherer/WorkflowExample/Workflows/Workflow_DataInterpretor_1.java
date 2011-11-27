package org.Webgatherer.WorkflowExample.Workflows;

import com.google.inject.Injector;
import com.google.inject.Provider;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Core.Threadable.DataInterpreatation.DataInterpretor;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParser;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParserImpl;
import org.Webgatherer.WorkflowExample.DataHolders.DataHolder1;
import org.Webgatherer.WorkflowExample.Workflows.Base.WorkflowBase;
import org.ardverk.collection.PatriciaTrie;
import org.ardverk.collection.StringKeyAnalyzer;
import org.ardverk.collection.Trie;
import org.htmlcleaner.HtmlCleaner;
import sun.net.idn.StringPrep;
import sun.reflect.generics.tree.Tree;

import java.util.*;

/**
 * @author Rick Dane
 */
public class Workflow_DataInterpretor_1 extends WorkflowBase {


    private Trie<String, DataHolder1> trie = new PatriciaTrie<String, DataHolder1>(StringKeyAnalyzer.INSTANCE);
    private Provider<HtmlCleaner> htmlCleanerProvider;

    public Workflow_DataInterpretor_1(Injector injector) {
        super(injector);

    }


    public void runWorkflow(Map<String, Object> workflowParams) {
        this.htmlCleanerProvider = injector.getProvider(HtmlCleaner.class);

        DataInterpretor dataInterpretor = (DataInterpretor) workflowParams.get("dataInterpretor");
        ThreadCommunication threadCommunication = (ThreadCommunication) workflowParams.get("threadCommunication");
        FinalOutputContainer finalOutputContainer = (FinalOutputContainer) workflowParams.get("finalOutputContainer");

        HtmlCleaner htmlCleaner = htmlCleanerProvider.get();
        String[] curEntry = threadCommunication.getFromPageQueue();

        HtmlParser htmlParser = new HtmlParserImpl(htmlCleaner);
        String parsedHtml = htmlParser.getText(curEntry[1]);

         DataHolder1 dataHolder1 = trie.get(curEntry[0]);
         if (dataHolder1 == null) {
             dataHolder1 = new DataHolder1();

         }
        dataHolder1.addEmailAddress(null);
        dataHolder1.addContent("main",parsedHtml);

        trie.put(curEntry[0],dataHolder1);

        List<String> outputRow = new ArrayList<String>();
        outputRow.add(parsedHtml);
        finalOutputContainer.addToFinalOutputContainer(outputRow);

    }
}
