package org.Webgatherer.WorkflowExample.Workflows.Base;

import com.google.inject.Injector;

import java.util.Map;

/**
 * @author Rick Dane
 */
public abstract class WorkflowBase {

    protected Injector injector;

    protected WorkflowBase(Injector injector) {
        this.injector = injector;
    }

    public abstract void runWorkflow(Map<String, Object> workflowParams);

}
