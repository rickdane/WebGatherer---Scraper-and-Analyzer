package org.Webgatherer.ExperimentalLabs.WebService;

import com.google.gson.Gson;
import com.rickdane.springmodularizedproject.api.transport.Scraper;
import org.Webgatherer.Controller.EntityTransport.EntryTransport;
import org.Webgatherer.Utility.Service.WebServiceClient;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author Rick Dane
 */
public class WebServiceCaller {

    public static void main(String[] args) throws Exception {

        String serviceEndpoint = "http://localhost:8080/springmodularizedproject/webgathererjobs/getPendingJobToLaunch";

        Gson gson = new Gson();

        EntryTransport entryTransport = new EntryTransport();
        entryTransport.setEntry("some entry from web gatherer2222222");
        entryTransport.setDescription("sumfin'");
        String jsonStr = gson.toJson(entryTransport);

        WebServiceClient webService = new WebServiceClient(serviceEndpoint);

        String response = webService.servicePost("", jsonStr, "application/json");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Scraper scraper =  mapper.readValue(response, Scraper.class);

        String pause = "";
    }

}


