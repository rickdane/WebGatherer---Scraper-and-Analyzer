package org.Webgatherer.Workflow;

import java.util.Map;

/**
 * @author Rick Dane
 */
public interface WorkflowWrapper {

    public boolean runWorfklow (String processName, Map<String, Object>  workflowParams);
}
