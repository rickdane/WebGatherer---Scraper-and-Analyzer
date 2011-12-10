package org.Webgatherer.CoreEngine.Core.Threadable.WebGather;

import org.Webgatherer.Common.Properties.PropertiesContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunicationBase;
import org.Webgatherer.CoreEngine.Core.Threadable.Base.BaseWebThreadImpl;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Workflow.WorkflowWrapper;
import com.google.inject.Inject;
import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor.TextExtraction;
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
    private TextExtraction textExtraction;

    protected int maxNullEntries = 10;
    protected int cntMaxNullEntries = 0;
    protected int maxMillisecondTimeout = 12000;

    @Inject
    public WebGatherImpl(PropertiesContainer propertiesContainer, WorkflowWrapper workflowWrapper, WebDriverFactory webDriverFactory, TextExtraction textExtraction) {
        super(propertiesContainer);
        this.workflowWrapper = workflowWrapper;
        this.webDriverFactory = webDriverFactory;
        this.textExtraction = textExtraction;

        Properties properties = propertiesContainer.getProperties("CoreEngine");
        maxNullEntries = Integer.parseInt(properties.getProperty("webGather_maxNullEntries"));
        cntMaxNullEntries = Integer.parseInt(properties.getProperty("webGather_cntMaxNullEntries"));
        maxMillisecondTimeout = Integer.parseInt(properties.getProperty("webGather_maxMillisecondTimeout"));

    }

    public void configure(WebDriver driver, Wait<WebDriver> wait, ThreadCommunication threadCommunication, String workflowId, FinalOutputContainer finalOutputContainer) {
        super.configure(threadCommunication, workflowId, finalOutputContainer);
        this.driver = driver;
    }

    public WebDriver getNewWebDriver() {
        return webDriverFactory.createNewWebDriver();
    }


    public void run() {
        runQueue();
    }

    public void retrievePageFromUrl(String[] entry, int retrieveType) {
        if (entry == null) {
            cntMaxNullEntries++;
            if (cntMaxNullEntries >= maxNullEntries) {
                threadCommunication.setIsWebGathererThreadFinished(true);
            }
            return;
        }

        //if domain was marked as slow, don't attempt to load page
        if (slowLoadingIgnoreUrls.contains(entry[ThreadCommunicationBase.PageQueueEntries.KEY.ordinal()])) {
            return;
        }


        Date before = new Date();

        if (!textExtraction.isUrlValid(entry[ThreadCommunicationBase.PageQueueEntries.BASE_URL.ordinal()])) {
            return;
        }

        driver.get(entry[ThreadCommunicationBase.PageQueueEntries.BASE_URL.ordinal()]);
        Date after = new Date();

        if (after.getTime() - before.getTime() > maxMillisecondTimeout) {
            //this is due to a bug in the Selenium Web Driver, it doesn't always respect the timeout setting so we need to
            //check how long the request took and if it was too long then block all future urls from this site since it will make the crawler too slow
            slowLoadingIgnoreUrls.add(entry[ThreadCommunicationBase.PageQueueEntries.KEY.ordinal()]);
            return;
        }

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

