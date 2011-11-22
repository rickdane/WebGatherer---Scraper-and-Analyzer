package org.WorkflowExample.Workflows;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.Webgatherer.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.Core.Threadable.DataInterpreatation.DataInterpretor;
import org.Webgatherer.Utility.HtmlParsing.HtmlParser;
import org.Webgatherer.Utility.HtmlParsing.HtmlParserImpl;
import org.WorkflowExample.Workflows.Base.WorkflowBase;
import org.htmlcleaner.HtmlCleaner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Rick Dane
 */
public class Workflow_DataInterpretor_1 extends WorkflowBase {

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

        HtmlParser htmlParser = new HtmlParserImpl(htmlCleaner);
        String parsedHtml = htmlParser.getText(threadCommunication.getFromPageQueue());
        List outputRow = new ArrayList();
        outputRow.add(parsedHtml);
        finalOutputContainer.addToFinalOutputContainer(outputRow);

    }
}
