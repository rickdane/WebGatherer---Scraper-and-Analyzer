package org.Webgatherer.CoreEngine.Workflow;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.htmlcleaner.HtmlCleaner;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Rick Dane
 */
public class WorfkflowWrapperImpl_Reflection {

    private Provider <HtmlCleaner> htmlCleanerProvider;

    @Inject
    public WorfkflowWrapperImpl_Reflection(Provider<HtmlCleaner> htmlCleanerProvider) {
        this.htmlCleanerProvider = htmlCleanerProvider;
    }

    public boolean runWorfklow(String methodName, Map<String, Object> workflowParams) {
        Method method = null;
        Class thisClass;
        Class[] parameter = new Class[1];
        parameter[0] = Map.class;
        try {
            thisClass = Class.forName("org.Webgatherer.CoreEngine.Workflow.WorfkflowWrapperImpl_Reflection");

            method = thisClass.getDeclaredMethod(methodName, parameter);
        } catch (Exception e) {
            System.out.println("Reflection failed - method '" + methodName + "' does not exist");
            return false;
        }

        try {
            method.invoke(this, workflowParams);
        } catch (Exception e) {
            System.out.println("Reflection failed - error during execution of method: '" + methodName + "'");
        }
        return true;
    }

}
