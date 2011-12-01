package org.Webgatherer.CoreEngine.Workflow;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Meant for using workflows that are part of a separate package, such as one that is included via a jar file (for loose coupling)
 *
 * @author Rick Dane
 */
public class WorkflowWrapperImpl_External implements WorkflowWrapper {

    private final String EXTERNAL_WORKFLOW_PROVIDER = "org.Webgatherer.WorkflowExample.Provider.WorkflowProvider";
    private final String EXTERNAL_WORKFLOW_PROVIDER_METHOD = "runWorfklow";
    private final String EXTERNAL_WORKFLOW_DESTROY_METHOD = "destroyWorkflowCleanly";
    private final String CLASSPATH = "";
    private Object externalWorkflowProvider;

    public WorkflowWrapperImpl_External() {
        initiateProvider();
    }

    public boolean runWorfklow(String classPath, Map<String, Object> workflowParams) {
        Method method = null;
        Class[] parameter = new Class[2];
        parameter[0] = Map.class;
        parameter[1] = String.class;
        try {
            method = externalWorkflowProvider.getClass().getDeclaredMethod(EXTERNAL_WORKFLOW_PROVIDER_METHOD, parameter);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            Object[] args = {workflowParams, classPath};
            if (method != null) {
                method.invoke(externalWorkflowProvider, args);
            }
        } catch (Exception e) {
            System.out.println("Reflection failed - error during execution in WorkflowWrapperImpl_External");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean cleanDestroyWorkflow(String classPath) {
        Method method = null;
        Class[] parameter = new Class[1];
        parameter[0] = String.class;
        try {
            method = externalWorkflowProvider.getClass().getDeclaredMethod(EXTERNAL_WORKFLOW_DESTROY_METHOD, parameter);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
             Object[] args = {classPath};
            if (method != null) {
                method.invoke(externalWorkflowProvider, args);
            }
        } catch (Exception e) {
            System.out.println("Reflection failed - error during attempt to destroy workflow");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void initiateProvider() {

        try {
            externalWorkflowProvider = Class.forName(EXTERNAL_WORKFLOW_PROVIDER).newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
