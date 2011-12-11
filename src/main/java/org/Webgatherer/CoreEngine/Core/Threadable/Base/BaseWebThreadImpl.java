package org.Webgatherer.CoreEngine.Core.Threadable.Base;

import com.google.inject.Inject;
import org.Webgatherer.Common.Properties.PropertiesContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Workflow.WorkflowWrapper;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;

/**
 * @author Rick Dane
 */
public abstract class BaseWebThreadImpl extends Thread implements BaseWebThread {

    protected ThreadCommunication threadCommunication;
    protected List<String> workflowList;
    protected FinalOutputContainer finalOutputContainer;
    protected String workflowId;
    protected WorkflowWrapper workflowWrapper;

    protected int threadSleep;
    protected int emptyLoopCycles = 0;
    protected int maxEmptyLoopCycles;

    protected Properties properties;

    @Inject
    public BaseWebThreadImpl(PropertiesContainer propertiesContainer) {
        properties = propertiesContainer.getProperties("CoreEngine");
        threadSleep = Integer.parseInt(properties.getProperty("threadSleep"));
        maxEmptyLoopCycles = Integer.parseInt(properties.getProperty("maxEmptyLoopCycles"));
    }


    public void configure(ThreadCommunication threadCommunication, String workflowId, FinalOutputContainer finalOutputContainer) {
        this.threadCommunication = threadCommunication;
        this.workflowId = workflowId;
        this.finalOutputContainer = finalOutputContainer;

    }

    abstract public void run();

    public void setWorkflowList(List<String> workflowList) {
        this.workflowList = workflowList;
    }

    public void setThreadCommunication(ThreadCommunication threadCommunication) {
        this.threadCommunication = threadCommunication;
    }

    public abstract void runQueue();
}
