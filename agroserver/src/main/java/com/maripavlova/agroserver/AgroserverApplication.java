package com.maripavlova.agroserver;

import org.joda.time.DateTimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import org.joda.time.DateTime;

@SpringBootApplication
@RestController
@EnableCaching
public class AgroserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgroserverApplication.class, args);
	}


}
