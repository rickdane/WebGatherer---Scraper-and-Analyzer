package org.Webgatherer.WorkflowExample.DataHolders;

import org.Webgatherer.WorkflowExample.Status.StatusIndicator;
import org.ardverk.collection.PatriciaTrie;
import org.ardverk.collection.StringKeyAnalyzer;
import org.ardverk.collection.Trie;

import java.util.*;

/**
 * @author Rick Dane
 */
public class DataHolderImpl implements DataHolder {

    private Trie<String, String> emailAddresses = new PatriciaTrie<String, String>(StringKeyAnalyzer.INSTANCE);
    private Map<String, ContainerBase> containerHolder = new HashMap<String, ContainerBase>();
    private Queue<String> finishedContainerKeys = new LinkedList<String>();

    private int maxEmailAddresses;
    private Map<String, Integer> contentTypesMap = new HashMap<String, Integer>();


    public StatusIndicator addEmailAddress(String email) {
        emailAddresses.put(email, null);
        if (emailAddresses.selectKey(email) != null) {
            return StatusIndicator.ALREADYEXISTS;
        }
        emailAddresses.put(email, null);
        return StatusIndicator.SUCCESS;
    }

    public boolean isFinishedContainerQueueEmpty() {
        return finishedContainerKeys.isEmpty();
    }

    public ContainerBase pullFromFinishedContainerQueue() {
        String finishedContainerKey = null;
        if (!finishedContainerKeys.isEmpty()) {
            finishedContainerKey = finishedContainerKeys.remove();

        }
        if (finishedContainerKey != null) {
            return containerHolder.get(finishedContainerKey);
        }
        return null;
    }

    /**
     * meant to be called before getContainerByIdentifier so calling code knows if it should even bother trying to retrieve object
     *
     * @param identifier
     * @return
     */
    public StatusIndicator checkIfContainerAvailable(String identifier) {
        ContainerBase cb = containerHolder.get(identifier);
        if (cb == null) {
            return StatusIndicator.DOESNOTEXIST;
        }
        if (cb.isLocked()) {
            return StatusIndicator.NOTAVAILABLE;
        }
        return StatusIndicator.AVAILABLE;
    }

    /**
     * returns the instance of Container that matches the key, if none exists or the instance is locked, it returns null
     *
     * @param identifier
     * @return
     */
    public ContainerBase getContainerByIdentifier(String identifier) {
        ContainerBase cb = containerHolder.get(identifier);
        if (cb == null || cb.isLocked()) {
            return null;
        }
        return cb;
    }

    public Trie<String, String> getEmailAddresses() {
        return emailAddresses;
    }

    public StatusIndicator createContainer(String identifier, int maxEntries, int maxAttempts) {
        if (containerHolder.containsKey(identifier)) {
            return StatusIndicator.ALREADYEXISTS;
        }
        //TODO, convert this to DI
        ContainerBase cb = new ContainerBase(identifier, maxEntries, maxAttempts);
        containerHolder.put(identifier, cb);
        return StatusIndicator.SUCCESS;
    }

    public StatusIndicator addEntryToContainer(String identifier, String entry) {
        ContainerBase cb = containerHolder.get(identifier);
        if (cb == null) {
            return StatusIndicator.DOESNOTEXIST;
        }
        if (cb.isLocked()) {
            return StatusIndicator.NOTAVAILABLE;
        }
        cb.addContent(entry);
        StatusIndicator status = cb.incrementAttempts();
        if (status == StatusIndicator.JUSTUNLOCKED) {
            finishedContainerKeys.add(identifier);
        }
        return StatusIndicator.SUCCESS;
    }

    public void incrementContainerAllowedAttempts(String identifier) {
        ContainerBase cb = containerHolder.get(identifier);
        StatusIndicator status = cb.incrementAttempts();
        if (status == StatusIndicator.JUSTUNLOCKED) {
            finishedContainerKeys.add(identifier);
        }
    }

}
