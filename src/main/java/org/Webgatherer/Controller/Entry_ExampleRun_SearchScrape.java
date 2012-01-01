package org.Webgatherer.Controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.Webgatherer.Controller.Component.ControllerFlow;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.DependencyInjection.DependencyBindingModule;
import org.Webgatherer.WorkflowExample.DataHolders.ContainerBase;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Rick Dane
 */
public class Entry_ExampleRun_SearchScrape {

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new DependencyBindingModule());

        ControllerFlow wfContrl = injector.getInstance(ControllerFlow.class);
        FinalOutputContainer finalOutputContainer = launchWebGathererThread(injector, wfContrl, "org.Webgatherer.WorkflowExample.Workflows.Implementations.WebGatherer.Workflow_WebSearch1", "org.Webgatherer.WorkflowExample.Workflows.Implementations.DataInterpetor.Workflow_DataInterpretor_SearchResultsScrape");

        persistResults(finalOutputContainer);
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

    private static void persistResults(FinalOutputContainer finalOutputContainer) {
        int THREAD_SLEEP = 2000;
        int LIST_FIRST_ITEM = 0;
        int killCount = 5;
        int countKilledSoFar = 0;

        while (true) {
            Map<String, ContainerBase> outputMap = null;
            try {
                outputMap = finalOutputContainer.removeFromFinalOutputContainer();
            } catch (Exception e) {

                try {
                    Thread.sleep(THREAD_SLEEP);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                continue;
            }
            if (outputMap == null || outputMap.isEmpty()) {
                try {
                    Thread.sleep(THREAD_SLEEP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            ContainerBase outputContainer = null;
            for (Map.Entry<String, ContainerBase> entries : outputMap.entrySet()) {
                String key = entries.getKey();
                outputContainer = outputMap.get(key);

                //this is just for this case where each map only has 1 entry
                countKilledSoFar++;
                break;
            }

            LinkedList<String> list = outputContainer.getEntries();

            System.out.println("\n " + outputContainer.getIdentifier() + ": \n");
            for (String curStr : list) {
                System.out.println(curStr + "\n");
            }
            if (countKilledSoFar == killCount) {
                break;
            }
        }
    }

    private static Queue testLoadPages() {

        Queue<String[]> pageQueue = new ConcurrentLinkedQueue<String[]>();

        String numPages = "7";

        String[] site1 = {"livermore", null, null, null, "livermore,+ca+software+company", numPages, ""};
        String[] site3 = {"fremont", null, null, null, "fremont,+ca+software+company", numPages, ""};
        String[] site4 = {"milpitas", null, null, null, "milpitas,+ca+software+company", numPages, ""};
        String[] site5 = {"emeryville", null, null, null, "emeryville,+ca+software+company", numPages, ""};
        String[] site6 = {"pleasanton", null, null, null, "pleasanton,+ca+software+company", numPages, ""};

        pageQueue.add(site1);
        pageQueue.add(site3);
        pageQueue.add(site4);
        pageQueue.add(site5);
        pageQueue.add(site6);

        return pageQueue;
    }

}
