package org.Webgatherer.Controller.Api;

import com.google.gson.Gson;
import com.rickdane.springmodularizedproject.api.transport.Rawscrapeddata;
import com.rickdane.springmodularizedproject.api.transport.Scraper;
import org.Webgatherer.Api.Scraper.ScraperFactory;
import org.Webgatherer.Controller.EntityTransport.EntryTransport;
import org.Webgatherer.ExperimentalLabs.Scraper.Core.ScraperBase;
import org.Webgatherer.Utility.Service.WebServiceClient;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;

/**
 * This is the main method for the applicatin, it polls the API at a set interval to check for new jobs to run
 *
 * @author Rick Dane
 */
public class ApiCommunication {

    private static final Gson gson = new Gson();
    private static final String baseApiUrl = "http://localhost:8080/springmodularizedproject/";
    private static final String serviceEndpointGetScraper = baseApiUrl + "webgathererjobs/getPendingJobToLaunch";
    private static final String servicePersistRawscrapeddata = baseApiUrl + "webgathererjobs/rawscrapeddatas";

    private static int callIntervalSeconds = 10;
    private static String apiHeader = "application/json";

    private static boolean isRunning = true;

    private static int pageNum = 1;
    private static int maxPages = 2;

    public static void main(String[] args) {

        while (isRunning) {
            EntryTransport entryTransport = new EntryTransport();
            Scraper curScraper = apiPost(entryTransport, serviceEndpointGetScraper, Scraper.class);

            if (curScraper.getType() == Scraper.Type.CRAIGSLIST) {

                ScraperBase scraper = ScraperFactory.createScraper("generic");

                List<String[]> urlEntries = scraper.run("java", pageNum, maxPages);

                for (String[] curEntry : urlEntries) {
                    Rawscrapeddata rawscrapeddata = new Rawscrapeddata();
                    rawscrapeddata.setEmailAddress(curEntry[1]);
                    rawscrapeddata.setFkScraperId(curScraper.getId());
                    apiCall(entryTransport, servicePersistRawscrapeddata);
                }

            } else {
                sleep();
                continue;
            }
            sleep();
        }
    }


    private static <T> T apiPost(Object inputObj, String endPoint, Class<T> clazz) {

        String jsonStr = gson.toJson(inputObj);

        WebServiceClient webService = new WebServiceClient(endPoint);

        String apiResponse = webService.servicePost("", jsonStr, apiHeader);

        T object = deserializeFromJson(apiResponse, clazz);

        return object;
    }

    private static <T> void apiCall(Object inputObj, String endPoint) {

        String jsonStr = gson.toJson(inputObj);

        WebServiceClient webService = new WebServiceClient(endPoint);

        String apiResponse = webService.servicePost("", jsonStr, apiHeader);

    }

    private static <T> T deserializeFromJson(String response, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        T object = null;
        try {
            object = mapper.readValue(response, clazz);
        } catch (Exception e) {

        }
        return object;
    }

    private static void sleep() {
        try {
            Thread.sleep(callIntervalSeconds * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
