package com.ashutosh.pincodeDistance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PincodeDistanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PincodeDistanceApplication.class, args);
	}

}
