package org.Webgatherer.Core.Threadable.Base;

import org.Webgatherer.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.Workflow.WorkflowWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rick Dane
 */
public abstract class BaseWebThreadImpl extends Thread implements BaseWebThread {

    protected ThreadCommunication threadCommunication;
    protected List<String> workflowList;
    protected FinalOutputContainer finalOutputContainer;
    protected String workflowId;
    protected WorkflowWrapper workflowWrapper;

    protected final int THREAD_SLEEP = 500;
    protected final int THREAD_SLEEP_LONGER = 10000;
    protected final int SELF_TERMINATE_COUNT = 10;


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
