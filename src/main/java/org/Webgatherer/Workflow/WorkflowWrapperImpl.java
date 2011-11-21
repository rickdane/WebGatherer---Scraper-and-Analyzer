package org.Webgatherer.Workflow;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import java.io.FileInputStream;
import java.util.Map;

/**
 * @author Rick Dane
 */
public class WorkflowWrapperImpl implements WorkflowWrapper {

    /**
     * Creates, runs and disposes of workflow session, returns false if the workflow did not run properly
     *
     * @return
     */
    public boolean runWorfklow(String processName, Map<String, Object> parameterMap) {
        StatefulKnowledgeSession worfklowSess = WorkflowWrapperImpl.provideStatefulknowledgeSession("/home/user/Dropbox/Rick/WebGatherer/src/IntegrationThreadTest/java/rules/level1/" + processName + ".bpmn");
        if (worfklowSess != null) {
            worfklowSess.startProcess(processName, parameterMap);
            worfklowSess.fireAllRules();
            worfklowSess.dispose();
        } else {
            System.out.println("Error during execution of workflow " + processName);
            return false;
        }
        return true;
    }

    private static StatefulKnowledgeSession provideStatefulknowledgeSession(String workflowFilePath) {
        KnowledgeBase knowledgeBase = null;
        try {
            knowledgeBase = readKnowledgeBase(workflowFilePath);
        } catch (Exception e) {
            return null;
        }
        StatefulKnowledgeSession ksession = knowledgeBase.newStatefulKnowledgeSession();
        SimpleWorkItemHandler handler = new SimpleWorkItemHandler();

        ksession.getWorkItemManager().registerWorkItemHandler("Log", handler);
        return ksession;
    }

    private static KnowledgeBase readKnowledgeBase(String workflowFilePath) throws Exception {
        KnowledgeBase knowledgeBase = null;

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        FileInputStream fis = new FileInputStream(workflowFilePath);
        kbuilder.add(ResourceFactory.newInputStreamResource(fis), ResourceType.BPMN2);
        knowledgeBase = kbuilder.newKnowledgeBase();
        return knowledgeBase;
    }
}
