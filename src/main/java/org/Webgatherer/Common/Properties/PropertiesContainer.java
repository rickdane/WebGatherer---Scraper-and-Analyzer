package org.Webgatherer.Common.Properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Rick Dane
 */
public class PropertiesContainer {

    private final String basePath = "/home/user/Dropbox/Rick/WebGatherer/WebGatherer---Scraper-and-Analyzer/src/main/resources/Webgatherer/";
    private final String[] propertiesFilePaths = {"WorkflowExample/WorkflowExample", "CoreEngine/CoreEngine"};
    private static Map<String, Properties> propertiesMap;
    private static boolean isInitialized = false;

    public PropertiesContainer() {

        if (isInitialized) {
            return;
        }
        isInitialized = true;

        propertiesMap = new HashMap<String, Properties>();

        for (String curEntry : propertiesFilePaths) {
            String[] split = curEntry.split("/");
            String label = split[split.length - 1];

            Properties curProperty = new Properties();
            try {
                curProperty.load(new FileInputStream(basePath + curEntry + ".properties"));
            } catch (IOException e) {
            }

            propertiesMap.put(label, curProperty);
        }
    }

    public Properties getProperties(String key) {
        return propertiesMap.get(key);
    }
}
