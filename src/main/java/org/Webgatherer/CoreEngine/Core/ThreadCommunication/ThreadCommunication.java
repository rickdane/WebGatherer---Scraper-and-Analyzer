package org.Webgatherer.CoreEngine.Core.ThreadCommunication;

import java.util.Map;
import java.util.Queue;

/**
 * @author Rick Dane
 */
public interface ThreadCommunication {

    public void setThreadIdentifier(String identifier);

    public void setShouldBeRunning(boolean status);

    public boolean isOutputDataHolderEmpty();

    public void setPageQueue (Queue pageQueue);

    /**
     * the thread can check this to gracefully "shutdown" itself if set to false
     * @return
     */
    public Boolean shouldBeRunning();

    public void addToOutputDataHolder(String string);

    public void addToSendbackDataHolder(String string);

    public boolean isSendbackDataHolderEmpty();

    public String getFromSendbackDataHolder();

    public boolean isPageQueueEmpty();

    public String getFromPageQueue();

    public int getCheckQuitInterval ();

    public String getFromOutputDataHolder();

    public void setCheckQuitInterval (int checkQuitInterval);

    /**
     * Sets an object into the local hashmap for storing custom objects, these objects are ONLY meant for use
     * within workflows as they are inherently error prone due to lack of generics
     * @param key
     * @param obj
     */
    public void setCustomDataMapElement (String key, Object obj);

    public Object getCustomDataMapElement (String key);

    public Map<String, Object> getCustomDataMap ();

    public void setCustomDataMap (Map map);

    public void addToPageQueue (String page);
}
