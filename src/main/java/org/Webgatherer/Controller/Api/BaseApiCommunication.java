package org.Webgatherer.Controller.Api;

import com.google.gson.Gson;
import org.Webgatherer.Utility.Service.WebServiceClient;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author Rick Dane
 */
public class BaseApiCommunication {

    private static final Gson gson = new Gson();
    private static String apiHeader = "application/json";

    protected static <T> T apiPost(Object inputObj, String endPoint, Class<T> clazz) {

        String jsonStr = gson.toJson(inputObj);

        WebServiceClient webService = new WebServiceClient(endPoint);

        String apiResponse = webService.servicePost("", jsonStr, apiHeader);

        T object = null;
        try {
            object = deserializeFromJson(apiResponse, clazz);
        } catch (Exception e) {

        }

        return object;
    }

    protected static <T> void apiPost(Object inputObj, String endPoint) {

        String jsonStr = gson.toJson(inputObj);

        WebServiceClient webService = new WebServiceClient(endPoint);

        String apiResponse = webService.servicePost("", jsonStr, apiHeader);

    }

    protected static <T> void apiPut(Object inputObj, String endPoint) {

        String jsonStr = gson.toJson(inputObj);

        WebServiceClient webService = new WebServiceClient(endPoint);

        String apiResponse = webService.servicePut("", jsonStr, apiHeader);

    }

    private static <T> T deserializeFromJson(String response, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T object = null;
        try {
            object = mapper.readValue(response, clazz);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return object;
    }

}
