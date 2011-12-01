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


//    private void one_DataInterpretor(Map<String, Object> workflowParams) {
//        DataInterpretor dataInterpretor = (DataInterpretor) workflowParams.get("dataInterpretor");
//        ThreadCommunication threadCommunication = (ThreadCommunication) workflowParams.get("threadCommunication");
//        FinalOutputContainer finalOutputContainer = (FinalOutputContainer) workflowParams.get("finalOutputContainer");
//
//        HtmlCleaner htmlCleaner = htmlCleanerProvider.get();
//
//        HtmlParser htmlParser = new HtmlParserImpl(htmlCleaner);
//        String parsedHtml = htmlParser.getText(threadCommunication.getFromPageQueue());
//        List outputRow = new ArrayList();
//        outputRow.add(parsedHtml);
//        finalOutputContainer.addToFinalOutputContainer(outputRow);
//
//    }
//
//
//    private void default_DataInterpretor(Map<String, Object> workflowParams) {
//        DataInterpretor dataInterpretor = (DataInterpretor) workflowParams.get("dataInterpretor");
//        ThreadCommunication threadCommunication = (ThreadCommunication) workflowParams.get("threadCommunication");
//        FinalOutputContainer finalOutputContainer = (FinalOutputContainer) workflowParams.get("finalOutputContainer");
//
//        //HtmlParserImpl htmlParser = new HtmlParserImpl();
//        //String parsedHtml = htmlParser.testParse(threadCommunication.getFromPageQueue());
//        threadCommunication.addToOutputDataHolder("http://bing.com");
//        threadCommunication.addToSendbackDataHolder("http://yahoo.com");
//
//    }


//    private void default_ThreadLaunch(Map<String, Object> workflowParams) {
//        ControllerFlow workflowController = (ControllerFlow) workflowParams.get("ControllerFlow");
//        List<String> workflowList = (List<String>) workflowParams.get("workflowList");
//        Map<String, Object> threadParameterMap = (Map<String, Object>) workflowParams.get("parameterMap");
//
//        workflowController.launchScraperThread("googleScrape", threadParameterMap, workflowList.get(1), 1, true);
//        workflowController.launchDataProcessorThread("dataProcess1", threadParameterMap, workflowList.get(2), 1);
//    }

//
//    private void default_WebScrape(Map<String, Object> workflowParams) {
//        WebGather webGather = (WebGather) workflowParams.get("webGather");
//        ThreadCommunication threadCommunication = (ThreadCommunication) workflowParams.get("threadCommunication");
//        FinalOutputContainer finalOutputContainer = (FinalOutputContainer) workflowParams.get("finalOutputContainer");
//
//        String curItem = threadCommunication.getFromPageQueue();
//
//        webGather.retrievePageFromUrl(curItem);
//
//    }
}
