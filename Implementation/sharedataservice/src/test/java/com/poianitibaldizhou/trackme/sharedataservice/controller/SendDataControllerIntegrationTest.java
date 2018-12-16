package com.poianitibaldizhou.trackme.sharedataservice.controller;

import com.jayway.jsonpath.JsonPath;
import com.poianitibaldizhou.trackme.sharedataservice.ShareDataServiceApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShareDataServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(profiles = {"test"})
@Sql("classpath:sql/testSendDataService.sql")
public class SendDataControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate;

    private HttpHeaders httpHeaders;

    @Before
    public void setUp() {
        restTemplate = new TestRestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        httpHeaders.setContentType(MediaTypes.HAL_JSON);
    }

    @After
    public void tearDown() {
        restTemplate = null;
        httpHeaders = null;
    }

    // TEST SEND POSITION DATA METHOD

    /**
     * Test the position data with a request body which is correct and a user ssn which exists
     *
     * @throws Exception due to json assertEquals method
     */
    @Test
    public void sendPositionDataSuccessful() throws Exception {
        httpHeaders.set("userSsn", "user1");

        String requestBody = "{\n" +
                "\t\"timestamp\": 1543422758573,\n" +
                "\t\"latitude\": 20,\n" +
                "\t\"longitude\": 30\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/datacollection/positiondata/user1"),
                entity, String.class);

        String expectedBody = "{\n" +
                "  \"timestamp\": \"2018-11-28T16:32:38.573+0000\",\n" +
                "  \"longitude\": 30.0,\n" +
                "  \"latitude\": 20.0,\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"http://localhost:" + port + "/datacollection/positiondata/user1\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONAssert.assertEquals(expectedBody, response.getBody(), false);
    }

    /**
     Test the position data with a request body which is correct but the user ssn does not exist
     */
    @Test
    public void sendPositionDataNotExistingUser(){
        httpHeaders.set("userSsn", "user3");
        String requestBody = "{\n" +
                "\t\"timestamp\": 1543422758573,\n" +
                "\t\"latitude\": 20,\n" +
                "\t\"longitude\": 30\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/datacollection/positiondata/user3"),
                entity, String.class);

        String expectedBody = "Could not find user: user3";
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        String message = JsonPath.read(response.getBody(), "$.message");
        assertEquals(expectedBody, message);
    }

    /**
     Test the position data with a request body empty
     */
    @Test
    public void sendPositionDataNotExistingUserWithRequestBodyEmpty() {
        httpHeaders.set("userSsn", "user3");
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/datacollection/positiondata/user3"),
                entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // TEST SEND HEALTH DATA METHOD

    /**
     * Test the health data with a request body which is correct and a user ssn which exists
     *
     * @throws Exception due to json assertEquals method
     */
    @Test
    public void sendHealthDataSuccessful() throws Exception {
        httpHeaders.set("userSsn", "user1");
        String requestBody = "{\n" +
                "\t\"timestamp\": 1543422758573,\n" +
                "\t\"heartBeat\": 60,\n" +
                "\t\"bloodOxygenLevel\": 30,\n" +
                "\t\"pressureMin\": 60,\n" +
                "\t\"pressureMax\": 80\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/datacollection/healthdata/user1"),
                entity, String.class);

        String expectedBody = "{\n" +
                "  \"timestamp\": \"2018-11-28T16:32:38.573+0000\",\n" +
                "  \"heartBeat\": 60,\n" +
                "  \"bloodOxygenLevel\": 30,\n" +
                "  \"pressureMin\": 60,\n" +
                "  \"pressureMax\": 80,\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"http://localhost:" + port + "/datacollection/healthdata/user1\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONAssert.assertEquals(expectedBody, response.getBody(), false);
    }

    /**
     Test the health data with a request body which is correct but the user ssn does not exist
     */
    @Test
    public void sendHealthDataNotExistingUser() {
        httpHeaders.set("userSsn", "user3");
        String requestBody = "{\n" +
                "\t\"timestamp\": 1543422758573,\n" +
                "\t\"heartBeat\": 60,\n" +
                "\t\"bloodOxygenLevel\": 30,\n" +
                "\t\"pressureMin\": 60,\n" +
                "\t\"pressureMax\": 80\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/datacollection/healthdata/user3"),
                entity, String.class);

        String expectedBody = "Could not find user: user3";
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        String message = JsonPath.read(response.getBody(), "$.message");
        assertEquals(expectedBody, message);
    }

    /**
     Test the health data with a request body empty
     */
    @Test
    public void sendHealthDataNotExistingUserWithRequestBodyEmpty() {
        httpHeaders.set("userSsn", "user3");
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/datacollection/healthdata/user3"),
                entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // TEST SEND CLUSTER OF DATA METHOD

    /**
     * Test the send cluster of data with a request body which is correct and a user ssn which exists
     */
    @Test
    public void sendClusterDataSuccessful() throws Exception {
        httpHeaders.set("userSsn", "user1");
        String requestBody = "{\n" +
                "\t\"positionDataList\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"timestamp\": 1543422758573,\n" +
                "\t\t\t\"latitude\": 20,\n" +
                "\t\t\t\"longitude\": 30\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"healthDataList\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"timestamp\": 1543422758573,\n" +
                "\t\t\t\"heartBeat\": 60,\n" +
                "\t\t\t\"bloodOxygenLevel\": 30,\n" +
                "\t\t\t\"pressureMin\": 60,\n" +
                "\t\t\t\"pressureMax\": 80\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"timestamp\": 1543422713573,\n" +
                "\t\t\t\"heartBeat\": 20,\n" +
                "\t\t\t\"bloodOxygenLevel\": 30,\n" +
                "\t\t\t\"pressureMin\": 45,\n" +
                "\t\t\t\"pressureMax\": 80\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/datacollection/clusterdata/user1"),
                entity, String.class);

        String expectedBody = "{\n" +
                "  \"positionDataList\": [\n" +
                "    {\n" +
                "      \"timestamp\": \"2018-11-28T16:32:38.573+0000\",\n" +
                "      \"longitude\": 30.0,\n" +
                "      \"latitude\": 20.0,\n" +
                "      \"_links\": {\n" +
                "        \"self\": {\n" +
                "          \"href\": \"http://localhost:" + port +"/datacollection/positiondata/user1\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"healthDataList\": [\n" +
                "    {\n" +
                "      \"timestamp\": \"2018-11-28T16:32:38.573+0000\",\n" +
                "      \"heartBeat\": 60,\n" +
                "      \"bloodOxygenLevel\": 30,\n" +
                "      \"pressureMin\": 60,\n" +
                "      \"pressureMax\": 80,\n" +
                "      \"_links\": {\n" +
                "        \"self\": {\n" +
                "          \"href\": \"http://localhost:" + port + "/datacollection/healthdata/user1\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"timestamp\": \"2018-11-28T16:31:53.573+0000\",\n" +
                "      \"heartBeat\": 20,\n" +
                "      \"bloodOxygenLevel\": 30,\n" +
                "      \"pressureMin\": 45,\n" +
                "      \"pressureMax\": 80,\n" +
                "      \"_links\": {\n" +
                "        \"self\": {\n" +
                "          \"href\": \"http://localhost:" + port + "/datacollection/healthdata/user1\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"http://localhost:" + port + "/datacollection/clusterdata/user1\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONAssert.assertEquals(expectedBody, response.getBody(), false);
    }

    /**
     Test the send cluster of data with a request body which is correct but the user ssn does not exist
     */
    @Test
    public void sendClusterDataNotExistingUser()  {
        httpHeaders.set("userSsn", "user3");
        String requestBody = "{\n" +
                "\t\"positionDataList\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"creationTimestamp\": 1543422758573,\n" +
                "\t\t\t\"latitude\": 20,\n" +
                "\t\t\t\"longitude\": 30\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"healthDataList\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"creationTimestamp\": 1543422758573,\n" +
                "\t\t\t\"heartBeat\": 60,\n" +
                "\t\t\t\"bloodOxygenLevel\": 30,\n" +
                "\t\t\t\"pressureMin\": 60,\n" +
                "\t\t\t\"pressureMax\": 80\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"creationTimestamp\": 1543422713573,\n" +
                "\t\t\t\"heartBeat\": 20,\n" +
                "\t\t\t\"bloodOxygenLevel\": 30,\n" +
                "\t\t\t\"pressureMin\": 45,\n" +
                "\t\t\t\"pressureMax\": 80\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/datacollection/clusterdata/user3"),
                entity, String.class);

        String expectedBody = "Could not find user: user3";
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        String message = JsonPath.read(response.getBody(), "$.message");
        assertEquals(expectedBody, message);
    }

    /**
     Test the send cluster of data with a request body empty
     */
    @Test
    public void sendClusterDataNotExistingUserWithRequestBodyEmpty()  {
        httpHeaders.set("userSsn", "user3");
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/datacollection/clusterdata/user3"),
                entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // UTILITY FUNCTIONS

    /**
     * Utility method to form the url with the injected port for a certain uri
     *
     * @param uri uri that will access a certain resource of the application
     * @return url for accesing the resource identified by the uri
     */
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
