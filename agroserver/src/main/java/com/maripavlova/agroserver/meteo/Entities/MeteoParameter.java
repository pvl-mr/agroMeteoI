package com.maripavlova.agroserver.meteo.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MeteoParameter implements Serializable {

    private String meteoId;
    private String parameterName;
    private String startTime;
    private String endTime;
    private List<String> dates;
    private ValueForm values;
    private ValueForm normValues;

    public MeteoParameter(String meteoId, String parameterName, String startTime, String endTime) {
        this.meteoId = meteoId;
        this.parameterName = parameterName;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
