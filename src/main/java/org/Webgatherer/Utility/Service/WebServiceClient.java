package org.Webgatherer.Utility.Service;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

@ThreadSafe
public class WebServiceClient {

    DefaultHttpClient httpClient;
    HttpContext localContext;

    String webServiceUrl;

    public WebServiceClient(String baseUrl) {
        HttpParams myParams = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(myParams, 10000);
        HttpConnectionParams.setSoTimeout(myParams, 10000);
        httpClient = new DefaultHttpClient(myParams);
        localContext = new BasicHttpContext();
        webServiceUrl = baseUrl;

        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);

    }

    public String servicePost(String methodName, String data, String contentType) {
        HttpPost httpPost = new HttpPost(webServiceUrl + methodName);

        return serviceCall(methodName, data, contentType, httpPost);
    }

    public String servicePut(String methodName, String data, String contentType) {
        HttpPut httpPut = new HttpPut(webServiceUrl + methodName);

        return serviceCall(methodName, data, contentType, httpPut);
    }

    private String serviceCall(String methodName, String data, String contentType, HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase) {
        String ret = null;

        HttpResponse response = null;

        StringEntity tmp = null;

        if (contentType != null) {
            httpEntityEnclosingRequestBase.setHeader("Content-Type", contentType);
        } else {
            httpEntityEnclosingRequestBase.setHeader("Content-Type", "application/x-www-form-urlencoded");
        }

        try {
            tmp = new StringEntity(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        httpEntityEnclosingRequestBase.setEntity(tmp);

        try {
            response = httpClient.execute(httpEntityEnclosingRequestBase, localContext);

            if (response != null) {
                ret = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
        }

        return ret;
    }

    public String serviceGet(String endValue) {
        HttpGet httpGet = new HttpGet(webServiceUrl + "/" + endValue);

        return coreGet(httpGet);
    }

    public String serviceGet(String methodName, Map<String, String> params) {
        String getUrl = webServiceUrl + methodName;

        int i = 0;
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (i == 0) {
                getUrl += "?";
            } else {
                getUrl += "&";
            }

            try {
                getUrl += param.getKey() + "=" + URLEncoder.encode(param.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        HttpGet httpGet = new HttpGet(getUrl);

        return coreGet(httpGet);
    }

    private String coreGet(HttpGet httpGet) {
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (Exception e) {
        }

        String ret = null;

        try {
            ret = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
        }

        return ret;
    }

    public static JSONObject Object(Object o) {
        try {
            return new JSONObject(new Gson().toJson(o));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream getHttpStream(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            response = httpConn.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception e) {
            throw new IOException("Error connecting");
        }

        return in;
    }

    public void clearCookies() {
        httpClient.getCookieStore().clear();
    }

}