package com.maripavlova.agroserver.meteo;

import com.maripavlova.agroserver.meteo.Entities.MeteoParameter;
import com.maripavlova.agroserver.meteo.Entities.ValueForm;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.maripavlova.agroserver.meteo.utils.DateUtils.*;
import static com.maripavlova.agroserver.meteo.utils.NormRegionUtils.readFromJson;
import static com.maripavlova.agroserver.meteo.utils.PrecipitationUtils.readFromJsonPrecipitation;

@Slf4j
@Service
public class MeteoService {

    static final String PUBLIC_KEY = "ba9c8f7415885c20da4ab8e7cd46bf2de6a49b8c1e320dea";
    static final String PRIVATE_KEY = "9e9f3139bbe88c1a47475225b5991713bb1c4e8fc7a49c8f";
    static final String BASE_URL = "https://api.fieldclimate.com/v2";


//    public JSONObject getTemperatures() throws Exception {
//        String now = getServerTime();
//        String requestHttpMethod = "GET";
//        String requestRoute = "/data/00001F77/GROUP-BY/from/1669896389/to/1669982789";
////        String requestRoute = "/station/00001F77";
////        String requestRoute = "/user/stations";
//
//        HttpGet request = new HttpGet(BASE_URL + requestRoute);
//        request.setHeader(HttpHeaders.DATE, now);
//        request.setHeader(HttpHeaders.ACCEPT, "application/json");
//        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
//
//        String signatureRawData = String.format("%s%s%s%s", requestHttpMethod, requestRoute, now, PUBLIC_KEY);
//        String requestSignatureString = encode(PRIVATE_KEY, signatureRawData);
//        String authHeaderValue = String.format("hmac %s:%s", PUBLIC_KEY, requestSignatureString);
//
//        request.setHeader(HttpHeaders.AUTHORIZATION, authHeaderValue);
//
//        HttpClient httpClient = HttpClientBuilder.create().build();
//        HttpResponse response = httpClient.execute(request);
//        System.out.println(response.toString());
//        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
//        System.out.println("Response body: " + responseBody);
//        JSONObject jsonObject = new JSONObject(responseBody);
//        System.out.println(jsonObject);
//        JSONObject clean_json = new JSONObject();
//
//        clean_json.put("station_id", "00001F77");
//        clean_json.put("time_to", "1669982789");
//        clean_json.put("time_from", "1669896389");
//
//        JSONArray jsonDates = jsonObject.getJSONArray("dates");
//        JSONObject jsonInfo = (jsonObject.getJSONArray("data"))
//                            .getJSONObject(5);
//        JSONObject jsonValues = jsonInfo.getJSONObject("values");
//        String jsonParameterName = jsonInfo.getString("name_original");
//        clean_json.put("parameter_name", jsonParameterName);
//        clean_json.put("values", jsonValues);
//        clean_json.put("dates", jsonDates);
//        System.out.println(clean_json);
//        return clean_json;
//    }

    public MeteoParameter getTemperatures() throws Exception {
        return getParameters(Parameters.HC_AIR_TEMPERATURE);
    }

    public MeteoParameter getWindSpeeds() throws Exception {
        return getParameters(Parameters.WIND_SPEED);
    }

    public MeteoParameter getPrecipitations() throws Exception {
        return getParameters(Parameters.PRECIPITATION);
    }

    public MeteoParameter getHumidities() throws Exception {
        return getParameters(Parameters.HC_RELATIVE_HUMIDITY);
    }

    @Cacheable("parameters")
    public MeteoParameter getParameters(Parameters parameterName) throws Exception {
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
                .getJSONObject(parameterName.ordinal());
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
        String type = "avg";
        if (parameterName == Parameters.PRECIPITATION) {
            type = "sum";
        }
        JSONArray jsonArrayValues = jsonInfo.getJSONObject("values").getJSONArray(type);
//        JSONArray jsonArrayValues = jsonInfo.getJSONObject("values").getJSONArray("avg");


        List<Float> values = new ArrayList<>();
        for (int i = 0; i < jsonArrayValues.length(); i++) {
            values.add(jsonArrayValues.getFloat(i));
        }
        meteoParameter.setValues(new ValueForm(type, values));

        return meteoParameter;

    }

