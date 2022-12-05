package com.maripavlova.agroserver.meteo;


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
@RequestMapping(path = "/temperature")
public class MeteoContoller {
    private final MeteoService meteoService;

    @Autowired
    public MeteoContoller(MeteoService meteoService){
        this.meteoService = meteoService;
    }

    @GetMapping
    public MeteoParameter getParameter() throws Exception {
        return meteoService.getTemperatures2();
    }
}
