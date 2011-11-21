package org.Webgatherer.Core.Threadable.Base;

import org.Webgatherer.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.Core.ThreadCommunication.ThreadCommunication;
import org.apache.commons.net.nntp.Threadable;

import java.util.List;

/**
 * @author Rick Dane
 */
public interface BaseWebThread extends Threadable {

    public void start ();

    public void configure(ThreadCommunication threadCommunication,String workflowId, FinalOutputContainer finalOutputContainer);

    public abstract void runQueue();

    public void setWorkflowList(List<String> workflowList);

    public void setThreadCommunication(ThreadCommunication threadCommunication);

}
