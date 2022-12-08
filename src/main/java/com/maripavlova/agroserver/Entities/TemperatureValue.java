package com.maripavlova.agroserver.Entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TemperatureValue {
    private String date;
    private Float avgTemperature;
    private Float maxTemperature;
    private Float minTemperature;
    private String atmPressure;
    private Float windSpeed;
    private Float precipitation;
    private Float effectiveTemperature;
}