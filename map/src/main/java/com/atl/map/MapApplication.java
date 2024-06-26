package com.atl.map;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class MapApplication {

	@PostConstruct
    public void started() {
      // timezone UTC 셋팅
      TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
	
	public static void main(String[] args) {
		SpringApplication.run(MapApplication.class, args);
	}

}
