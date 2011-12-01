package org.Webgatherer.CoreEngine.Core.Threadable.DataInterpreatation;

import org.Webgatherer.CoreEngine.Core.Threadable.Base.BaseWebThreadImpl;
import org.Webgatherer.CoreEngine.Workflow.WorkflowWrapper;
import com.google.inject.Inject;
import org.apache.commons.net.nntp.Threadable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rick Dane
 */
public class DataInterpretorImpl extends BaseWebThreadImpl implements DataInterpretor {

    @Inject
    public DataInterpretorImpl(WorkflowWrapper workflowWrapper) {
        this.workflowWrapper = workflowWrapper;
    }

    public void run() {
        runQueue();
    }

    /**
     * this is just for testing, it probably doesn't make sense to override this method since its setup to use a workflow already
     */
    @Override
    public void runQueue() {

        int i = 1;
        while (threadCommunication.shouldBeRunning()) {

            if (threadCommunication.isPageQueueEmpty()) {
                if (determineWhetherBreakLoop()) {
                    break;
                }

            } else {

                Map<String, Object> workflowParams = new HashMap<String, Object>();
                workflowParams.put("dataInterpretor", this);
                workflowParams.put("threadCommunication", threadCommunication);
                workflowParams.put("finalOutputContainer", finalOutputContainer);
                workflowWrapper.runWorfklow(workflowId, workflowParams);

                if (i == threadCommunication.getCheckQuitInterval() || threadCommunication.isPageQueueEmpty()) {
                    if (i == threadCommunication.getCheckQuitInterval()) {
                        i = 1;
                    }
                    synchronized (threadCommunication.shouldBeRunning()) {
                        if (!threadCommunication.shouldBeRunning().booleanValue()) {
                            break;
                        }
                    }
                    if (threadCommunication.isPageQueueEmpty()) {
                        if (determineWhetherBreakLoop()) {
                            break;
                        }

                    }
                } else {
                    i++;
                }
            }
        }

        //the "main loop" for this thread has finished, so we need to run the destroy method on the workflow as it may have state data that needs to be released
        workflowWrapper.cleanDestroyWorkflow(workflowId);
    }

    private boolean determineWhetherBreakLoop() {

        if (emptyLoopCycles > maxEmptyLoopCycles) {
            return true;
        } else {

            emptyLoopCycles++;

            try {
                Thread.sleep(THREAD_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean isDummy() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String messageThreadId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String[] messageThreadReferences() {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String simplifiedSubject() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean subjectIsReply() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setChild(Threadable threadable) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setNext(Threadable threadable) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Threadable makeDummy() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
