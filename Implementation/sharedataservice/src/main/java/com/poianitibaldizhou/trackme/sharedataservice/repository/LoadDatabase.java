package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poianitibaldizhou.trackme.sharedataservice.entity.DataWrapper;
import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    public CommandLineRunner initUserDatabase(UserRepository userRepository){
        return args -> {
            User user = new User();
            user.setSsn("SSN");
            user.setFirstName("firstName");
            user.setLastName("lastName");
            user.setAge(30);
            user.setBirthDate(Date.valueOf(LocalDate.now()));
            log.info("Preloading: " + user);
            userRepository.save(user);
            HealthData healthData = new HealthData();
            healthData.setId(1l);
            healthData.setUser(user);
            healthData.setHeartBeat(60);
            healthData.setTimestamp(Timestamp.from(Instant.now()));
            healthData.setPressureMax(80);
            healthData.setPressureMin(60);
            healthData.setBloodOxygenLevel(30);
            ObjectMapper mapper = new ObjectMapper();
            DataWrapper dataWrapper = new DataWrapper();
            dataWrapper.addHealthData(healthData);
            log.info(mapper.writeValueAsString(dataWrapper));
        };
    }

}
