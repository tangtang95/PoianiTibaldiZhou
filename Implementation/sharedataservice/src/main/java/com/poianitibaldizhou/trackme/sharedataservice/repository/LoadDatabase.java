package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poianitibaldizhou.trackme.sharedataservice.util.DataWrapper;
import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

@Component
@Slf4j
public class LoadDatabase implements CommandLineRunner{

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setSsn("SSN");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setBirthCity("milan");
        user.setBirthNation("italy");
        user.setBirthDate(Date.valueOf(LocalDate.now()));
        log.info("Preloading: " + user);
        userRepository.save(user);
        HealthData healthData = new HealthData();
        healthData.setId(1L);
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
    }
}
