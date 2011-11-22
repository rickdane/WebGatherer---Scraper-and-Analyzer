package org.WorkflowExample.Workflows;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.Webgatherer.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.Core.Threadable.WebGather.WebGather;
import org.WorkflowExample.Workflows.Base.WorkflowBase;

import java.util.Map;

/**
 * @author Rick Dane
 */
public class Workflow_WebGather_1 extends WorkflowBase {

    public Workflow_WebGather_1(Injector injector) {
        super(injector);
    }

    public void runWorkflow(Map<String, Object> workflowParams) {

        WebGather webGather = (WebGather) workflowParams.get("webGather");
        ThreadCommunication threadCommunication = (ThreadCommunication) workflowParams.get("threadCommunication");
        FinalOutputContainer finalOutputContainer = (FinalOutputContainer) workflowParams.get("finalOutputContainer");

        String curItem = threadCommunication.getFromPageQueue();

        webGather.retrievePageFromUrl(curItem);
    }
}
