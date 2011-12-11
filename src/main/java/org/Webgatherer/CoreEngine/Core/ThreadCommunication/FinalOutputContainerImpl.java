package org.Webgatherer.CoreEngine.Core.ThreadCommunication;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.Webgatherer.WorkflowExample.DataHolders.ContainerBase;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Rick Dane
 */
public class FinalOutputContainerImpl implements FinalOutputContainer {

    private Queue<Map<String, ContainerBase>> finalOutputContainer = new ConcurrentLinkedQueue<Map<String, ContainerBase>>();

    public void addToFinalOutputContainer(String identifier, ContainerBase cb) {
        Map<String, ContainerBase> map = new HashMap<String, ContainerBase>();
        map.put(identifier, cb);
        finalOutputContainer.add(map);

    }

    public Map<String, ContainerBase> removeFromFinalOutputContainer() {
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
