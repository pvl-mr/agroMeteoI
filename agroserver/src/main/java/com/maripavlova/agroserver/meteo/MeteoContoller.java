package com.maripavlova.agroserver.meteo;


import com.maripavlova.agroserver.meteo.Entities.MeteoParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/parameter")
@Slf4j
@CrossOrigin(origins="http://localhost:3000", allowCredentials = "true")
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



    @PostMapping(path = "/")
    public MeteoParameter getParams(
            @RequestBody MeteoParameter meteoParameter
    ) throws Exception {
        log.info("Running into Controller, ", meteoParameter);
        return meteoService.getParams(meteoParameter);
    }

    @PostMapping(path = "/increased_precipitation")
    public MeteoParameter getIncreasedPrecipitation(
            @RequestBody MeteoParameter meteoParameter
    ) throws Exception {
        log.info("Running into increased_precipitation Controller, ", meteoParameter);
        return meteoService.getIncreasedPrecipitation(meteoParameter);
    }

    @PostMapping(path = "/norm_temperature")
    public MeteoParameter getTemperatureWithNorm(
            @RequestBody MeteoParameter meteoParameter
    ) throws Exception {
        log.info("Running into getTemperatureWithNorm Controller, ", meteoParameter);
        return meteoService.getTemperatureWithNorm(meteoParameter);
    }

    @PostMapping(path = "/norm_precipitation")
    public MeteoParameter getPrecipitationWithNorm(
            @RequestBody MeteoParameter meteoParameter
    ) throws Exception {
        log.info("Running into getPrecipitationWithNorm Controller, ", meteoParameter);
        return meteoService.getPrecipitationWithNorm(meteoParameter);
    }






}
