package org.Webgatherer.WorkflowExample.DataHolders;

import org.Webgatherer.WorkflowExample.Status.StatusIndicator;
import org.ardverk.collection.Trie;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * @author Rick Dane
 */
public interface DataHolder {

    public StatusIndicator addEmailAddress(String email);

    public StatusIndicator checkIfContainerAvailable(String identifier);

    void destroyRetrieveFinalData();

    public ContainerBase getContainerByIdentifier(String identifier);

    public Trie<String, String> getEmailAddresses();

    public StatusIndicator createContainer(String identifier, int maxEntries, int maxAttempts);

    public StatusIndicator addEntryToContainer(String identifier, String entry);

    public void incrementContainerAllowedAttempts(String identifier);

    public boolean isFinishedContainerQueueEmpty();

    public ContainerBase pullFromFinishedContainerQueue();

}
