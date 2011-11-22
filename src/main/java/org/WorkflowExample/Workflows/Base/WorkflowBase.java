package org.WorkflowExample.Workflows.Base;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.Webgatherer.DependencyInjection.DependencyBindingModule;

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
