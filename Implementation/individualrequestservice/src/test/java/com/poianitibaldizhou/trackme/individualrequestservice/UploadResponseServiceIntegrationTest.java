package com.poianitibaldizhou.trackme.individualrequestservice;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.BlockedThirdPartyKey;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.*;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.BlockedThirdPartyRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.ResponseRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import com.poianitibaldizhou.trackme.individualrequestservice.util.ResponseType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Integration test for the upload request service
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IndividualRequestServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class UploadResponseServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private IndividualRequestRepository requestRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private BlockedThirdPartyRepository blockedThirdPartyRepository;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders httpHeaders = new HttpHeaders();

    // TEST ADD RESPONSE METHOD

    /**
     * Test the add of an acceptance response to an individual request
     * @throws Exception unsuccessful insertion of the new response
     */
    @Test
    public void testAddAcceptResponse() throws Exception {
        String responseType = ResponseType.ACCEPT.toString();

        HttpEntity<String> entity = new HttpEntity<>(responseType, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/uploadresponseservice/response/user1/1"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(responseRepository.findById(1L).isPresent());
        assertTrue(responseRepository.findById(1L).orElseThrow(Exception::new).getResponse().equals(ResponseType.ACCEPT));
        assertTrue(requestRepository.findById(1L).orElseThrow(Exception::new).getStatus().equals(IndividualRequestStatus.ACCEPTED_UNDER_ANALYSIS));
    }

    /**
     * Test the add of a refusing response to an individual request
     * @throws Exception unsuccessful insertion of the new response
     */
    @Test
    public void testAddRefuseResponse() throws Exception {
        String responseType = ResponseType.REFUSE.toString();

        HttpEntity<String> entity = new HttpEntity<>(responseType, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/uploadresponseservice/response/user1/1"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(responseRepository.findById(1L).isPresent());
        assertTrue(responseRepository.findById(1L).orElseThrow(Exception::new).getResponse().equals(ResponseType.REFUSE));
        assertTrue(requestRepository.findById(1L).orElseThrow(Exception::new).getStatus().equals(IndividualRequestStatus.REFUSED));
    }

    /**
     * Test the add of a response to a non existing individual request
     */
    @Test
    public void testAddResponseOfNonExistingRequest() {
        String responseType = ResponseType.ACCEPT.toString();

        HttpEntity<String> entity = new HttpEntity<>(responseType, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/uploadresponseservice/response/user1/100"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new RequestNotFoundException(100L).getMessage(), response.getBody());
    }

    /**
     * Test the add of a response to an individual request when the user accessing the service is not the one
     * that is related with the request
     *
     * @throws Exception when the mocked sql insertion are wrong
     */
    @Test
    public void testAddResponseWhenNonMatchingUser() throws Exception {
        String responseType = ResponseType.ACCEPT.toString();

        HttpEntity<String> entity = new HttpEntity<>(responseType, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/uploadresponseservice/response/user2/1"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new NonMatchingUserException("user2").getMessage(), response.getBody());

    }

    /**
     * Test the add of a response when it is performed by a non registered suer
     */
    @Test
    public void testAddResponseWhenTheUserIsNotRegistered() {
        String responseType = ResponseType.ACCEPT.toString();

        HttpEntity<String> entity = new HttpEntity<>(responseType, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/uploadresponseservice/response/nonRegisteredUser/100"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new UserNotFoundException(new User("nonRegisteredUser")).getMessage(), response.getBody());
    }


    // TEST BLOCK THIRD PARTY METHOD

    /**
     * Test the add of a block by a certain user for a certain third party customer, when the user is not
     * registered
     */
    @Test
    public void testAddBlockWhenUserNotFound() {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/uploadresponseservice/blockedThirdParty/nonRegisteredUser/100"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new UserNotFoundException(new User("nonRegisteredUser")).getMessage(), response.getBody());
    }

    /**
     * Test the add of a block by a certain user for a certain third party customer, when the third party customer
     * never sent a request toward that user
     */
    @Test
    public void testAddBlockWhenNoRequest() {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/uploadresponseservice/blockedThirdParty/user1/10"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new ThirdPartyNotFoundException(10L).getMessage(), response.getBody());
    }

    /**
     * Test the add of a block by a certain user for a certain third party customer, when that user has already
     * blocked the specified third party customer
     */
    @Test
    public void testAddBlockWhenBlockAlreadyPresent() {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/uploadresponseservice/blockedThirdParty/user5/4"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new BlockAlreadyPerformedException(4L).getMessage(), response.getBody());
    }

    /**
     * Test the add of a block by a certian user for a certain third party customer
     */
    @Test
    public void testAddBlock() {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/uploadresponseservice/blockedThirdParty/user1/1"),
                HttpMethod.POST, entity, String.class);

        System.out.println("RESPONSE : " + response);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        BlockedThirdPartyKey newItemKey = new BlockedThirdPartyKey(1L, new User("user1"));
        assertTrue(blockedThirdPartyRepository.findById(newItemKey).isPresent());

        requestRepository.flush();
        requestRepository.findAllByThirdPartyID(1L).forEach(individualRequest -> assertEquals(IndividualRequestStatus.REFUSED,
                individualRequest.getStatus()));
    }


    // UTIL METHOD

    /**
     * Utility method to form the url with the injected port for a certain uri
     * @param uri uri that will access a certain resource of the application
     * @return url for accesing the resource identified by the uri
     */
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
