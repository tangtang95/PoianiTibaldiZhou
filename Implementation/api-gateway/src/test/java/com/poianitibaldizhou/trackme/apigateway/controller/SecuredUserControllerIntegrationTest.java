package com.poianitibaldizhou.trackme.apigateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentSsnException;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentUsernameException;
import com.poianitibaldizhou.trackme.apigateway.exception.SsnNotFoundException;
import com.poianitibaldizhou.trackme.apigateway.repository.UserRepository;
import com.poianitibaldizhou.trackme.apigateway.util.Constants;
import com.poianitibaldizhou.trackme.apigateway.util.ExceptionResponseBody;
import com.poianitibaldizhou.trackme.apigateway.ApiGatewayApplication;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Date;

import static org.junit.Assert.assertEquals;

/**
 * Integration test for the secured controller that manages the user accounts
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiGatewayApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"classpath:IntegrationUserControllerTestData.sql"})
@Transactional
public class SecuredUserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private HttpHeaders httpHeaders = new HttpHeaders();

    private TestRestTemplate restTemplate = new TestRestTemplate();

    /**
     * Test the get of information of a user
     *
     * @throws Exception due to json assert equals
     */
    @Test
    public void testGetUserBySsn() throws JSONException {
        String token = login();

        httpHeaders.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/users/info"),
                HttpMethod.GET, entity, String.class);

        String expectedBody = "{\n" +
                "   \"password\":\"password1\",\n" +
                "   \"username\":\"username1\",\n" +
                "   \"firstName\":\"Frank\",\n" +
                "   \"lastName\":\"Rossi\",\n" +
                "   \"birthDate\":\"1999-01-01\",\n" +
                "   \"birthCity\":\"Verona\",\n" +
                "   \"birthNation\":\"ITALY\",\n" +
                "   \"_links\":{\n" +
                "      \"self\":{\n" +
                "         \"href\":\"http://localhost:"+port+"/users/info\"\n" +
                "      }\n" +
                "   }\n" +
                "}";

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expectedBody, response.getBody(), false);
    }


    // UTILS METHODS

    /**
     * Perform the login and return the token
     *
     * @return token
     */
    private String login() {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/public/users/authenticate?username=username1&password=password1"),
                HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    /**
     * Utility method to form the url with the injected port for a certain uri
     * @param uri uri that will access a certain resource of the application
     * @return url for accesing the resource identified by the uri
     */
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
