package org.Webgatherer.WorkflowExample.Workflows.Base;

import com.google.inject.Injector;
import com.google.inject.TypeLiteral;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author Rick Dane
 */
public abstract class WorkflowBase {

    protected Injector injector;

    protected WorkflowBase(Injector injector) {
        this.injector = injector;
    }

    public abstract void runWorkflow(Map<String, Object> workflowParams);

    protected void runSubWorkflow (Class clazz) {

    }

}
