package com.bat.petra.photorecognition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EinsteinApplication {
	public static void main(String[] args) {
		SpringApplication.run(EinsteinApplication.class, args);
	}
}