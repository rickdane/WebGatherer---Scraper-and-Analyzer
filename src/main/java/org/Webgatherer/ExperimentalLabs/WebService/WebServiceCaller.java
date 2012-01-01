package org.Webgatherer.ExperimentalLabs.WebService;

import com.google.gson.Gson;
import org.Webgatherer.Controller.EntityTransport.EntryTransport;
import org.Webgatherer.Utility.Service.WebServiceClient;

/**
 * @author Rick Dane
 */
public class WebServiceCaller {

    public static void main(String[] args) throws Exception {

        String serviceEndpoint = "http://localhost:8080/springservicesmoduleroo/entrys";

        Gson gson = new Gson();

        EntryTransport entryTransport = new EntryTransport();
        entryTransport.setEntry("some entry from web gatherer2222222");
        entryTransport.setDescription("sumfin'");
        String jsonStr = gson.toJson(entryTransport);

        WebServiceClient webService = new WebServiceClient(serviceEndpoint);

        webService.servicePost("", jsonStr, "application/json");

        String resp = webService.serviceGet("1");

        String pause = "";
    }

}


