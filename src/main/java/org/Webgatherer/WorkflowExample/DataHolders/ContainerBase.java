package org.Webgatherer.WorkflowExample.DataHolders;

import org.Webgatherer.WorkflowExample.Status.StatusIndicator;

import java.util.LinkedList;

/**
 * This is a data holder container that specifically holds text article / page content. It is not thread-safe and is not meant to be
 * used in a way that requires thread safety. Instead, it stays "locked" until its done being used and only then can it be released
 * to a class other than the one that created it.
 *
 * @author Rick Dane
 */
public class ContainerBase {

    private final String identifier;
    private final int maxEntries;
    private final LinkedList <String> entries;
    private final int maxAttempts;
    private int numberOfAttempts;
    private boolean isUnLocked = false;

    public ContainerBase(String identifier, int maxEntries, int maxAttempts) {
        this.maxEntries = maxEntries;
        this.identifier = identifier;
        this.maxAttempts = maxAttempts;
        entries = new LinkedList<String> ();
    }

    public StatusIndicator addContent(String content) {
        int size = entries.size();
        if (isUnLocked()) {
            return StatusIndicator.FULL;
        }
        if (size == maxEntries) {
            isUnLocked = true;
            return StatusIndicator.JUSTUNLOCKED;
        }
        entries.add(content);
        return StatusIndicator.SUCCESS;
    }

    public StatusIndicator incrementAttempts() {
        numberOfAttempts++;
        if (numberOfAttempts >= maxAttempts) {
            if (!isUnLocked()) {
                isUnLocked = true;
                return StatusIndicator.JUSTUNLOCKED;
            }

        }
        return StatusIndicator.SUCCESS;
    }

    public boolean isUnLocked() {
        return isUnLocked;
    }

    public LinkedList<String> getEntries() {
        if (!isUnLocked) {
            return null;
        }
        return entries;
    }

    public void forceUnlock () {
        isUnLocked = true;
    }

    public String getIdentifier () {
        return identifier;
    }

}
