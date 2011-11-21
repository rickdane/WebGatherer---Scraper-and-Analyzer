package org.Webgatherer.Core.ThreadCommunication;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Rick Dane
 */
public class FinalOutputContainerImpl implements FinalOutputContainer {

    private volatile Queue<List> finalOutputContainer = new LinkedList<List>();

    public void addToFinalOutputContainer(List addList) {

        finalOutputContainer.add(addList);

    }

    public List removeFromFinalOutputContainer() {
        if (!finalOutputContainer.isEmpty()) {
            try {
                return finalOutputContainer.remove();
            } catch (Exception e) {

            }
        }
        return null;
    }
}
