package org.Webgatherer.CoreEngine.Core.ThreadCommunication;

/**
 * @author Rick Dane
 */
public class ThreadCommunicationBase {
    private String threadIdentifier;
    private Boolean shouldBeRunning = true;
    private int checkQuitInterval = 25;
    private boolean isWebGathererThreadFinished = false;

    public enum PageQueueEntries {
        KEY, BASE_URL, CATEGORY, SCRAPED_PAGE, CUSTOM_PARAM, NUM_PAGES_TOSCRAPE, CUSTOM_LABEL;
    }

    public void setThreadIdentifier(String identifier) {
        this.threadIdentifier = identifier;
    }

    public void setShouldBeRunning(boolean status) {
        shouldBeRunning = status;
    }

    public Boolean shouldBeRunning() {
        return shouldBeRunning;
    }

    public int getCheckQuitInterval() {
        return checkQuitInterval;
    }

    public void setCheckQuitInterval(int checkQuitInterval) {
        this.checkQuitInterval = checkQuitInterval;
    }

    public boolean isWebGathererThreadFinished() {
        return isWebGathererThreadFinished;
    }

    public void setIsWebGathererThreadFinished(boolean value) {
        isWebGathererThreadFinished = value;
    }
}