    public HttpGet getRequest(String requestHttpMethod, String requestRoute) throws Exception {
        String now = getServerTime();
        HttpGet request = new HttpGet(BASE_URL + requestRoute);
        request.setHeader(HttpHeaders.DATE, now);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        String signatureRawData = String.format("%s%s%s%s", requestHttpMethod, requestRoute, now, PUBLIC_KEY);
        String requestSignatureString = encode(PRIVATE_KEY, signatureRawData);
        String authHeaderValue = String.format("hmac %s:%s", PUBLIC_KEY, requestSignatureString);

        request.setHeader(HttpHeaders.AUTHORIZATION, authHeaderValue);
        return request;

    }

    public MeteoParameter createResponse(JSONObject jsonObject, MeteoParameter requestMeteoParameter) throws NumberFormatException {

        String stationId = requestMeteoParameter.getMeteoId();
        Parameters parameterName = Parameters.valueOf(requestMeteoParameter.getParameterName());
        String startTime = requestMeteoParameter.getStartTime();
        String endTime = requestMeteoParameter.getEndTime();

        MeteoParameter meteoParameter = new MeteoParameter();
        meteoParameter.setMeteoId(stationId);
        JSONObject jsonInfo = (jsonObject.getJSONArray("data"))
                .getJSONObject(parameterName.ordinal());
        meteoParameter.setParameterName(jsonInfo.getString("name_original"));
        meteoParameter.setStartTime(startTime);
        meteoParameter.setEndTime(endTime);


        JSONArray jsonArrayDates = jsonObject.getJSONArray("dates");
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < jsonArrayDates.length(); i++) {
            dates.add(jsonArrayDates.getString(i));
        }
        meteoParameter.setDates(dates);
        String type = "avg";
        if (parameterName == Parameters.PRECIPITATION) {
            type = "sum";
        }
        if (parameterName == Parameters.LEAF_WETNESS) {
            type = "time";
        }
        if (parameterName == Parameters.BATTERY) {
            type = "last";
        }
        JSONArray jsonArrayValues = jsonInfo.getJSONObject("values").getJSONArray(type);
        System.out.println("=============");
        System.out.println(jsonArrayValues);


        List<Float> values = new ArrayList<>();
        for (int i = 0; i < jsonArrayValues.length(); i++) {
            try
            {
                values.add(jsonArrayValues.getFloat(i));
            }
            catch (Exception ex)
            {
                values.add(0F);
            }
        }
        meteoParameter.setValues(new ValueForm(type, values));

