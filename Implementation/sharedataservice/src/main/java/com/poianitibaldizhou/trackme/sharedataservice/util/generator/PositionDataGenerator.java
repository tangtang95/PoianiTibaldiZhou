package com.poianitibaldizhou.trackme.sharedataservice.util.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PositionDataGenerator implements Generator{

    public static void main(String[] args) throws URISyntaxException, IOException {
        PositionDataGenerator userGenerator = new PositionDataGenerator();
        List<Object> positionDataList = userGenerator.generateObjects(10);
        ObjectMapper mapper = new ObjectMapper();
        URL url = userGenerator.getClass().getClassLoader().getResource("./");
        File parentDirectory = new File(new URI(url.toString()));
        File file = new File(parentDirectory, "positionData.json");
        mapper.writeValue(file, positionDataList);
    }

    @Override
    public List<Object> generateObjects(int numberOfObjects) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<User> users = mapper.readValue(ResourceUtils.getFile("classpath:repository/users.json"),
                new TypeReference<List<User>>(){});
        List<Object> positionDataList = new ArrayList<>();
        Faker faker = new Faker();
        for (int i = 0; i < numberOfObjects; i++) {
            User user = users.get(faker.number().numberBetween(0, users.size()-1));
            PositionData positionData = new PositionData();
            positionData.setUser(user);
            Date date = faker.date().between(
                    Date.from(user.getBirthDate().toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC)),
                    Date.from(Instant.now()));
            Timestamp timestamp = Timestamp.from(date.toInstant());
            positionData.setTimestamp(timestamp);
            positionData.setLatitude(faker.number().randomDouble(5, -90, 90));
            positionData.setLongitude(faker.number().randomDouble(5, -90, 90));
            positionDataList.add(positionData);
        }
        return positionDataList;
    }

}
