package com.poianitibaldizhou.trackme.apigateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poianitibaldizhou.trackme.apigateway.ApiGatewayApplication;
import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentSsnException;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentUsernameException;
import com.poianitibaldizhou.trackme.apigateway.repository.UserRepository;
import com.poianitibaldizhou.trackme.apigateway.util.Constants;
import com.poianitibaldizhou.trackme.apigateway.util.ExceptionResponseBody;
import org.junit.Test;
import org.junit.runner.RunWith;
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
 * Integration test for the public controller that manages the user accounts
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiGatewayApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"classpath:IntegrationUserControllerTestData.sql"})
@Transactional
public class PublicUserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private HttpHeaders httpHeaders = new HttpHeaders();

    private TestRestTemplate restTemplate = new TestRestTemplate();

    /**
     * Test the registration of a user
     *
     * @throws Exception user already present in the data set
     */
    @Test
    public void testRegisterUser() throws Exception {
        User user = new User();
        user.setUsername("newUserName");
        user.setPassword("xcasggv");
        user.setLastName("Tiba");
        user.setFirstName("Mattia");
        user.setBirthDate(new Date(0));
        user.setBirthCity("Palermo");
        user.setBirthNation("Italia");

        HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/public/users/newSsn"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        User insertedUser = userRepository.findById("newSsn").orElseThrow(Exception::new);
        assertEquals(user.getUsername(), insertedUser.getUsername());
        assertEquals(user.getLastName(), insertedUser.getLastName());
        assertEquals(user.getFirstName(), insertedUser.getFirstName());
        assertEquals(user.getBirthCity(), insertedUser.getBirthCity());
        assertEquals(user.getBirthNation(), insertedUser.getBirthNation());
        assertEquals(user.getPassword(), insertedUser.getPassword());
        assertEquals(user.getBirthDate(), insertedUser.getBirthDate());
    }

    /**
     * Test the registration of a user when a user with the specified username is already present
     */
    @Test
    public void testRegisterUserWhenUserNameAlreadyPresent() throws IOException {
        User user = new User();
        user.setUsername("username1");
        user.setPassword("xcasggv");
        user.setLastName("Tiba");
        user.setFirstName("Mattia");
        user.setBirthDate(new Date(0));
        user.setBirthCity("Palermo");
        user.setBirthNation("Italia");

        HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/public/users/newSsn"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.BAD_REQUEST.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), exceptionResponseBody.getError());
        assertEquals(new AlreadyPresentUsernameException("username1").getMessage(), exceptionResponseBody.getMessage());
    }

    /**
     * Test the registration of a user when a user with the specified ssn is already present
     */
    @Test
    public void testRegisterUserWhenSsnAlreadyPresent() throws IOException {
        User user = new User();
        user.setUsername("newUsername");
        user.setPassword("xcasggv");
        user.setLastName("Tiba");
        user.setFirstName("Mattia");
        user.setBirthDate(new Date(0));
        user.setBirthCity("Palermo");
        user.setBirthNation("Italia");

        HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/public/users/user1"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.BAD_REQUEST.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), exceptionResponseBody.getError());
        assertEquals(new AlreadyPresentSsnException("user1").getMessage(), exceptionResponseBody.getMessage());
    }

    /**
     * Test the registration of a user when not all of the necessary and mandatory parameters has been specified
     */
    @Test
    public void testRegisterUserWrongParameters() throws IOException {
        User user = new User();
        user.setUsername("newUsername");
        user.setPassword("xcasggv");
        user.setLastName("Tiba");
        user.setFirstName("Mattia");
        user.setBirthNation("Italia");

        HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/public/users/user100"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.BAD_REQUEST.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), exceptionResponseBody.getError());
        assertEquals(Constants.INVALID_OPERATION, exceptionResponseBody.getMessage());
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
