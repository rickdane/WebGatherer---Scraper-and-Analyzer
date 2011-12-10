package org.Webgatherer.WorkflowExample.Workflows.Implementations.WebGatherer;

import com.google.inject.Injector;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Core.Threadable.WebGather.WebGather;
import org.Webgatherer.WorkflowExample.Workflows.Base.Common.WorkflowBase;

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

        webGather.retrievePageFromUrl(EnumUrlRetrieveOptions.HTMLPAGE.ordinal());
    }
}
