package org.Webgatherer.Controller;

import org.Webgatherer.DependencyInjection.DependencyBindingModule;
import org.Webgatherer.Core.ThreadCommunication.FinalOutputContainer;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.*;


/**
 * @author Rick Dane
 */
public class main {

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new DependencyBindingModule());

        ControllerFlow wfContrl = injector.getInstance(ControllerFlow.class);
        FinalOutputContainer finalOutputContainer = launchWebGathererThread(injector, wfContrl,"org.WorkflowExample.Workflows.Workflow_WebGather_1","org.WorkflowExample.Workflows.Workflow_DataInterpretor_1");

        testPrintResults(finalOutputContainer);
    }

    private static FinalOutputContainer launchWebGathererThread(Injector injector, ControllerFlow wfContrl, String workflow2, String workflow3) {
        List worfklowList = new ArrayList();
        worfklowList.add(workflow2);
        worfklowList.add(workflow3);

        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("pageQueue", testLoadPages());

        FinalOutputContainer finalOutputContainer = injector.getInstance(FinalOutputContainer.class);

        wfContrl.configure(finalOutputContainer, worfklowList, parameterMap);
        wfContrl.start();
        return finalOutputContainer;
    }

    private static void testPrintResults(FinalOutputContainer finalOutputContainer) {
        int THREAD_SLEEP = 1000;
        int LIST_FIRST_ITEM = 0;

        while (true) {
            List outputList;
            try {
                outputList = (List) finalOutputContainer.removeFromFinalOutputContainer();
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
            }
        }
    }

    private static Queue testLoadPages() {

        Queue<String> pageQueue = new LinkedList<String>();

        pageQueue.add("http://espn.com");
        pageQueue.add("http://reddit.com");
        pageQueue.add("http://tumblr.com");
        return pageQueue;
    }

}
