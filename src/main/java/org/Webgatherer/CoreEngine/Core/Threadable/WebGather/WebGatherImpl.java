package org.Webgatherer.CoreEngine.Core.Threadable.WebGather;

import org.Webgatherer.CoreEngine.Core.Threadable.Base.BaseWebThreadImpl;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Workflow.WorkflowWrapper;
import com.google.inject.Inject;
import org.apache.commons.net.nntp.Threadable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;

import java.util.*;

/**
 * @author Rick Dane
 */
public class WebGatherImpl extends BaseWebThreadImpl implements WebGather {
    private WebDriver driver;
    private Wait<WebDriver> wait;

    @Inject
    public WebGatherImpl(WorkflowWrapper workflowWrapper) {
        this.workflowWrapper = workflowWrapper;
    }

    public void configure(WebDriver driver, Wait<WebDriver> wait, ThreadCommunication threadCommunication, String workflowId, FinalOutputContainer finalOutputContainer) {
        super.configure(threadCommunication, workflowId, finalOutputContainer);
        this.driver = driver;
        this.wait = wait;
    }


    public void run() {
        runQueue();
    }

    public void retrievePageFromUrl(String[] entry) {
        driver.get(entry[1]);
        entry[3] = driver.getPageSource();
        threadCommunication.addToOutputDataHolder(entry);
    }

    public void runQueue() {

        int i = 1;
        int selfTerminate = 1;
        while (!threadCommunication.isPageQueueEmpty()) {

            Map<String, Object> workflowParams = new HashMap<String, Object>();
            workflowParams.put("webGather", this);
            workflowParams.put("threadCommunication", threadCommunication);
            workflowParams.put("finalOutputContainer", finalOutputContainer);
            workflowWrapper.runWorfklow(workflowId, workflowParams);


            if (i == threadCommunication.getCheckQuitInterval() || threadCommunication.isPageQueueEmpty()) {
                if (i == threadCommunication.getCheckQuitInterval()) {
                    i = 1;
                }
                synchronized (threadCommunication.shouldBeRunning()) {
                    if (!threadCommunication.shouldBeRunning()) {
                        break;
                    }
                }
                if (threadCommunication.isPageQueueEmpty()) {
                    if (selfTerminate > SELF_TERMINATE_COUNT) {
                        break;
                    }
                    try {
                        Thread.sleep(THREAD_SLEEP_LONGER);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    selfTerminate++;
                }
            } else {
                i++;
            }
        }
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

