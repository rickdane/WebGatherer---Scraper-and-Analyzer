package org.Webgatherer.CoreEngine.Core.Threadable.WebGather;

import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunicationBase;
import org.Webgatherer.CoreEngine.Core.Threadable.Base.BaseWebThreadImpl;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Workflow.WorkflowWrapper;
import com.google.inject.Inject;
import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.Webgatherer.WorkflowExample.Workflows.Implementations.WebGatherer.EnumUrlRetrieveOptions;
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
    private WebDriverFactory webDriverFactory;

    @Inject
    public WebGatherImpl(WorkflowWrapper workflowWrapper, WebDriverFactory webDriverFactory) {
        this.workflowWrapper = workflowWrapper;
        this.webDriverFactory = webDriverFactory;
    }

    public void configure(WebDriver driver, Wait<WebDriver> wait, ThreadCommunication threadCommunication, String workflowId, FinalOutputContainer finalOutputContainer) {
        super.configure(threadCommunication, workflowId, finalOutputContainer);
        this.driver = driver;
        this.wait = wait;

    }

    public WebDriver getNewWebDriver() {
        return webDriverFactory.createNewWebDriver();
    }


    public void run() {
        runQueue();
    }

    protected int maxNullEntries = 10;
    protected int cntMaxNullEntries = 0;

    public void retrievePageFromUrl(String[] entry, int retrieveType) {
        if (entry == null) {
            cntMaxNullEntries++;
            if (cntMaxNullEntries >= maxNullEntries) {
                threadCommunication.setIsWebGathererThreadFinished(true);
            }
            return;
        }
        driver.get(entry[ThreadCommunicationBase.PageQueueEntries.BASE_URL.ordinal()]);
        if (retrieveType == EnumUrlRetrieveOptions.HTMLPAGE.ordinal()) {
            entry[ThreadCommunicationBase.PageQueueEntries.SCRAPED_PAGE.ordinal()] = driver.getPageSource();
        }
        if (retrieveType == EnumUrlRetrieveOptions.TEXTPAGE.ordinal()) {
            //TODO change this, its not currently implemented
        }

        threadCommunication.addToOutputDataHolder(entry);
    }

    public WebDriver getDriver() {
        return driver;

    }

    public void runQueue() {

        int i = 1;
        int selfTerminate = 1;
        while (threadCommunication.shouldBeRunning()) {

            Map<String, Object> workflowParams = new HashMap<String, Object>();
            workflowParams.put("webGather", this);
            workflowParams.put("threadCommunication", threadCommunication);
            workflowParams.put("finalOutputContainer", finalOutputContainer);
            workflowWrapper.runWorfklow(workflowId, workflowParams);

            if (threadCommunication.isPageQueueEmpty()) {

                synchronized (threadCommunication.shouldBeRunning()) {
                    if (!threadCommunication.shouldBeRunning()) {
                        break;
                    }
                }

                if (threadCommunication.isPageQueueEmpty()) {
                    if (determineWhetherBreakLoop()) {
                        break;
                    }
                }
            } else {
                emptyLoopCycles = 0;
            }
        }

        System.out.println("Workflow - WebGather Destroyed");
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

