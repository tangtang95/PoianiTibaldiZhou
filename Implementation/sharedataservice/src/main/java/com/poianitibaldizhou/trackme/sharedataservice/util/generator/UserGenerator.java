package com.poianitibaldizhou.trackme.sharedataservice.util.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Date;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class UserGenerator implements Generator{

    public static void main(String[] args) throws IOException, URISyntaxException {
        UserGenerator userGenerator = new UserGenerator();
        List<Object> users = userGenerator.generateObjects(10);
        ObjectMapper mapper = new ObjectMapper();
        URL url = userGenerator.getClass().getClassLoader().getResource("./");
        File parentDirectory = new File(new URI(url.toString()));
        File file = new File(parentDirectory, "users.json");
        mapper.writeValue(file, users);
    }

    @Override
    public List<Object> generateObjects(int numberOfObjects) {
        List<Object> users = new ArrayList<>();
        Faker faker = new Faker();
        for (int i = 0; i < numberOfObjects; i++) {
            User user = new User();
            user.setSsn(String.format("%016d", i));

            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());

            user.setBirthDate(Date.valueOf(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            user.setBirthCity(faker.address().cityName());
            user.setBirthNation(faker.address().country());
            users.add(user);
        }
        return users;
    }
}
