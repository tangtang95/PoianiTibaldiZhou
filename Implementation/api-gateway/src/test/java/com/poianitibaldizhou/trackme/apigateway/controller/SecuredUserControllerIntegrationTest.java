package com.poianitibaldizhou.trackme.apigateway.controller;

import com.poianitibaldizhou.trackme.apigateway.ApiGatewayApplication;
import com.poianitibaldizhou.trackme.apigateway.TestUtils;
import com.poianitibaldizhou.trackme.apigateway.repository.UserRepository;
import com.poianitibaldizhou.trackme.apigateway.util.Constants;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

    private RestTemplate restTemplate;

    @Before
    public void setUp() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        restTemplate = TestUtils.getRestTemplate();
    }

    @After
    public void tearDown() {
        restTemplate = null;
    }

    /**
     * Test the get of information of a user
     *
     * @throws Exception due to json assert equals
     */
    @Test
    public void testGetUserBySsn() throws Exception {
        String token = login();

        httpHeaders.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(Constants.SECURED_USER_API + Constants.GET_USER_INFO_API),
                HttpMethod.GET, entity, String.class);

        String expectedBody = "{\n" +
                "   \"username\":\"username1\",\n" +
                "   \"firstName\":\"Frank\",\n" +
                "   \"lastName\":\"Rossi\",\n" +
                "   \"birthDate\":\"1999-01-01\",\n" +
                "   \"birthCity\":\"Verona\",\n" +
                "   \"birthNation\":\"ITALY\",\n" +
                "   \"_links\":{\n" +
                "      \"self\":{\n" +
                "         \"href\":\"https://localhost:"+port+Constants.SECURED_USER_API + Constants.GET_USER_INFO_API+ "\"\n" +
                "      }\n" +
                "   }\n" +
                "}";

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expectedBody, response.getBody(), false);

    }


    /**
     * Test the user logout
     */
    @Test
    public void testUserLogout() {
        String token = login();

        httpHeaders.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(Constants.SECURED_USER_API + Constants.LOGOUT_USER_API),
                HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("true", response.getBody());
    }

    /**
     * Test the logout when the user is not logged
     */
    @Test
    public void testLogoutWhenNotLogged() {
        try {
            httpHeaders.setBearerAuth("fakeToken");
            HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(Constants.SECURED_USER_API + Constants.LOGOUT_USER_API),
                    HttpMethod.GET, entity, String.class);
            fail("Exception expected");
        } catch(HttpClientErrorException e) {
            assertEquals("401 ", e.getMessage());
        }
    }

    /**
     * Test the get of information when the user is not logged
     */
    @Test
    public void testGetWhenNotLogged() {
        try {
            httpHeaders.setBearerAuth("fakeToken");
            HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(Constants.SECURED_USER_API + Constants.GET_USER_INFO_API),
                    HttpMethod.GET, entity, String.class);
            fail("Exception expected");
        } catch(HttpClientErrorException e) {
            assertEquals("401 ", e.getMessage());
        }
    }


    // UTILS METHODS

    /**
     * Perform the login and return the token
     *
     * @return token
     */
    private String login() {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(
                Constants.PUBLIC_USER_API + Constants.LOGIN_USER_API+"?username=username1&password=password1"),
                HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    /**
     * Utility method to form the url with the injected port for a certain uri
     * @param uri uri that will access a certain resource of the application
     * @return url for accesing the resource identified by the uri
     */
    private String createURLWithPort(String uri) {
        return "https://localhost:" + port + uri;
    }

}
