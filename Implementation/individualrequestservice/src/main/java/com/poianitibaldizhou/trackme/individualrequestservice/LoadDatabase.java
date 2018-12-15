package com.poianitibaldizhou.trackme.individualrequestservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class LoadDatabase {
/*
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, IndividualRequestRepository individualRequestRepository) {
        return args -> {
            User user = new User("user1");
            log.info("Preloading: " +userRepository.saveAndFlush(user));

            IndividualRequest individualRequest = new IndividualRequest();
            individualRequest.setMotivation("this is the motivation!");
            individualRequest.setTimestamp(new Timestamp(0));
            individualRequest.setStartDate(new Date(0));
            individualRequest.setEndDate(new Date(0));
            individualRequest.setUser(user);
            individualRequest.setThirdPartyID(1L);
            individualRequest.setStatus(IndividualRequestStatus.PENDING);

            log.info("Preloading " + individualRequestRepository.saveAndFlush(individualRequest));

        };
    }*/
}
