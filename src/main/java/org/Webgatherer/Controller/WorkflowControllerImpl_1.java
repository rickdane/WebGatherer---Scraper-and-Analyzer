package org.Webgatherer.Controller;

import org.Webgatherer.Core.Threadable.DataInterpreatation.DataInterpretor;
import org.Webgatherer.Core.Threadable.WebGather.WebGather;
import org.Webgatherer.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.Workflow.WorkflowWrapper;
import org.Webgatherer.lib.WebDriverFactory;
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

    @Inject
    public WorkflowControllerImpl_1(Provider<ThreadCommunication> threadCommunicationProvider, WorkflowWrapper workflowWrapper, WebDriverFactory webDriverFactory, Provider<WebGather> webGatherProvider,
                                    Provider<DataInterpretor> dataInterpretorProvider) {
        this.threadCommunicationProvider = threadCommunicationProvider;
        this.webDriverFactory = webDriverFactory;
        this.dataInterpretorProvider = dataInterpretorProvider;
        this.workflowWrapper = workflowWrapper;
        this.webGatherProvider = webGatherProvider;
    }

    public void configure(FinalOutputContainer finalOutputContainer,List<String> workflowList, Map<String, Object> parameterMap) {
        this.finalOutputContainer = finalOutputContainer;
        this.parameterMap = parameterMap;
        this.workflowList = workflowList;
    }

    /**
     * Driven by a workflow, threads are started in a particular, meaningful order and added to the tracker queue, then
     * the data from the first "flows" into the second and so on until all data is processed, the final thread's ThreadCommunication
     * should then hold the final version of all processed data
     *
     */
    public void run() {
        this.workflowList = workflowList;

//        Map<String, Object> workflowParams = new HashMap<String, Object>();
//        workflowParams.put("ControllerFlow", this);
//        workflowParams.put("workflowList",workflowList);
//        workflowParams.put("parameterMap",parameterMap);
//        workflowWrapper.runWorfklow(workflowList.get(0), workflowParams);

        launchScraperThread("googleScrape", parameterMap, workflowList.get(0), 1, true);
        launchDataProcessorThread("dataProcess1",parameterMap, workflowList.get(1), 1);

        autoRunFlow();
    }

    /**
     * continually runs to control the flow of first thread to second, and so on, passing along data, or sending back
     */
    private void autoRunFlow() {

        int sizeThreadTrackMin1 = threadTrackerOrder.size() - 1;
        while (true) {
            int i = 0;
            for (String curKey : threadTrackerOrder) {
                ThreadCommunication curThreadComm = threadTracker.get(curKey);

                int emptyCnt = 0;

                if (curThreadComm.isOutputDataHolderEmpty() && curThreadComm.isSendbackDataHolderEmpty()) {
                    if (emptyCnt == threadTrackerOrder.size() - 1) {
                        break;
                    }
                    if (curThreadComm.isPageQueueEmpty()) {
                        emptyCnt = emptyCnt++;
                    }

                }
                while (!curThreadComm.isOutputDataHolderEmpty() || !curThreadComm.isSendbackDataHolderEmpty()) {

                    if (i != sizeThreadTrackMin1 && !curThreadComm.isOutputDataHolderEmpty()) {
                        String curStr = curThreadComm.getFromOutputDataHolder();
                        ThreadCommunication nextThreadComm = threadTracker.get(threadTrackerOrder.get(i + 1));
                        nextThreadComm.addToPageQueue(curStr);
                    }
                    if (i != 0 && !curThreadComm.isSendbackDataHolderEmpty()) {
                        String curStr = curThreadComm.getFromSendbackDataHolder();
                        ThreadCommunication prevThreadComm = threadTracker.get(threadTrackerOrder.get(i - 1));
                        prevThreadComm.addToPageQueue(curStr);
                    }
                }
                if (i == sizeThreadTrackMin1) {
                    i = 0;

                } else {
                    i++;
                }
            }
        }
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
