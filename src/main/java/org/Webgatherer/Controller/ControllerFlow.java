package org.Webgatherer.Controller;

import org.Webgatherer.Core.ThreadCommunication.FinalOutputContainer;

import java.util.List;
import java.util.Map;

/**
 * @author Rick Dane
 */
public interface ControllerFlow {

    public void start ();

    public void configure (FinalOutputContainer finalOutputContainer,List<String> workflowList, Map<String, Object> parameterMap);

    public int launchScraperThread(String threadName, Map<String, Object> parameterMap, String workflowId, int crawlerDelay, boolean setPageQueue);

    public int launchDataProcessorThread(String threadName, Map<String, Object> parameterMap, String workflowId, int crawlerDelay);

    public void stop();

}
