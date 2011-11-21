package org.Webgatherer.Core.ThreadCommunication;

/**
 * @author Rick Dane
 */
public class ThreadCommunicationBase {
        private String threadIdentifier;
        private Boolean shouldBeRunning = true;
        private int checkQuitInterval = 25;

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
}
