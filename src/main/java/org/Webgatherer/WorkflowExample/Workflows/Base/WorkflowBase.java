package org.Webgatherer.WorkflowExample.Workflows.Base;

import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import org.htmlcleaner.HtmlCleaner;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author Rick Dane
 */
public abstract class WorkflowBase {

    protected Injector injector;
    protected Provider<HtmlCleaner> htmlCleanerProvider;
    protected HtmlCleaner htmlCleaner;

    protected WorkflowBase(Injector injector) {
        this.injector = injector;
        htmlCleanerProvider = injector.getProvider(HtmlCleaner.class);
        htmlCleaner = htmlCleanerProvider.get();
    }

    public abstract void runWorkflow(Map<String, Object> workflowParams);

    protected void runSubWorkflow (Class clazz) {

    }

}
