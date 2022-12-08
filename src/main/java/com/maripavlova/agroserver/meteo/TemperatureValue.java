package com.maripavlova.agroserver.meteo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TemperatureValue {
    private String date;
    private Float avgTemperature;
}
