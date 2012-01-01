package org.Webgatherer.Controller.Base;

import com.google.gson.Gson;
import org.Webgatherer.Controller.EntityTransport.EntryTransport;
import org.Webgatherer.Utility.Service.WebServiceClient;

/**
 * @author Rick Dane
 */
public class EntryBase {

    protected static WebServiceClient webServiceClient = new WebServiceClient("http://localhost:8080/springservicesmoduleroo/entrys");
    protected static String webServiceContentType = "application/json";


    protected static void persistEntry(String data) {
        EntryTransport entryTransport = new EntryTransport();
        entryTransport.setDescription(data);
        Gson gson = new Gson();
        String jsonData = gson.toJson(entryTransport);
        webServiceClient.servicePost("", jsonData, webServiceContentType);
    }

}
