package com.maripavlova.agroserver;

import org.joda.time.DateTimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
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
//
//	@GetMapping("/getting")
//	public String getStudents() {
//		return "getting data...";
//	}
//
//	@GetMapping("/callgetting")
//	public String call() {
//		String uri = "http://localhost:8080/getting";
//		RestTemplate restTemplate = new RestTemplate();
//		String result = restTemplate.getForObject(uri, String.class);
//		return result;
//	}
//
//	@GetMapping("/api")
//	public DateTime getCountries() {
//		String url = "https://rescountries.eu/rest/v2/all";
//		RestTemplate restTemplate = new RestTemplate();
//		Object[] countries = restTemplate.getForObject(url, Object[].class);
//		DateTime now = DateTime.now( DateTimeZone.UTC );
//		System.out.println(now);
//
//
//		return now;

//	}



}
