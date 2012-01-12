package org.Webgatherer.Controller.Api;

import com.google.gson.Gson;
import com.rickdane.springmodularizedproject.api.transport.Rawscrapeddata;
import com.rickdane.springmodularizedproject.api.transport.Scraper;
import com.rickdane.springmodularizedproject.api.transport.TransportBase;
import org.Webgatherer.Api.Scraper.ScraperFactory;
import org.Webgatherer.Controller.EntityTransport.EntryTransport;
import org.Webgatherer.ExperimentalLabs.Scraper.Core.ScraperBase;
import org.Webgatherer.Utility.Service.WebServiceClient;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
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
    private static final String servicePersistRawscrapeddata = baseApiUrl + "rawscrapeddatas";
    private static final String serviceUrlsAwaitingEmailScrape = baseApiUrl + "rawscrapeddatas/retrieveUrlsAwaitingEmailScrape";

    private static int callIntervalSeconds = 10;
    private static String apiHeader = "application/json";

    private static boolean isRunning = true;

    private static int pageNum = 1;
    private static int maxPages = 1;

    private static int maxUrlEmailScrapeUrls = 20;

    public static void main(String[] args) {

        while (isRunning) {
            EntryTransport entryTransport = new EntryTransport();
            Scraper curScraper = apiPost(entryTransport, serviceEndpointGetScraper, Scraper.class);

            //for testing
            boolean hasRunOnceThrough = false;

            if (curScraper.getType() == Scraper.Type.CRAIGSLIST && !hasRunOnceThrough) {

                ScraperBase scraper = ScraperFactory.createScraper("generic");

                List<String[]> urlEntries = scraper.run("java", pageNum, maxPages);

                for (String[] curEntry : urlEntries) {
                    Rawscrapeddata rawscrapeddata = new Rawscrapeddata();
                    rawscrapeddata.setUrl(curEntry[1]);
                    rawscrapeddata.setFkScraperId(curScraper.getId());
                    rawscrapeddata.setRawscrapeddataEmailScrapeAttempted(Rawscrapeddata.RawscrapeddataEmailScrapeAttempted.NOT_ATTEMPTED);
                    apiPost(rawscrapeddata, servicePersistRawscrapeddata, Rawscrapeddata.class);
                }

                hasRunOnceThrough = true;

            } else {
                sleep();
            }
            //sleep();


            //get urls that need to have emails scraped

            int i = 1;
            List<Rawscrapeddata> rawscrapeddataList = new ArrayList<Rawscrapeddata>();
            for (i = 1; i <= maxUrlEmailScrapeUrls; i++) {

                //dummy object
                TransportBase transportBase = new TransportBase();

                Rawscrapeddata rawscrapeddata = apiPost(transportBase, serviceUrlsAwaitingEmailScrape, Rawscrapeddata.class);
                rawscrapeddataList.add(rawscrapeddata);
            }

            String pause = "";
        }
    }


    private static <T> T apiPost(Object inputObj, String endPoint, Class<T> clazz) {

        String jsonStr = gson.toJson(inputObj);

        WebServiceClient webService = new WebServiceClient(endPoint);

        String apiResponse = webService.servicePost("", jsonStr, apiHeader);

        T object = deserializeFromJson(apiResponse, clazz);

        return object;
    }

    private static <T> void apiPost(Object inputObj, String endPoint) {

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
