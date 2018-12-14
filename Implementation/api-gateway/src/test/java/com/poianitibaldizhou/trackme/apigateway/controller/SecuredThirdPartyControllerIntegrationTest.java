package com.poianitibaldizhou.trackme.apigateway.controller;

import com.poianitibaldizhou.trackme.apigateway.ApiGatewayApplication;
import com.poianitibaldizhou.trackme.apigateway.TestUtils;
import com.poianitibaldizhou.trackme.apigateway.repository.CompanyDetailRepository;
import com.poianitibaldizhou.trackme.apigateway.repository.PrivateThirdPartyDetailRepository;
import com.poianitibaldizhou.trackme.apigateway.repository.ThirdPartyRepository;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
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
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

/**
 * Integration test for the secured controller that manages the third party accounts
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiGatewayApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"classpath:IntegrationTPControllerTestData"})
@Transactional
public class SecuredThirdPartyControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private CompanyDetailRepository companyDetailRepository;

    @Autowired
    private PrivateThirdPartyDetailRepository privateThirdPartyDetailRepository;

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
     * Test the get of information of a third party that has provided company detail while registering
     *
     * @throws JSONException due to json assert equals method
     */
    @Test
    public void testGetTPWhenCompany() throws JSONException {
        String token = login("tp1@provider.com", "tp1pass");

        httpHeaders.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/thirdparties/info"),
                HttpMethod.GET, entity, String.class);

        String expectedBody = "\n" +
                "{\n" +
                "   \"thirdPartyCustomer\":{\n" +
                "      \"email\":\"tp1@provider.com\",\n" +
                "      \"password\":\"tp1pass\"\n" +
                "   },\n" +
                "   \"companyDetail\":{\n" +
                "      \"thirdPartyCustomer\":{\n" +
                "         \"email\":\"tp1@provider.com\",\n" +
                "         \"password\":\"tp1pass\"\n" +
                "      },\n" +
                "      \"companyName\":\"company1\",\n" +
                "      \"address\":\"address1\",\n" +
                "      \"dunsNumber\":\"555\"\n" +
                "   },\n" +
                "   \"_links\":{\n" +
                "      \"self\":{\n" +
                "         \"href\":\"https://localhost:"+port+"/thirdparties/info\"\n" +
                "      }\n" +
                "   }\n" +
                "}";

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expectedBody, response.getBody(), false);
    }

    /**
     * Test the get of information of a third party that has provided private detail while registering
     *
     * @throws JSONException due to json assert equals method
     */
    @Test
    public void testGetTPWhenPrivate() throws JSONException {
        String token = login("tp3@provider.com", "tp3pass");

        httpHeaders.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/thirdparties/info"),
                HttpMethod.GET, entity, String.class);

        String expectedBody = "\n" +
                "{\n" +
                "   \"thirdPartyCustomer\":{\n" +
                "      \"email\":\"tp3@provider.com\",\n" +
                "      \"password\":\"tp3pass\"\n" +
                "   },\n" +
                "   \"privateThirdPartyDetail\":{\n" +
                "      \"thirdPartyCustomer\":{\n" +
                "         \"email\":\"tp3@provider.com\",\n" +
                "         \"password\":\"tp3pass\"\n" +
                "      },\n" +
                "      \"ssn\":\"tp3ssn\",\n" +
                "      \"name\":\"Jack\",\n" +
                "      \"surname\":\"Jones\",\n" +
                "      \"birthDate\":\"2000-01-01\",\n" +
                "      \"birthCity\":\"Roma\"\n" +
                "   },\n" +
                "   \"_links\":{\n" +
                "      \"self\":{\n" +
                "         \"href\":\"https://localhost:"+port+"/thirdparties/info\"\n" +
                "      }\n" +
                "   }\n" +
                "}";

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expectedBody, response.getBody(), false);
    }

    // UTILS METHOD

    /**
     * Perform the login and return the token
     *
     * @return token
     */
    private String login(String email, String password) {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/public/thirdparties/authenticate?email="+email+"&password="+password),
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
