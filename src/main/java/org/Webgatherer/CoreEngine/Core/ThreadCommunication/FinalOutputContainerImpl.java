package org.Webgatherer.CoreEngine.Core.ThreadCommunication;

import org.Webgatherer.WorkflowExample.DataHolders.ContainerBase;

import java.util.*;

/**
 * @author Rick Dane
 */
public class FinalOutputContainerImpl implements FinalOutputContainer {

    private volatile Queue<Map<String,ContainerBase>> finalOutputContainer = new LinkedList<Map<String,ContainerBase>>();

    public void addToFinalOutputContainer(String identifier, ContainerBase cb) {
        Map <String, ContainerBase> map = new HashMap<String, ContainerBase>();
        map.put(identifier,cb);
        finalOutputContainer.add(map);

    }

    public Map<String,ContainerBase> removeFromFinalOutputContainer() {
        if (!finalOutputContainer.isEmpty()) {
            try {
                return finalOutputContainer.remove();
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
        return null;
    }
}
