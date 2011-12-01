package org.Webgatherer.CoreEngine.Workflow;

import java.util.Map;

/**
 * @author Rick Dane
 */
public interface WorkflowWrapper {

    public boolean runWorfklow (String processName, Map<String, Object>  workflowParams);

    public boolean cleanDestroyWorkflow(String classPath);
}
