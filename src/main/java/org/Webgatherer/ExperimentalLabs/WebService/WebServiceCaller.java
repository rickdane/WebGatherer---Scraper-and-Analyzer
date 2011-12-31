package org.Webgatherer.ExperimentalLabs.WebService;

import com.google.gson.Gson;

/**
 * @author Rick Dane
 */
public class WebServiceCaller {


    public static void main(String[] args) throws Exception {

        String serviceEndpoint = "http://localhost:8080/springservicesmoduleroo/entrys";

        Gson gson = new Gson();

        Entry entryTransport = new Entry();
        entryTransport.setEntry("some entry from web gatherer2222222");
        entryTransport.setDescription("sumfin'");
        String jsonStr = gson.toJson(entryTransport);

        String json2 = "{description: \"entry from webgatherer\"}";

        WebServiceClient webService = new WebServiceClient(serviceEndpoint);

        webService.servicePost("", jsonStr, "application/json");

        String resp = webService.serviceGet("1");

        String pause = "";
    }

}


