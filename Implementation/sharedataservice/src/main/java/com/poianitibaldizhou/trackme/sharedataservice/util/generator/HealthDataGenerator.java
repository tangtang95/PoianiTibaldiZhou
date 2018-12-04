package com.poianitibaldizhou.trackme.sharedataservice.util.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class HealthDataGenerator implements Generator {

    public static void main(String[] args) throws URISyntaxException, IOException {
        HealthDataGenerator userGenerator = new HealthDataGenerator();
        List<Object> healthDataList = userGenerator.generateObjects(10);
        ObjectMapper mapper = new ObjectMapper();
        URL url = userGenerator.getClass().getClassLoader().getResource("./");
        File parentDirectory = new File(new URI(url.toString()));
        File file = new File(parentDirectory, "healthData.json");
        mapper.writeValue(file, healthDataList);
    }

    /**
     *
     *
     * @param numberOfObjects
     * @return
     * @throws IOException
     */
    @Override
    public List<Object> generateObjects(int numberOfObjects) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<User> users = mapper.readValue(ResourceUtils.getFile("classpath:repository/users.json"), List.class);
        List<Object> healthDataList = new ArrayList<>();
        Faker faker = new Faker();
        for (int i = 0; i < numberOfObjects; i++) {
            User user = users.get(faker.number().numberBetween(0, users.size()-1));
            HealthData healthData = new HealthData();
            healthData.setUser(user);
            Timestamp timestamp = Timestamp.from(faker.date().between(
                    Date.from(user.getBirthDate().toInstant()),
                    Date.from(Instant.now())).toInstant());
            healthData.setTimestamp(timestamp);
            healthData.setHeartBeat(faker.number().numberBetween(40, 200));
            healthData.setPressureMin(faker.number().numberBetween(60, 130));
            healthData.setPressureMax(faker.number().numberBetween(100, 180));
            healthData.setBloodOxygenLevel(faker.number().numberBetween(50, 125));
            healthDataList.add(healthData);
        }
        return healthDataList;
    }

}
