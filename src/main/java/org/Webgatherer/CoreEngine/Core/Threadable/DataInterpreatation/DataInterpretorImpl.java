package org.Webgatherer.CoreEngine.Core.Threadable.DataInterpreatation;

import org.Webgatherer.Common.Properties.PropertiesContainer;
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

    protected final int threadSleep;
    protected int emptyLoopCycles = 0;
    protected int maxEmptyLoopCycles;

    @Inject
    public DataInterpretorImpl(PropertiesContainer propertiesContainer, WorkflowWrapper workflowWrapper) {
        super(propertiesContainer);
        this.workflowWrapper = workflowWrapper;
        maxEmptyLoopCycles = Integer.parseInt(properties.getProperty("dataInterpretor_maxEmptyLoopCycles"));
        threadSleep = Integer.parseInt(properties.getProperty("dataInterpretor_threadSleep"));
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
                if (threadCommunication.isWebGathererThreadFinished()) {
                    break;
                }
                if (determineWhetherBreakLoop()) {
                    break;
                }

            } else {
                emptyLoopCycles = 0;
                Map<String, Object> workflowParams = new HashMap<String, Object>();
                workflowParams.put("dataInterpretor", this);
                workflowParams.put("threadCommunication", threadCommunication);
                workflowParams.put("finalOutputContainer", finalOutputContainer);
                workflowWrapper.runWorfklow(workflowId, workflowParams);

                synchronized (threadCommunication.shouldBeRunning()) {
                    if (!threadCommunication.shouldBeRunning().booleanValue()) {
                        break;
                    }
                }
            }
        }

        //the "main loop" for this thread has finished, so we need to run the destroy method on the workflow as it may have state data that needs to be released
        workflowWrapper.cleanDestroyWorkflow(workflowId);
        System.out.println("Data Interpreter Workflow Successfully Destroyed");
    }

    private boolean determineWhetherBreakLoop() {

        if (emptyLoopCycles > maxEmptyLoopCycles) {
            return true;
        } else {

            emptyLoopCycles++;

            try {
                Thread.sleep(threadSleep);
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
