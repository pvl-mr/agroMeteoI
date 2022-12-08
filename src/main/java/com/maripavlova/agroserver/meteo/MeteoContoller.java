package com.maripavlova.agroserver.meteo;


import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/parameter")
@Slf4j
public class MeteoContoller {
    private final MeteoService meteoService;

    @Autowired
    public MeteoContoller(MeteoService meteoService){
        this.meteoService = meteoService;
    }

//    @GetMapping(path = "/temperature")
//    public MeteoParameter getTemperatures() throws Exception {
//        return meteoService.getTemperatures();
//    }
//
//    @GetMapping(path = "/windspeed")
//    public MeteoParameter getWindSpeeds() throws Exception {
//        return meteoService.getWindSpeeds();
//    }
//
//    @GetMapping(path = "/humidity")
//    public MeteoParameter getHumidities() throws Exception {
//        return meteoService.getHumidities();
//    }
//
//    @GetMapping(path = "/precipitation")
//    public MeteoParameter getPrecipitations() throws Exception {
//        return meteoService.getPrecipitations();
//    }



    @GetMapping(path = "/test")
    public MeteoParameter getParams(
            @RequestBody MeteoParameter meteoParameter
    ) throws Exception {
        log.info("Running into Controller, ", meteoParameter);
        return meteoService.getParams(meteoParameter);
    }

    @GetMapping(path = "/increased_precipitation")
    public MeteoParameter getIncreasedPrecipitation(
            @RequestBody MeteoParameter meteoParameter
    ) throws Exception {
        log.info("Running into Controller, ", meteoParameter);
        return meteoService.getIncreasedPrecipitation(meteoParameter);
    }

    @GetMapping(path = "/norm")
    public MeteoParameter getTemperatureWithNorm(
            @RequestBody MeteoParameter meteoParameter
    ) throws Exception {
        log.info("Running into Controller, ", meteoParameter);
        return meteoService.getTemperatureWithNorm(meteoParameter);
    }




}
