package org.Webgatherer.WorkflowExample.Workflows.Base.Common;

import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import org.Webgatherer.Common.Properties.PropertiesContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
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
    protected Properties properties;

    protected WorkflowBase(Injector injector) {
        this.injector = injector;
        htmlCleanerProvider = injector.getProvider(HtmlCleaner.class);
        htmlCleaner = htmlCleanerProvider.get();
        PropertiesContainer propertiesContainer = injector.getInstance(PropertiesContainer.class);
        properties = propertiesContainer.getProperties("WorkflowExample");

    }


    public abstract void runWorkflow(Map<String, Object> workflowParams);

    protected void runSubWorkflow(Class clazz) {

    }

    /**
     * Leave blank, its not specifically needed so we don't make it abstract but leave the option for a workflow to implement
     * a destroy method, if needed
     */
    public void destroyCleanly() {

    }

}
