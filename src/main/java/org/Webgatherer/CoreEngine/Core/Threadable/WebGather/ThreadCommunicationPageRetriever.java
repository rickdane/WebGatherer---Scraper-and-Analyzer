package org.Webgatherer.CoreEngine.Core.Threadable.WebGather;

import com.google.inject.Inject;
import org.Webgatherer.Common.Properties.PropertiesContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;

import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is meant to be used as an object that is accessed by a large number of threads, therefore all variables that are exposed
 * publicly must be thread safe
 *
 * @author Rick Dane
 */
public class ThreadCommunicationPageRetriever {

    private HashSet<String> slowLoadingIgnoreUrls = new HashSet<String>();
    private int maxMillisecondTimeout;
    private int maxAllowedRunningThreads;

    private AtomicInteger numberOfRunningThreads = new AtomicInteger();

    @Inject
    public ThreadCommunicationPageRetriever(PropertiesContainer propertiesContainer) {
        Properties properties = propertiesContainer.getProperties("CoreEngine");
        maxMillisecondTimeout = Integer.parseInt(properties.getProperty("threadCommunicationPageRetriever_maxMillisecondTimeout"));
        maxAllowedRunningThreads = Integer.parseInt(properties.getProperty("threadCommunicationPageRetriever_maxAllowedRunningThreads"));
    }

    public boolean doesSlowLoadingUrlsContain(String key) {
        if (slowLoadingIgnoreUrls.contains(key)) {
            return true;
        }
        return false;
    }

    public int getMaxMillisecondTimeout() {
        return maxMillisecondTimeout;
    }

    public void addSlowLoadingIgnoreUrl(String url) {
        slowLoadingIgnoreUrls.add(url);
    }

    public void registerKilledThread() {
        numberOfRunningThreads.decrementAndGet();
    }

    public void registerNewThread() {
        numberOfRunningThreads.incrementAndGet();
    }

    public boolean allowedToCreateNewThread() {
        if (numberOfRunningThreads.get() <= maxAllowedRunningThreads) {
            return true;
        }
        return false;
    }
}
