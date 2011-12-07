package org.Webgatherer.Controller;

import org.Webgatherer.CoreEngine.Core.Threadable.DataInterpreatation.DataInterpretor;
import org.Webgatherer.CoreEngine.Core.Threadable.WebGather.WebGather;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Workflow.WorkflowWrapper;
import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;

import java.util.*;

/**
 * @author Rick Dane
 */
public class WorkflowControllerImpl_1 extends Thread implements ControllerFlow {

    private Map<String, ThreadCommunication> threadTracker = new HashMap<String, ThreadCommunication>();
    private LinkedList<String> threadTrackerOrder = new LinkedList<String>();
    private List<String> workflowList;
    private Provider<ThreadCommunication> threadCommunicationProvider;
    private WebDriverFactory webDriverFactory;
    private Provider<DataInterpretor> dataInterpretorProvider;
    private FinalOutputContainer finalOutputContainer;
    private WorkflowWrapper workflowWrapper;
    private Provider<WebGather> webGatherProvider;
    private Map<String, Object> parameterMap;

    private final int MAX_SLEEPS = 3000;
    private int sleepCount = 0;
    private final int SLEEP_LENGTH = 250;

    @Inject
    public WorkflowControllerImpl_1(Provider<ThreadCommunication> threadCommunicationProvider, WorkflowWrapper workflowWrapper, WebDriverFactory webDriverFactory, Provider<WebGather> webGatherProvider,
                                    Provider<DataInterpretor> dataInterpretorProvider) {
        this.threadCommunicationProvider = threadCommunicationProvider;
        this.webDriverFactory = webDriverFactory;
        this.dataInterpretorProvider = dataInterpretorProvider;
        this.workflowWrapper = workflowWrapper;
        this.webGatherProvider = webGatherProvider;
    }

    public void configure(FinalOutputContainer finalOutputContainer, List<String> workflowList, Map<String, Object> parameterMap) {
        this.finalOutputContainer = finalOutputContainer;
        this.parameterMap = parameterMap;
        this.workflowList = workflowList;
    }

    public void run() {

        launchScraperThread("googleScrape", parameterMap, workflowList.get(0), 1, true);
        launchDataProcessorThread("dataProcess1", parameterMap, workflowList.get(1), 1);

        autoRunFlow();
    }

    /**
     * continually runs to control the flow of first thread to second, and so on, passing along data, or sending back
     */
    private void autoRunFlow() {

        int sizeThreadTrackMin1 = threadTrackerOrder.size() - 1;
        while (sleepCount < MAX_SLEEPS) {
            int i = 0;
            int emptyCount = 0;

            for (String curKey : threadTrackerOrder) {
                ThreadCommunication curThreadComm = threadTracker.get(curKey);

                while (!curThreadComm.isOutputDataHolderEmpty() || !curThreadComm.isSendbackDataHolderEmpty()) {
                    sleepCount = 0;

                    if (i != sizeThreadTrackMin1 && !curThreadComm.isOutputDataHolderEmpty()) {
                        String[] curStr = curThreadComm.getFromOutputDataHolder();

                        ThreadCommunication nextThreadComm = threadTracker.get(threadTrackerOrder.get(i + 1));
                        nextThreadComm.addToPageQueue(curStr);
                        if (curThreadComm.isWebGathererThreadFinished()) {
                            nextThreadComm.setIsWebGathererThreadFinished(true);
                        }
                    }
                    if (i != 0 && !curThreadComm.isSendbackDataHolderEmpty()) {
                        String[] curStr = curThreadComm.getFromSendbackDataHolder();
                        ThreadCommunication prevThreadComm = threadTracker.get(threadTrackerOrder.get(i - 1));
                        prevThreadComm.addToPageQueue(curStr);
                    }
                }

                if (i == sizeThreadTrackMin1) {
                    i = 0;

                } else {
                    i++;
                }

                if (curThreadComm.isOutputDataHolderEmpty() && curThreadComm.isSendbackDataHolderEmpty()) {
                    emptyCount++;
                }

                if (emptyCount >= sizeThreadTrackMin1) {
                    try {
                        Thread.sleep(SLEEP_LENGTH);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sleepCount++;
                }
            }
        }
        System.out.println("AutoRun Worfklow - Successfully Destroyed");
    }


    public int launchScraperThread(String threadName, Map<String, Object> parameterMap, String workflowId, int crawlerDelay, boolean setPageQueue) {
        if (threadTracker.containsKey(threadName)) {
            System.out.println("Thread already exists with that name, new thread was not created");
            return -1;
        }
        ThreadCommunication threadCommunication = threadLaunchPrepare(threadName);
        if (setPageQueue) {
            threadCommunication.setPageQueue((Queue) parameterMap.get("pageQueue"));
        }

        WebDriver driver = webDriverFactory.createNewWebDriver();
        Wait<WebDriver> wait = webDriverFactory.createWebDriverWait(driver, crawlerDelay);

        WebGather webGather = webGatherProvider.get();
        webGather.configure(driver, wait, threadCommunication, workflowId, finalOutputContainer);
        webGather.start();

        return 0;
    }


    public int launchDataProcessorThread(String threadName, Map<String, Object> parameterMap, String workflowId, int crawlerDelay) {
        if (threadTracker.containsKey(threadName)) {
            System.out.println("Thread already exists with that name, new thread was not created");
            return -1;
        }
        ThreadCommunication threadCommunication = threadLaunchPrepare(threadName);

        DataInterpretor dataInterpretor = dataInterpretorProvider.get();
        dataInterpretor.configure(threadCommunication, workflowId, finalOutputContainer);
        dataInterpretor.start();

        return 0;
    }

    private ThreadCommunication threadLaunchPrepare(String threadName) {
        ThreadCommunication threadCommunication = threadCommunicationProvider.get();

        setThreadInTracker(threadName, threadCommunication);
        return threadCommunication;
    }


    private void setThreadInTracker(String key, ThreadCommunication threadCommunication) {
        synchronized (threadTracker) {
            threadTracker.put(key, threadCommunication);
            threadTrackerOrder.add(key);
        }
    }

}