        return meteoParameter;
    }

    public MeteoParameter createIncreasedPrecipitationResponse(JSONObject jsonObject, MeteoParameter requestMeteoParameter) {

        String stationId = requestMeteoParameter.getMeteoId();
        Parameters parameterName = Parameters.valueOf(requestMeteoParameter.getParameterName());
        String startTime = requestMeteoParameter.getStartTime();
        String endTime = requestMeteoParameter.getEndTime();

        MeteoParameter meteoParameter = new MeteoParameter();
        meteoParameter.setMeteoId(stationId);
        JSONObject jsonInfo = (jsonObject.getJSONArray("data"))
                .getJSONObject(parameterName.ordinal());
        meteoParameter.setParameterName(jsonInfo.getString("name_original"));
        meteoParameter.setStartTime(startTime);
        meteoParameter.setEndTime(endTime);


        JSONArray jsonArrayDates = jsonObject.getJSONArray("dates");
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < jsonArrayDates.length(); i++) {
            dates.add(jsonArrayDates.getString(i));
        }
        meteoParameter.setDates(dates);
        String type = "sum";

        JSONArray jsonArrayValues = jsonInfo.getJSONObject("values").getJSONArray(type);


        List<Float> values = new ArrayList<>();
        values.add(jsonArrayValues.getFloat(0));
        for (int i = 1; i < jsonArrayValues.length(); i++) {
            values.add(values.get(i-1) + jsonArrayValues.getInt(i));
        }
        meteoParameter.setValues(new ValueForm(type, values));

        return meteoParameter;
    }


    public MeteoParameter getParams(MeteoParameter requestMeteoParameter) throws Exception {
        String requestHttpMethod = "GET";
        String requestRoute = String.format("/data/%s/GROUP-BY/from/%s/to/%s",
                                            requestMeteoParameter.getMeteoId(),
                                            requestMeteoParameter.getStartTime(),
                                            requestMeteoParameter.getEndTime());
        HttpGet request = getRequest(requestHttpMethod, requestRoute);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(request);
        System.out.println(response.toString());
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        System.out.println("Response body: " + responseBody);
        JSONObject jsonObject = new JSONObject(responseBody);
        System.out.println(jsonObject);
        return createResponse(jsonObject, requestMeteoParameter);
    }

    public MeteoParameter getIncreasedPrecipitation(MeteoParameter requestMeteoParameter) throws Exception {
        String requestHttpMethod = "GET";
        String requestRoute = String.format("/data/%s/GROUP-BY/from/%s/to/%s",
                requestMeteoParameter.getMeteoId(),
                requestMeteoParameter.getStartTime(),
                requestMeteoParameter.getEndTime());
        HttpGet request = getRequest(requestHttpMethod, requestRoute);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(request);
        System.out.println(response.toString());
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        System.out.println("Response body: " + responseBody);
        JSONObject jsonObject = new JSONObject(responseBody);
        System.out.println(jsonObject);
        return createIncreasedPrecipitationResponse(jsonObject, requestMeteoParameter);
    }

    public MeteoParameter createNormTemperatureResponse(JSONObject jsonObject, MeteoParameter requestMeteoParameter) throws IOException {
        String stationId = requestMeteoParameter.getMeteoId();
        Parameters parameterName = Parameters.valueOf(requestMeteoParameter.getParameterName());
        String startTime = requestMeteoParameter.getStartTime();
        String endTime = requestMeteoParameter.getEndTime();

        MeteoParameter meteoParameter = new MeteoParameter();
        meteoParameter.setMeteoId(stationId);
        JSONObject jsonInfo = (jsonObject.getJSONArray("data"))
                .getJSONObject(parameterName.ordinal());
        meteoParameter.setParameterName(jsonInfo.getString("name_original"));
        meteoParameter.setStartTime(startTime);
        meteoParameter.setEndTime(endTime);


        JSONArray jsonArrayDates = jsonObject.getJSONArray("dates");
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < jsonArrayDates.length(); i++) {
            dates.add(jsonArrayDates.getString(i));
        }
        meteoParameter.setDates(dates);
        String type = "avg";
        if (parameterName == Parameters.PRECIPITATION) {
            type = "sum";
        }
        JSONArray jsonArrayValues = jsonInfo.getJSONObject("values").getJSONArray(type);


        List<Float> values = new ArrayList<>();
        for (int i = 0; i < jsonArrayValues.length(); i++) {
            values.add(jsonArrayValues.getFloat(i));
        }
        meteoParameter.setValues(new ValueForm(type, values));

        ValueForm normValues = getTemperatureNormValues(startTime, endTime);
        meteoParameter.setNormValues(normValues);

        return meteoParameter;

    }

    private MeteoParameter createNormPrecipitationResponse(JSONObject jsonObject, MeteoParameter requestMeteoParameter) throws IOException {
        String stationId = requestMeteoParameter.getMeteoId();
        Parameters parameterName = Parameters.valueOf(requestMeteoParameter.getParameterName());
        String startTime = requestMeteoParameter.getStartTime();
        String endTime = requestMeteoParameter.getEndTime();

        MeteoParameter meteoParameter = new MeteoParameter();
        meteoParameter.setMeteoId(stationId);
        JSONObject jsonInfo = (jsonObject.getJSONArray("data"))
                .getJSONObject(parameterName.ordinal());
        meteoParameter.setParameterName(jsonInfo.getString("name_original"));
        meteoParameter.setStartTime(startTime);
        meteoParameter.setEndTime(endTime);


        JSONArray jsonArrayDates = jsonObject.getJSONArray("dates");
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < jsonArrayDates.length(); i++) {
            dates.add(jsonArrayDates.getString(i));
        }
        meteoParameter.setDates(dates);
        String type = "sum";
        JSONArray jsonArrayValues = jsonInfo.getJSONObject("values").getJSONArray(type);


        List<Float> values = new ArrayList<>();
        for (int i = 0; i < jsonArrayValues.length(); i++) {
            values.add(jsonArrayValues.getFloat(i));
        }
        meteoParameter.setValues(new ValueForm(type, values));

        ValueForm normValues = getPrecipitationNormValues(startTime, endTime);
        System.out.println("=======================");
        System.out.println(normValues);
        meteoParameter.setNormValues(normValues);
        log.info("1. Running into createNormPrecipitationResponse ", meteoParameter);

        return meteoParameter;
    }




    public ValueForm getTemperatureNormValues(String startTime, String endTime) throws IOException {

        String startingTimeStr = startTime;
        String endingTimeStr = endTime;

        Integer between = getDaysBetween(startingTimeStr, endingTimeStr);

        ValueForm values = new ValueForm("calculated");
        Map<String, Float> dict = readFromJson();

        for (int i = 0; i < between; i++) {
            String[] nextDay = getNeedTime(startingTimeStr);
            String nextDateView = nextDay[0];
            values.getValues().add(dict.get(nextDateView));
            startingTimeStr = nextDay[1];


        }

        values.getValues().add(dict.get(getSimpleNeedTime(endingTimeStr)[0]));
        System.out.println("==================");
        System.out.println(values.getValues());
        System.out.println(values.getValues().size());

        return values;

    }

    public ValueForm getPrecipitationNormValues(String startTime, String endTime) throws IOException {

        String startingTimeStr = startTime;
        String endingTimeStr = endTime;

        Integer between = getMonthBetween(startingTimeStr, endingTimeStr);

        ValueForm values = new ValueForm("calculated");
        Map<String, Float> dict = readFromJsonPrecipitation();
        System.out.println("dictionary");
        System.out.println(dict.keySet() + " " + dict.values());

        Integer first_month = Integer.parseInt(getMonthOfDate(startingTimeStr));
        System.out.println("first_month " + first_month);
        System.out.println("between " + between);
        for (int i = 0; i < between; i++) {
            Float value = dict.get(String.valueOf(i+first_month));
            System.out.println("value=" + value);
            values.getValues().add(value);
        }

        System.out.println(values.getValues());
        System.out.println(values.getValues().size());

        return values;

    }



    public MeteoParameter getTemperatureWithNorm(MeteoParameter requestMeteoParameter) throws Exception {
        String requestHttpMethod = "GET";
        String requestRoute = String.format("/data/%s/daily/from/%s/to/%s",
                requestMeteoParameter.getMeteoId(),
                requestMeteoParameter.getStartTime(),
                requestMeteoParameter.getEndTime());
        HttpGet request = getRequest(requestHttpMethod, requestRoute);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(request);
        System.out.println(response.toString());
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        System.out.println("Response body: " + responseBody);
        JSONObject jsonObject = new JSONObject(responseBody);
        System.out.println(jsonObject);

        return createNormTemperatureResponse(jsonObject, requestMeteoParameter);
    }

    public MeteoParameter getPrecipitationWithNorm(MeteoParameter requestMeteoParameter) throws Exception {
        String requestHttpMethod = "GET";
        String requestRoute = String.format("/data/%s/monthly/from/%s/to/%s",
                requestMeteoParameter.getMeteoId(),
                requestMeteoParameter.getStartTime(),
                requestMeteoParameter.getEndTime());
        HttpGet request = getRequest(requestHttpMethod, requestRoute);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(request);
        System.out.println(response.toString());
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        System.out.println("Response body: " + responseBody);
        JSONObject jsonObject = new JSONObject(responseBody);
        System.out.print("sout jsonobject ");
        System.out.println(jsonObject);
        log.info("1. Running into getPrecipitationWithNorm service before createNormPrecipitationResponse ", jsonObject);

        return createNormPrecipitationResponse(jsonObject, requestMeteoParameter);
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
