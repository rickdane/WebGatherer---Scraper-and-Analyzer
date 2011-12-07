package org.Webgatherer.CoreEngine.Core.Threadable.Base;

import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Workflow.WorkflowWrapper;

import java.util.List;

/**
 * @author Rick Dane
 */
public abstract class BaseWebThreadImpl extends Thread implements BaseWebThread {

    protected ThreadCommunication threadCommunication;
    protected List<String> workflowList;
    protected FinalOutputContainer finalOutputContainer;
    protected String workflowId;
    protected WorkflowWrapper workflowWrapper;

    protected final int THREAD_SLEEP = 4000;
    protected int emptyLoopCycles = 0;
    protected int maxEmptyLoopCycles = 15;


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
