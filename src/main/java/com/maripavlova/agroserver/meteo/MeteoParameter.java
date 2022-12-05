package com.maripavlova.agroserver.meteo;

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


}
