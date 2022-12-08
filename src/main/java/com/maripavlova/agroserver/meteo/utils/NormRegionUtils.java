package com.maripavlova.agroserver.meteo.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.maripavlova.agroserver.Entities.TemperatureValue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class NormRegionUtils {
    public static Map<String, Float> readFromJson() throws IOException {
        String filePathString = "calculated_averages.json";
        File f = new File(filePathString);
        if(f.exists() && !f.isDirectory()) {
            System.out.println("intooooooooooooooooooooooooooooooo");
            return getAveragesFromJson();
        } else {
            return calculateAveragesFrom3Years();
        }
    }

    public static Map<String, Float> getAveragesFromJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Float> temperatureValuesAverages = objectMapper.readValue(new File("./calculated_averages.json"), new TypeReference<HashMap<String, Float>>() {});
        return temperatureValuesAverages;
    }

    public static Map<String, Float> calculateAveragesFrom3Years() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<TemperatureValue> temperatureValues2019 = objectMapper.readValue(new File("./historic_temperature_2019.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, TemperatureValue.class));
        List<TemperatureValue> temperatureValues2020 = objectMapper.readValue(new File("./historic_temperature_2020.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, TemperatureValue.class));
        List<TemperatureValue> temperatureValues2021 = objectMapper.readValue(new File("./historic_temperature_2021.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, TemperatureValue.class));


        LinkedHashMap<String, List<Float>> averageTemperatures = new LinkedHashMap<String, List<Float>>();
        for (int i = 0; i < temperatureValues2020.size(); i++) {
            String dayMonth = temperatureValues2020.get(i).getDate().substring(0, 5);
            averageTemperatures.put(dayMonth, new ArrayList<>());
        }

        averageTemperatures = fullMap(averageTemperatures, temperatureValues2019);
        averageTemperatures = fullMap(averageTemperatures, temperatureValues2020);
        averageTemperatures = fullMap(averageTemperatures, temperatureValues2021);

        Map<String, Float> finalAverageTemperatures = getAverageTemperatures(averageTemperatures);

        return finalAverageTemperatures;

    }

    public static LinkedHashMap<String, List<Float>> fullMap(LinkedHashMap<String, List<Float>> partingMap, List<TemperatureValue> temperatureValues) {
        for (int i = 0; i < temperatureValues.size(); i++) {
            String key = temperatureValues.get(i).getDate().substring(0, 5);
            Float avgTemperature = temperatureValues.get(i).getAvgTemperature();
            if (partingMap.containsKey(key)) {
                partingMap.get(key).add(avgTemperature);
            }
        }
        return partingMap;
    }

    public static Map<String, Float> getAverageTemperatures(LinkedHashMap<String, List<Float>> fullMap) throws IOException {
        LinkedHashMap<String, Float> averages = new LinkedHashMap<>();

        for(Map.Entry<String, List<Float>> entry : fullMap.entrySet()) {
            String key = entry.getKey();
            List<Float> values = entry.getValue();
            Float sum = 0F;
            for (Float value: values) {
                sum += value;
            }
            double result = Math.round(sum/ values.size() * 100.0) / 100.0;
            averages.put(key, (float) result);
        }

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(averages);

        String path = "calculated_averages.json";
        Files.write( Paths.get(path), json.getBytes());

        return averages;

    }

}
