package com.maripavlova.agroserver.meteo.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PrecipitationUtils {
    public static Map<String, Float> readFromJsonPrecipitation() throws IOException {
        return calculateAverageFromJson();
    }

    public static Map<String, Float> calculateAverageFromJson() throws IOException {
        String filePathString = "month_year_precipitations.json";
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Float> result = new HashMap<>();
        HashMap<String, Integer[]> precipitationValues = objectMapper.readValue(new File(filePathString), new TypeReference<HashMap<String, Integer[]>>() {});
        for (int i = 0; i < precipitationValues.size()-2; i++) {
            Float sum = 0F;
            for (int j = 0; j < precipitationValues.get("years").length; j++) {
                sum += precipitationValues.get(String.valueOf(i+1))[j];
            }
            double res = Math.round(sum/precipitationValues.get("years").length * 100.0) / 100.0;
            result.put(String.valueOf(i), (float) res);
        }

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(result);

        String path = "calculated_averages_precipitation.json";
        Files.write( Paths.get(path), json.getBytes());
        return result;

    }

}
