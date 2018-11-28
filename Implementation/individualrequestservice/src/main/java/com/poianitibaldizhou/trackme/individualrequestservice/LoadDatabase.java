package com.poianitibaldizhou.trackme.individualrequestservice;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Date;
import java.sql.Timestamp;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(IndividualRequestRepository requestRepository) {
        return args -> {
            System.out.println("Preloading: " + requestRepository.save(
                    new IndividualRequest(
                    new Timestamp(1999, 12, 15, 1, 1, 1, 1),
                    new Date(1998, 1, 1),
                    new Date(2000, 1, 1),
                    "thisisatest",
                    new Long(86)
                    )));
        };
    }

}
