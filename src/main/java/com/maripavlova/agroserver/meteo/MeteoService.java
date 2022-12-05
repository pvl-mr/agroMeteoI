package com.maripavlova.agroserver.meteo;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MeteoService {

    static final String PUBLIC_KEY = "ba9c8f7415885c20da4ab8e7cd46bf2de6a49b8c1e320dea";
    static final String PRIVATE_KEY = "9e9f3139bbe88c1a47475225b5991713bb1c4e8fc7a49c8f";
    static final String BASE_URL = "https://api.fieldclimate.com/v2";


    public JSONObject getTemperatures() throws Exception {
        String now = getServerTime();
        String requestHttpMethod = "GET";
        String requestRoute = "/data/00001F77/GROUP-BY/from/1669896389/to/1669982789";
//        String requestRoute = "/station/00001F77";
//        String requestRoute = "/user/stations";

        HttpGet request = new HttpGet(BASE_URL + requestRoute);
        request.setHeader(HttpHeaders.DATE, now);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        String signatureRawData = String.format("%s%s%s%s", requestHttpMethod, requestRoute, now, PUBLIC_KEY);
        String requestSignatureString = encode(PRIVATE_KEY, signatureRawData);
        String authHeaderValue = String.format("hmac %s:%s", PUBLIC_KEY, requestSignatureString);

        request.setHeader(HttpHeaders.AUTHORIZATION, authHeaderValue);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(request);
        System.out.println(response.toString());
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        System.out.println("Response body: " + responseBody);
        JSONObject jsonObject = new JSONObject(responseBody);
        System.out.println(jsonObject);
        JSONObject clean_json = new JSONObject();

        clean_json.put("station_id", "00001F77");
        clean_json.put("time_to", "1669982789");
        clean_json.put("time_from", "1669896389");

        JSONArray jsonDates = jsonObject.getJSONArray("dates");
        JSONObject jsonInfo = (jsonObject.getJSONArray("data"))
                            .getJSONObject(5);
        JSONObject jsonValues = jsonInfo.getJSONObject("values");
        String jsonParameterName = jsonInfo.getString("name_original");
        clean_json.put("parameter_name", jsonParameterName);
        clean_json.put("values", jsonValues);
        clean_json.put("dates", jsonDates);
        System.out.println(clean_json);
        return clean_json;
    }

    @Cacheable("temperatures")
    @Scheduled(fixedRate=25000)
    public MeteoParameter getTemperatures2() throws Exception {
        String now = getServerTime();
        String requestHttpMethod = "GET";
        String requestRoute = "/data/00001F77/GROUP-BY/from/1669896389/to/1669982789";
//        String requestRoute = "/station/00001F77";
//        String requestRoute = "/user/stations";

        HttpGet request = new HttpGet(BASE_URL + requestRoute);
        request.setHeader(HttpHeaders.DATE, now);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        String signatureRawData = String.format("%s%s%s%s", requestHttpMethod, requestRoute, now, PUBLIC_KEY);
        String requestSignatureString = encode(PRIVATE_KEY, signatureRawData);
        String authHeaderValue = String.format("hmac %s:%s", PUBLIC_KEY, requestSignatureString);

        request.setHeader(HttpHeaders.AUTHORIZATION, authHeaderValue);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(request);
        System.out.println(response.toString());
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        System.out.println("Response body: " + responseBody);
        JSONObject jsonObject = new JSONObject(responseBody);
        System.out.println(jsonObject);

        MeteoParameter meteoParameter = new MeteoParameter();

        meteoParameter.setMeteoId("00001F77");
        JSONObject jsonInfo = (jsonObject.getJSONArray("data"))
                .getJSONObject(5);
        meteoParameter.setParameterName(jsonInfo.getString("name_original"));
        meteoParameter.setMeteoId("00001F77");
        meteoParameter.setStartTime("1669896389");
        meteoParameter.setEndTime("1669982789");


        JSONArray jsonArrayDates = jsonObject.getJSONArray("dates");
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < jsonArrayDates.length(); i++) {
            dates.add(jsonArrayDates.getString(i));
        }
        meteoParameter.setDates(dates);

        JSONArray jsonArrayValues = jsonInfo.getJSONObject("values").getJSONArray("avg");
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < jsonArrayValues.length(); i++) {
            values.add(jsonArrayValues.getInt(i));
        }
        meteoParameter.setValues(new ValueForm("avg", values));

        return meteoParameter;



    }

    public static String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }


    public static String encode(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }
}
