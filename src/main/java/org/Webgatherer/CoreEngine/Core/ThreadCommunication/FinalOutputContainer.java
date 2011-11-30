package org.Webgatherer.CoreEngine.Core.ThreadCommunication;

import org.Webgatherer.WorkflowExample.DataHolders.ContainerBase;

import java.util.List;
import java.util.Map;

/**
 * @author Rick Dane
 */
public interface FinalOutputContainer {

    public void addToFinalOutputContainer (String identifier, ContainerBase cb);

    public Map<String,ContainerBase> removeFromFinalOutputContainer ();

}
