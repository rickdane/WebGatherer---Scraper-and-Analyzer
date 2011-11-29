package org.Webgatherer.WorkflowExample.DataHolders;

import org.Webgatherer.WorkflowExample.Status.StatusIndicator;

/**This is a data holder container that specifically holds text article / page content. It is not thread-safe and is not meant to be
 * used in a way that requires thread safety. Instead, it stays "locked" until its done being used and only then can it be released
 * to a class other than the one that created it.
 * @author Rick Dane
 */
public class ContainerBase {

    private final String identifier;
    private final int maxEntries;
    private final String[] entries;
    private final int maxAttempts;
    private int numberOfAttempts;
    private boolean isLocked = false;

    public ContainerBase(String identifier, int maxEntries, int maxAttempts) {
        this.maxEntries = maxEntries;
        this.identifier = identifier;
        this.maxAttempts = maxAttempts;
        entries = new String[maxEntries];
    }

    public StatusIndicator addContent(String content) {
        int size = entries.length;
        if (isLocked()) {
            return StatusIndicator.FULL;
        }
        if (size == maxEntries) {
            isLocked = true;
            return StatusIndicator.JUSTUNLOCKED;
        }
        entries[size - 1] = content;
        return StatusIndicator.SUCCESS;
    }

    public StatusIndicator incrementAttempts() {
        numberOfAttempts++;
        if (numberOfAttempts >= maxAttempts) {
            if (!isLocked()) {
               isLocked = true;
                return StatusIndicator.JUSTUNLOCKED;
            }

        }
        return StatusIndicator.SUCCESS;
    }

    public boolean isLocked() {
        return isLocked;
    }

}
