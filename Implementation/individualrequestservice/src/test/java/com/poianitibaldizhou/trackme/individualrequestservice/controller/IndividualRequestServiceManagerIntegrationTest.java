package com.poianitibaldizhou.trackme.individualrequestservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poianitibaldizhou.trackme.individualrequestservice.IndividualRequestServiceApplication;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.IncompatibleDateException;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.RequestNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;
import com.poianitibaldizhou.trackme.individualrequestservice.util.ExceptionResponseBody;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
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

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for the individual request manager service
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IndividualRequestServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@Sql("classpath:ControllerIntegrationTest.sql")
public class IndividualRequestServiceManagerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private IndividualRequestRepository requestRepository;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpHeaders httpHeaders;

    @Before
    public void setUp() {
        httpHeaders = new HttpHeaders();
    }

    @After
    public void tearDown() {
        httpHeaders = null;
    }

    // TEST GET SINGLE REQUEST METHOD


    /**
     * Test the get of a single request with id 1 (this is present, since it is loaded with sql script with the loading
     * of the whole application)
     *
     * @throws Exception due to json assertEquals method
     */
    @Test
    public void testGetSingleRequest() throws Exception {
        httpHeaders.set("ssn", "user1");
        httpHeaders.set("id", "");
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/individualrequestservice/requests/1"),
                HttpMethod.GET, entity, String.class);

        String expectedBody  = "{\n" +
                "  \"status\" : \"PENDING\",\n" +
                "  \"timestamp\" : \"2000-01-01T00:00:00.000+0000\",\n" +
                "  \"startDate\" : \"2000-01-01\",\n" +
                "  \"endDate\" : \"2000-01-01\",\n" +
                "  \"thirdPartyID\" : 1,\n" +
                "  \"_links\" : {\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"http://localhost:"+port+"/individualrequestservice/requests/1\"\n" +
                "    },\n" +
                "    \"thirdPartyRequest\" : {\n" +
                "      \"href\" : \"http://localhost:"+port+"/individualrequestservice/requests/thirdparty/1\"\n" +
                "    },\n" +
                "    \"userPendingRequest\" : {\n" +
                "      \"href\" : \"http://localhost:"+port+"/individualrequestservice/requests/users/user1\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expectedBody, response.getBody(), false);

    }

    /**
     * Test the get of a single request when it is not present
     */
    @Test
    public void testGetSingleRequestWhenNotPresent() throws IOException {
        httpHeaders.set("id", "1");
        httpHeaders.set("ssn", "");
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(createURLWithPort("/individualrequestservice/requests/1000"),
                HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ObjectMapper mapper = new ObjectMapper();

        ExceptionResponseBody exceptionResponseBody = mapper.readValue(responseEntity.getBody(), ExceptionResponseBody.class);
        assertEquals(HttpStatus.NOT_FOUND.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.toString(), exceptionResponseBody.getError());
        assertEquals(new RequestNotFoundException(1000L).getMessage(), exceptionResponseBody.getMessage());
    }

    // TEST GET BY THIRD PARTY ID METHOD

    /**
     * Test the get of all the requests performed by a third party customer when that requests are empty
     *
     * @throws JSONException due to json assertEquals method
     */
    @Test
    public void testGetByThirdPartyIDWhenNoRequestArePresent() throws JSONException {
        httpHeaders.set("id", "1000");
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(createURLWithPort("/individualrequestservice/requests/thirdparty/1000"),
                HttpMethod.GET, entity, String.class);

        String expectedBody = "{\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"http://localhost:"+port+"/individualrequestservice/requests/thirdparty/1000\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        JSONAssert.assertEquals(expectedBody, responseEntity.getBody(), false);
    }

    /**
     * Test the get of all the requests performed by a third party customer when the list of the request is non empty
     *
     * @throws JSONException due to json assertEquals method
     */
    @Test
    public void testGetByThirdPartyID() throws JSONException {
        httpHeaders.set("id", "2");
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(createURLWithPort("/individualrequestservice/requests/thirdparty/2"),
                HttpMethod.GET, entity, String.class);


        String expectedBody = "{\n" +
                "  \"_embedded\": {\n" +
                "    \"individualRequests\": [\n" +
                "      {\n" +
                "        \"status\": \"PENDING\",\n" +
                "        \"timestamp\": \"2000-01-01T00:00:00.000+0000\",\n" +
                "        \"startDate\": \"2000-01-01\",\n" +
                "        \"endDate\": \"2000-01-01\",\n" +
                "        \"thirdPartyID\": 2,\n" +
                "        \"_links\": {\n" +
                "          \"self\": {\n" +
                "            \"href\": \"http://localhost:"+port+"/individualrequestservice/requests/4\"\n" +
                "          },\n" +
                "          \"thirdPartyRequest\": {\n" +
                "            \"href\": \"http://localhost:"+port+"/individualrequestservice/requests/thirdparty/2\"\n" +
                "          },\n" +
                "          \"userPendingRequest\": {\n" +
                "            \"href\": \"http://localhost:"+port+"/individualrequestservice/requests/users/user1\"\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"status\": \"PENDING\",\n" +
                "        \"timestamp\": \"2000-01-01T00:00:00.000+0000\",\n" +
                "        \"startDate\": \"2000-01-01\",\n" +
                "        \"endDate\": \"2000-01-01\",\n" +
                "        \"thirdPartyID\": 2,\n" +
                "        \"_links\": {\n" +
                "          \"self\": {\n" +
                "            \"href\": \"http://localhost:"+port+"/individualrequestservice/requests/5\"\n" +
                "          },\n" +
                "          \"thirdPartyRequest\": {\n" +
                "            \"href\": \"http://localhost:"+port+"/individualrequestservice/requests/thirdparty/2\"\n" +
                "          },\n" +
                "          \"userPendingRequest\": {\n" +
                "            \"href\": \"http://localhost:"+port+"/individualrequestservice/requests/users/user2\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"http://localhost:"+port+"/individualrequestservice/requests/thirdparty/2\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        JSONAssert.assertEquals(expectedBody, responseEntity.getBody(), false);
    }

    // TEST GET PENDING REQUEST OF A CERTAIN USER

    /**
     * Test the get of all the pending request of a certain user that is registered
     *
     * @throws JSONException due to json assertEquals method
     */
    @Test
    public void testGetPendingRequest() throws JSONException {
        httpHeaders.set("ssn", "user2");
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(createURLWithPort("/individualrequestservice/requests/users/user2"),
                HttpMethod.GET, entity, String.class);


        String expectedBody = "{\n" +
                "  \"_embedded\": {\n" +
                "    \"individualRequests\": [\n" +
                "      {\n" +
                "        \"status\": \"PENDING\",\n" +
                "        \"timestamp\": \"2000-01-01T00:00:00.000+0000\",\n" +
                "        \"startDate\": \"2000-01-01\",\n" +
                "        \"endDate\": \"2000-01-01\",\n" +
                "        \"thirdPartyID\": 2,\n" +
                "        \"_links\": {\n" +
                "          \"self\": {\n" +
                "            \"href\": \"http://localhost:"+port+"/individualrequestservice/requests/5\"\n" +
                "          },\n" +
                "          \"thirdPartyRequest\": {\n" +
                "            \"href\": \"http://localhost:"+port+"/individualrequestservice/requests/thirdparty/2\"\n" +
                "          },\n" +
                "          \"userPendingRequest\": {\n" +
                "            \"href\": \"http://localhost:"+port+"/individualrequestservice/requests/users/user2\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"http://localhost:"+port+"/individualrequestservice/requests/users/user2\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        JSONAssert.assertEquals(expectedBody, responseEntity.getBody(), false);
    }

    /**
     * Test the get of all the pending request of a certain user that is not registered
     */
    @Test
    public void testGetPendingRequestWhenUserNotRegistered() throws IOException {
        httpHeaders.set("ssn", "notregistered");
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(createURLWithPort("/individualrequestservice/requests/users/notregistered"),
                HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(responseEntity.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.toString(), exceptionResponseBody.getError());
        assertEquals(new UserNotFoundException(new User("notregistered")).getMessage(), exceptionResponseBody.getMessage());
    }

    // TEST THE ADD OF A NEW REQUEST

    /**
     * Test the add of a new individual request when not all the necessary parameters have been specified
     */
    @Test
    public void testAddRequestWrongParameters() throws IOException {
        httpHeaders.set("id", "1");
        IndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setThirdPartyID(1L);
        individualRequest.setEndDate(new Date(0));
        HttpEntity<IndividualRequest> entity = new HttpEntity<>(individualRequest, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/individualrequestservice/requests/user1"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();

        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), exceptionResponseBody.getError());
        assertEquals(Constants.INVALID_OPERATION, exceptionResponseBody.getMessage());
    }

    /**
     * Test the add of a new individual request on an existing user that is non blocked
     *
     * @throws Exception unsuccessful insertion of the new request
     */
    @Test
    public void testAddRequest() throws Exception {
        httpHeaders.set("id", "1");
        IndividualRequest individualRequest = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user1"), 1L);
        HttpEntity<IndividualRequest> entity = new HttpEntity<>(individualRequest, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/individualrequestservice/requests/user1"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

        Pattern p = Pattern.compile("[0-9]+$");
        Matcher m = p.matcher(actual);
        String requestId = "";
        if(m.find()) {
            requestId = m.group();
        }

        assertTrue(requestRepository.findById(Long.parseLong(requestId)).isPresent());
        assertTrue(requestRepository.findById(Long.parseLong(requestId)).
                orElseThrow(Exception::new).getStatus().equals(IndividualRequestStatus.PENDING));
        assertTrue(requestRepository.findById(Long.parseLong(requestId)).
                orElseThrow(Exception::new).getUser().getSsn().equals(individualRequest.getUser().getSsn()));
        assertTrue(requestRepository.findById(Long.parseLong(requestId)).
                orElseThrow(Exception::new).getStartDate().equals(individualRequest.getStartDate()));
        assertTrue(requestRepository.findById(Long.parseLong(requestId)).
                orElseThrow(Exception::new).getStartDate().equals(individualRequest.getEndDate()));
    }

    /**
     * Test the add of a new request when the specified dates are not compatible
     */
    @Test
    public void testAddRequestWhenIncompatibleDates() throws IOException {
        httpHeaders.set("id", "1");
        IndividualRequest individualRequest = new IndividualRequest(new Timestamp(0), new Date(100), new Date(0),
                new User("user1"), 1L);
        HttpEntity<IndividualRequest> entity = new HttpEntity<>(individualRequest, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/individualrequestservice/requests/user1"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();

        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), exceptionResponseBody.getError());
        assertEquals(new IncompatibleDateException().getMessage(), exceptionResponseBody.getMessage());
    }

    /**
     * Test the add of a request on a user that is not registered
     */
    @Test
    public void testAddRequestOnNonRegisteredUser() {
        httpHeaders.set("id", "1");
        IndividualRequest individualRequest = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0),
                new User("nonRegisteredUser"), 1L);
        HttpEntity<IndividualRequest> entity = new HttpEntity<>(individualRequest, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/individualrequestservice/requests/nonRegisteredUser"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test the add of a request on a user that is registered but that has already blocked the requesting third party
     *
     * @throws Exception unsuccessful insertion of the new request
     */
    @Test
    public void testAddRequestWhenBlocked() throws Exception {
        httpHeaders.set("id", "4");
        IndividualRequest individualRequest = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user5"), 4L);
        HttpEntity<IndividualRequest> entity = new HttpEntity<>(individualRequest, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/individualrequestservice/requests/user5"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

        Pattern p = Pattern.compile("[0-9]+$");
        Matcher m = p.matcher(actual);
        String requestId = "";
        if(m.find()) {
            requestId = m.group();
        }

        assertTrue(requestRepository.findById(Long.parseLong(requestId)).isPresent());
        assertTrue(requestRepository.findById(Long.parseLong(requestId)).
                orElseThrow(Exception::new).getStatus().equals(IndividualRequestStatus.REFUSED));
        assertTrue(requestRepository.findById(Long.parseLong(requestId)).
                orElseThrow(Exception::new).getUser().getSsn().equals(individualRequest.getUser().getSsn()));
        assertTrue(requestRepository.findById(Long.parseLong(requestId)).
                orElseThrow(Exception::new).getStartDate().equals(individualRequest.getStartDate()));
        assertTrue(requestRepository.findById(Long.parseLong(requestId)).
                orElseThrow(Exception::new).getStartDate().equals(individualRequest.getEndDate()));

    }

    // UTILITY FUNCTIONS

    /**
     * Utility method to form the url with the injected port for a certain uri
     * @param uri uri that will access a certain resource of the application
     * @return url for accesing the resource identified by the uri
     */
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
