package org.Webgatherer.WorkflowExample.Provider;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.Webgatherer.WorkflowExample.DependencyInjection.DependencyBindingModule;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rick Dane
 */
public class WorkflowProvider {
    private Injector injector;
    private final String WORKFLOW_METHOD = "runWorkflow";
    private final String WORKFLOW_DESTROY_METHOD = "destroyCleanly";

    private Map<String, Object> workflowObjectInstances = new HashMap<String, Object>();

    public WorkflowProvider() {
        injector = Guice.createInjector(new DependencyBindingModule());

    }

    /**
     * This is a wrapper since we want to use Inversion of Control and can't do that directly from reflection
     *
     * @param workflowParams
     */
    public void runWorfklow(Map<String, Object> workflowParams, String classPath) {

        Object workflowObject = workflowObjectInstances.get(classPath);
        if (workflowObject == null) {
            workflowObject = initiateObject(classPath);
        }

        Method method = null;
        Class[] parameter = new Class[1];
        parameter[0] = Map.class;
        try {
            method = workflowObject.getClass().getDeclaredMethod(WORKFLOW_METHOD, parameter);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            method.invoke(workflowObject, workflowParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroyWorkflowCleanly(String classPath) {
        Object workflowObject = workflowObjectInstances.get(classPath);
        if (workflowObject == null) {
            workflowObject = initiateObject(classPath);
        }

        Method method = null;
        try {
            method = workflowObject.getClass().getDeclaredMethod(WORKFLOW_DESTROY_METHOD);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            method.invoke(workflowObject);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Reflection failed during destroy of Workflow");
        }
    }

    private Object initiateObject(String classPath) {

        Object object = null;
        try {
            Class[] constructorArgs = new Class[]{Injector.class};
            Object[] args = {injector};
            Constructor constructor = Class.forName(classPath).getConstructor(constructorArgs);
            object = constructor.newInstance(args);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (object != null) {
            workflowObjectInstances.put(classPath, object);
        }
        return object;
    }
}

