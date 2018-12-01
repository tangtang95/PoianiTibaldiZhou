package com.poianitibaldizhou.trackme.individualrequestservice;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.UserRepository;
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
    CommandLineRunner initDatabase(IndividualRequestRepository requestRepository, UserRepository userRepository) {
        return args -> {
            userRepository.save(new User("thisisatest"));
            requestRepository.save(
                    new IndividualRequest(
                    new Timestamp(1999, 12, 15, 1, 1, 1, 1),
                    new Date(1998, 1, 1),
                    new Date(2000, 1, 1),
                    new User("thisisatest"),
                    new Long(86)
                    ));
        };
    }

}
