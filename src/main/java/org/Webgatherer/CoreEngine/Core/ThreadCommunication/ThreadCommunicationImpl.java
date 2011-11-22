package org.Webgatherer.CoreEngine.Core.ThreadCommunication;

import java.util.*;

/**
 * @author Rick Dane
 */
@SuppressWarnings({"ALL"})
public class ThreadCommunicationImpl extends ThreadCommunicationBase implements ThreadCommunication {

    private final Queue<String> outputDataHolder = new LinkedList<String>();
    private volatile Queue<String> pageQueue = new LinkedList<String>();
    private final  Queue<String> sendbackDataHolder = new LinkedList<String>();
    private volatile  Map<String, Object> customDataMap = new HashMap<String, Object>();

    public void setPageQueue(Queue pageQueue) {
        if (pageQueue != null) {
            this.pageQueue = pageQueue;
        }
    }

    public boolean isOutputDataHolderEmpty() {
        return outputDataHolder.isEmpty();
    }

    public void addToOutputDataHolder(String string) {
        outputDataHolder.add(string);
    }

    public boolean isSendbackDataHolderEmpty() {
        return sendbackDataHolder.isEmpty();
    }

    public String getFromOutputDataHolder() {
        if (!isOutputDataHolderEmpty()) {
            return outputDataHolder.remove();
        }
        return null;
    }

    public String getFromSendbackDataHolder() {
        if (!isSendbackDataHolderEmpty()) {
            return sendbackDataHolder.remove();
        }
        return null;
    }

    public boolean isPageQueueEmpty() {
        return pageQueue.isEmpty();
    }

    public String getFromPageQueue() {
        if (!isPageQueueEmpty()) {
            return pageQueue.remove();
        }
        return null;
    }

    public void setCustomDataMapElement(String key, Object obj) {

    }

    public void addToSendbackDataHolder(String string) {
        sendbackDataHolder.add(string);
    }

    public Object getCustomDataMapElement(String key) {
        Object returnObject = null;
        try {
            returnObject = customDataMap.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnObject;
    }

    public Map<String, Object> getCustomDataMap() {
        synchronized (customDataMap) {
            return customDataMap;
        }
    }

    public void setCustomDataMap(Map map) {
            if (map != null) {
                this.customDataMap = map;
            }
    }

    public void addToPageQueue(String page) {
        synchronized (pageQueue) {
            pageQueue.add(page);
        }
    }

}
