package com.poianitibaldizhou.trackme.accountservice.controller;

import com.poianitibaldizhou.trackme.accountservice.AccountServiceApplication;
import com.poianitibaldizhou.trackme.accountservice.entity.User;
import com.poianitibaldizhou.trackme.accountservice.repository.UserRepository;
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

import java.sql.Date;

import static org.junit.Assert.assertEquals;

/**
 * Integration test for the controller that manages the user accounts
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"classpath:IntegrationUserControllerTestData.sql"})
@Transactional
public class UserAccountManagerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private HttpHeaders httpHeaders = new HttpHeaders();

    private TestRestTemplate restTemplate = new TestRestTemplate();

    /**
     * Test the get of a user by means of his ssn
     *
     * @throws Exception due to json assert equals
     */
    @Test
    public void testGetUserBySsn() throws JSONException {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/useraccountservice/users/user1"),
                HttpMethod.GET, entity, String.class);

        String expectedBody = "{\n" +
                "   \"password\":\"password1\",\n" +
                "   \"userName\":\"username1\",\n" +
                "   \"firstName\":\"Frank\",\n" +
                "   \"lastName\":\"Rossi\",\n" +
                "   \"birthDate\":\"1999-01-01\",\n" +
                "   \"birthCity\":\"Verona\",\n" +
                "   \"birthNation\":\"ITALY\",\n" +
                "   \"_links\":{\n" +
                "      \"self\":{\n" +
                "         \"href\":\"http://localhost:"+port+"/useraccountservice/users/user1\"\n" +
                "      }\n" +
                "   }\n" +
                "}";

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expectedBody, response.getBody(), false);
    }

    /**
     * Test get of a user by means of his ssn when no user with that ssn is registered
     */
    @Test
    public void testGetUserBySsnWhenNotPresent() {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/useraccountservice/users/nonPresentSsn"),
                HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test the registration of a user
     *
     * @throws Exception user already present in the data set
     */
    @Test
    public void testRegisterUser() throws Exception {
        User user = new User();
        user.setUserName("newUserName");
        user.setPassword("xcasggv");
        user.setLastName("Tiba");
        user.setFirstName("Mattia");
        user.setBirthDate(new Date(0));
        user.setBirthCity("Palermo");
        user.setBirthNation("Italia");

        HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/useraccountservice/users/newSsn"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        User insertedUser = userRepository.findById("newSsn").orElseThrow(Exception::new);
        assertEquals(user.getUserName(), insertedUser.getUserName());
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
    public void testRegisterUserWhenUserNameAlreadyPresent() {
        User user = new User();
        user.setUserName("username1");
        user.setPassword("xcasggv");
        user.setLastName("Tiba");
        user.setFirstName("Mattia");
        user.setBirthDate(new Date(0));
        user.setBirthCity("Palermo");
        user.setBirthNation("Italia");

        HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/useraccountservice/users/newSsn"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test the registration of a user when a user with the specified ssn is already present
     */
    @Test
    public void testRegisterUserWhenSsnAlreadyPresent() {
        User user = new User();
        user.setUserName("newUsername");
        user.setPassword("xcasggv");
        user.setLastName("Tiba");
        user.setFirstName("Mattia");
        user.setBirthDate(new Date(0));
        user.setBirthCity("Palermo");
        user.setBirthNation("Italia");

        HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/useraccountservice/users/user1"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
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
