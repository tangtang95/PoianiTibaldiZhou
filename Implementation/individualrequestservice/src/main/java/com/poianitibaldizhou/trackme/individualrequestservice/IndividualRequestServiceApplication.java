package com.poianitibaldizhou.trackme.individualrequestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.poianitibaldizhou.trackme.individualrequestservice.entity"})
public class IndividualRequestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IndividualRequestServiceApplication.class, args);
	}
}
