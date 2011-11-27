package org.Webgatherer.CoreEngine.Controller;

import org.Webgatherer.CoreEngine.DependencyInjection.DependencyBindingModule;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.*;


/**
 * @author Rick Dane
 */
public class ExampleRun {

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new DependencyBindingModule());

        ControllerFlow wfContrl = injector.getInstance(ControllerFlow.class);
        FinalOutputContainer finalOutputContainer = launchWebGathererThread(injector, wfContrl, "org.Webgatherer.WorkflowExample.Workflows.Workflow_WebGather_1", "org.Webgatherer.WorkflowExample.Workflows.Workflow_DataInterpretor_1");

        testPrintResults(finalOutputContainer);
    }

    private static FinalOutputContainer launchWebGathererThread(Injector injector, ControllerFlow wfContrl, String workflow2, String workflow3) {
        List<String> workflowlist = new ArrayList<String>();
        workflowlist.add(workflow2);
        workflowlist.add(workflow3);

        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("pageQueue", testLoadPages());

        FinalOutputContainer finalOutputContainer = injector.getInstance(FinalOutputContainer.class);

        wfContrl.configure(finalOutputContainer, workflowlist, parameterMap);
        wfContrl.start();
        return finalOutputContainer;
    }

    private static void testPrintResults(FinalOutputContainer finalOutputContainer) {
        int THREAD_SLEEP = 1000;
        int LIST_FIRST_ITEM = 0;

        while (true) {
            List outputList;
            try {
                outputList = finalOutputContainer.removeFromFinalOutputContainer();
            } catch (Exception e) {
                continue;
            }
            if (outputList == null || outputList.isEmpty()) {
                continue;
            }

            String outputStr = (String) outputList.get(LIST_FIRST_ITEM);
            System.out.println(outputStr);
            try {
                Thread.sleep(THREAD_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static Queue testLoadPages() {

        Queue<String[]> pageQueue = new LinkedList<String[]>();


        String[] site1 = {"espn.com", "http://espn.com"};
        String[] site2 = {"reddit.com", "http://reddit.com"};
        String[] site3 = {"tumblr.com", "http://tumblr.com"};

        pageQueue.add(site1);
        pageQueue.add(site2);
        pageQueue.add(site3);
        return pageQueue;
    }

}
