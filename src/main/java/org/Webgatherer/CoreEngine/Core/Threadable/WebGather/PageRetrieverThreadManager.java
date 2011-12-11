package org.Webgatherer.CoreEngine.Core.Threadable.WebGather;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.Webgatherer.Common.Properties.PropertiesContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunicationBase;
import sun.net.idn.StringPrep;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Rick Dane
 */
public class PageRetrieverThreadManager {

    private ThreadCommunication threadCommunication;

    private Map<String, Queue<String[]>> waitingUrls = new HashMap<String, Queue<String[]>>();
    private HashSet<String> inWaiting = new HashSet<String>();

    private WebGather webGather;
    private int maxNullEntries;
    private int cntMaxNullEntries;

    //TODO properties
    private int reloadInterval;

    private Date lastIntervalCheck = new Date();
    private Provider<ThreadRetrievePage> threadRetrievePageProvider;
    private ThreadCommunicationPageRetriever threadCommunicationPageRetriever;

    @Inject
    public PageRetrieverThreadManager(Provider<ThreadRetrievePage> threadRetrievePageProvider, PropertiesContainer propertiesContainer, ThreadCommunicationPageRetriever threadCommunicationPageRetriever) {
        Properties properties = propertiesContainer.getProperties("CoreEngine");
        maxNullEntries = Integer.parseInt(properties.getProperty("webGather_maxNullEntries"));
        cntMaxNullEntries = Integer.parseInt(properties.getProperty("webGather_cntMaxNullEntries"));

         this.threadCommunicationPageRetriever = threadCommunicationPageRetriever;
        this.threadRetrievePageProvider = threadRetrievePageProvider;
        reloadInterval = Integer.parseInt(properties.getProperty("pageRetrieverThreadManager_reloadInterval"));
    }

    public void checkToExpireInterval(int retrieveType) {

        Date now = new Date();

        if (now.getTime() - lastIntervalCheck.getTime() > reloadInterval) {
            expireInterval(retrieveType);
            lastIntervalCheck = new Date();

        }
    }

    private void expireInterval(int retrieveType) {
        for (Map.Entry<String, Queue<String[]>> entries : waitingUrls.entrySet()) {
            String key = entries.getKey();
            Queue<String[]> curQueue = entries.getValue();

            if (!curQueue.isEmpty()) {
                String[] curEntry = curQueue.remove();
                if (curEntry != null) {
                    launchThread(curEntry, retrieveType);
                    if (curQueue.isEmpty()) {
                        inWaiting.remove(curEntry[ThreadCommunicationBase.PageQueueEntries.KEY.ordinal()]);
                    }
                }
            }
        }
    }

    public void configure(WebGather webGather, ThreadCommunication threadCommunication) {
        this.threadCommunication = threadCommunication;
    }

    public void run(int retrieveType) {

        String[] entry = threadCommunication.getFromPageQueue();
        if (entry == null) {
            return;
        }

        if (!determineIfCanStartThreadImmediately(entry)) {
            return;
        }

        launchThread(entry, retrieveType);
    }

    private void launchThread(String[] entry, int retrieveType) {
        ThreadRetrievePage threadRetrievePage = threadRetrievePageProvider.get();
        threadRetrievePage.configure(entry, threadCommunication, retrieveType,threadCommunicationPageRetriever);
        threadRetrievePage.start();
    }

    private boolean determineIfCanStartThreadImmediately(String[] entry) {

        String key = entry[ThreadCommunicationBase.PageQueueEntries.KEY.ordinal()];

        Queue domainSpecificQueue = waitingUrls.get(key);
        if (domainSpecificQueue == null) {
            domainSpecificQueue = new LinkedList<String[]>();
            waitingUrls.put(key, domainSpecificQueue);
        }

        if (inWaiting.contains(key)) {
            domainSpecificQueue.add(entry);
            return false;
        }
        inWaiting.add(key);
        return true;
    }
}
