package org.Webgatherer.Core.Threadable.WebGather;

import org.Webgatherer.Core.Threadable.Base.BaseWebThread;
import org.Webgatherer.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.Core.ThreadCommunication.ThreadCommunication;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;

/**
 * @author Rick Dane
 */
public interface WebGather extends BaseWebThread {

    public void runQueue();

    public void retrievePageFromUrl(String string);

    public void configure(WebDriver driver, Wait<WebDriver> wait, ThreadCommunication threadCommunication,String workflowId, FinalOutputContainer finalOutputContainer);


}
