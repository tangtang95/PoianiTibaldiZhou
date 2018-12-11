package com.poianitibaldizhou.trackme.accountservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poianitibaldizhou.trackme.accountservice.AccountServiceApplication;
import com.poianitibaldizhou.trackme.accountservice.entity.CompanyDetail;
import com.poianitibaldizhou.trackme.accountservice.entity.PrivateThirdPartyDetail;
import com.poianitibaldizhou.trackme.accountservice.entity.ThirdPartyCustomer;
import com.poianitibaldizhou.trackme.accountservice.exception.AlreadyPresentEmailException;
import com.poianitibaldizhou.trackme.accountservice.exception.ThirdPartyCustomerNotFoundException;
import com.poianitibaldizhou.trackme.accountservice.repository.CompanyDetailRepository;
import com.poianitibaldizhou.trackme.accountservice.repository.PrivateThirdPartyDetailRepository;
import com.poianitibaldizhou.trackme.accountservice.repository.ThirdPartyRepository;
import com.poianitibaldizhou.trackme.accountservice.util.Constants;
import com.poianitibaldizhou.trackme.accountservice.util.ExceptionResponseBody;
import com.poianitibaldizhou.trackme.accountservice.util.ThirdPartyCompanyWrapper;
import com.poianitibaldizhou.trackme.accountservice.util.ThirdPartyPrivateWrapper;
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
 * Integration test for the controller that manages the third party accounts
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"classpath:IntegrationTPControllerTestData"})
@Transactional
public class ThirdPartyCustomerManagerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private CompanyDetailRepository companyDetailRepository;

    @Autowired
    private PrivateThirdPartyDetailRepository privateThirdPartyDetailRepository;

    private HttpHeaders httpHeaders = new HttpHeaders();

    private TestRestTemplate restTemplate = new TestRestTemplate();

    /**
     * Test the get of information of a third party that has provided company detail while registering
     *
     * @throws JSONException due to json assert equals method
     */
    @Test
    public void testGetTPWhenCompany() throws JSONException {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/tpaccountservice/thirdparties/tp1@provider.com"),
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
                "         \"href\":\"http://localhost:"+port+"/tpaccountservice/thirdparties/tp1@provider.com\"\n" +
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
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/tpaccountservice/thirdparties/tp3@provider.com"),
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
                "         \"href\":\"http://localhost:"+port+"/tpaccountservice/thirdparties/tp3@provider.com\"\n" +
                "      }\n" +
                "   }\n" +
                "}";

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expectedBody, response.getBody(), false);
    }

    /**
     * Test the get of information of a third party, when no third party with the specified email is present
     */
    @Test
    public void testGetTPWhenNotRegistered() throws IOException {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/tpaccountservice/thirdparties/nonPresentMail@provider.com"),
                HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.toString(), exceptionResponseBody.getError());
        assertEquals(new ThirdPartyCustomerNotFoundException("nonPresentMail@provider.com").getMessage(), exceptionResponseBody.getMessage());
    }

    /**
     * Test the registration of a third party providing company details
     */
    @Test
    public void testCompanyRegisterThirdParty() {
        ThirdPartyCustomer thirdPartyCustomer = new ThirdPartyCustomer();
        thirdPartyCustomer.setEmail("newMail@provider.com");
        thirdPartyCustomer.setPassword("newPassword");

        CompanyDetail companyDetail = new CompanyDetail();
        companyDetail.setThirdPartyCustomer(thirdPartyCustomer);
        companyDetail.setDunsNumber("newDuns");
        companyDetail.setAddress("newAddr");
        companyDetail.setCompanyName("newCompName");

        ThirdPartyCompanyWrapper thirdPartyCompanyWrapper = new ThirdPartyCompanyWrapper();
        thirdPartyCompanyWrapper.setCompanyDetail(companyDetail);
        thirdPartyCompanyWrapper.setThirdPartyCustomer(thirdPartyCustomer);

        HttpEntity<ThirdPartyCompanyWrapper> entity = new HttpEntity<>(thirdPartyCompanyWrapper, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/tpaccountservice/thirdparties/companies"),
                HttpMethod.POST, entity, String.class);

        ThirdPartyCustomer insertedTp = thirdPartyRepository.findByEmail(thirdPartyCustomer.getEmail()).orElseThrow(Error::new);
        CompanyDetail insertedCompanyDetail = companyDetailRepository.findByThirdPartyCustomer(insertedTp).orElseThrow(Error::new);

        assertEquals(thirdPartyCustomer.getEmail(), insertedTp.getEmail());
        assertEquals(thirdPartyCompanyWrapper.getThirdPartyCustomer().getPassword(), insertedTp.getPassword());
        assertEquals(thirdPartyCompanyWrapper.getCompanyDetail().getAddress(), insertedCompanyDetail.getAddress());
        assertEquals(thirdPartyCompanyWrapper.getCompanyDetail().getDunsNumber(), insertedCompanyDetail.getDunsNumber());
        assertEquals(thirdPartyCompanyWrapper.getCompanyDetail().getCompanyName(), insertedCompanyDetail.getCompanyName());
        assertEquals(thirdPartyCompanyWrapper.getCompanyDetail().getThirdPartyCustomer().getEmail(), insertedCompanyDetail.getThirdPartyCustomer().getEmail());
        assertEquals(thirdPartyCompanyWrapper.getCompanyDetail().getThirdPartyCustomer().getPassword(), insertedCompanyDetail.getThirdPartyCustomer().getPassword());
    }

    /**
     * Test the registration of a third party providing company details when a third party customer
     * with the specified email is already registered into the system
     */
    @Test
    public void testCompanyRegisterThirdPartyWhenEmailAlreadyPresent() throws IOException {
        ThirdPartyCustomer thirdPartyCustomer = new ThirdPartyCustomer();
        thirdPartyCustomer.setEmail("tp1@provider.com");
        thirdPartyCustomer.setPassword("newPassword");

        CompanyDetail companyDetail = new CompanyDetail();
        companyDetail.setThirdPartyCustomer(thirdPartyCustomer);
        companyDetail.setDunsNumber("newDuns");
        companyDetail.setAddress("newAddr");
        companyDetail.setCompanyName("newCompName");

        ThirdPartyCompanyWrapper thirdPartyCompanyWrapper = new ThirdPartyCompanyWrapper();
        thirdPartyCompanyWrapper.setCompanyDetail(companyDetail);
        thirdPartyCompanyWrapper.setThirdPartyCustomer(thirdPartyCustomer);

        HttpEntity<ThirdPartyCompanyWrapper> entity = new HttpEntity<>(thirdPartyCompanyWrapper, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/tpaccountservice/thirdparties/companies"),
                HttpMethod.POST, entity, String.class);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.BAD_REQUEST.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), exceptionResponseBody.getError());
        assertEquals(new AlreadyPresentEmailException("tp1@provider.com").getMessage(), exceptionResponseBody.getMessage());
    }

    /**
     * Test the registration of a third party providing private details
     */
    @Test
    public void testPrivateRegisterThirdParty() {
        ThirdPartyCustomer thirdPartyCustomer = new ThirdPartyCustomer();
        thirdPartyCustomer.setEmail("newMail@provider.com");
        thirdPartyCustomer.setPassword("newPassword");

        PrivateThirdPartyDetail privateThirdPartyDetail = new PrivateThirdPartyDetail();
        privateThirdPartyDetail.setThirdPartyCustomer(thirdPartyCustomer);
        privateThirdPartyDetail.setSurname("surname");
        privateThirdPartyDetail.setName("name");
        privateThirdPartyDetail.setSsn("newSsn");
        privateThirdPartyDetail.setBirthCity("Verona");
        privateThirdPartyDetail.setBirthDate(new Date(0));

        ThirdPartyPrivateWrapper thirdPartyPrivateWrapper  = new ThirdPartyPrivateWrapper();
        thirdPartyPrivateWrapper.setPrivateThirdPartyDetail(privateThirdPartyDetail);
        thirdPartyPrivateWrapper.setThirdPartyCustomer(thirdPartyCustomer);

        HttpEntity<ThirdPartyPrivateWrapper> entity = new HttpEntity<>(thirdPartyPrivateWrapper, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/tpaccountservice/thirdparties/privates"),
                HttpMethod.POST, entity, String.class);

        ThirdPartyCustomer insertedTp = thirdPartyRepository.findByEmail(thirdPartyCustomer.getEmail()).orElseThrow(Error::new);
        PrivateThirdPartyDetail insertedPrivateThirdPartyDetail1  = privateThirdPartyDetailRepository.findByThirdPartyCustomer(insertedTp).orElseThrow(Error::new);

        assertEquals(thirdPartyCustomer.getEmail(), insertedTp.getEmail());
        assertEquals(thirdPartyPrivateWrapper.getThirdPartyCustomer().getPassword(), insertedTp.getPassword());
        assertEquals(thirdPartyPrivateWrapper.getPrivateThirdPartyDetail().getBirthDate(), insertedPrivateThirdPartyDetail1.getBirthDate());
        assertEquals(thirdPartyPrivateWrapper.getPrivateThirdPartyDetail().getBirthCity(), insertedPrivateThirdPartyDetail1.getBirthCity());
        assertEquals(thirdPartyPrivateWrapper.getPrivateThirdPartyDetail().getName(), insertedPrivateThirdPartyDetail1.getName());
        assertEquals(thirdPartyPrivateWrapper.getPrivateThirdPartyDetail().getSurname(), insertedPrivateThirdPartyDetail1.getSurname());
        assertEquals(thirdPartyPrivateWrapper.getPrivateThirdPartyDetail().getSsn(), insertedPrivateThirdPartyDetail1.getSsn());
        assertEquals(thirdPartyPrivateWrapper.getPrivateThirdPartyDetail().getThirdPartyCustomer().getEmail(), insertedPrivateThirdPartyDetail1.getThirdPartyCustomer().getEmail());
        assertEquals(thirdPartyPrivateWrapper.getPrivateThirdPartyDetail().getThirdPartyCustomer().getPassword(), insertedPrivateThirdPartyDetail1.getThirdPartyCustomer().getPassword());
    }

    /**
     * Test the registration of a third party providing private details when a third party customer
     * with the specified email is already registered into the system
     */
    @Test
    public void testPrivateRegisterThirdPartyWhenEmailAlreadyPresent() throws IOException {
        ThirdPartyCustomer thirdPartyCustomer = new ThirdPartyCustomer();
        thirdPartyCustomer.setEmail("tp1@provider.com");
        thirdPartyCustomer.setPassword("newPassword");

        PrivateThirdPartyDetail privateThirdPartyDetail = new PrivateThirdPartyDetail();
        privateThirdPartyDetail.setThirdPartyCustomer(thirdPartyCustomer);
        privateThirdPartyDetail.setSurname("surname");
        privateThirdPartyDetail.setName("name");
        privateThirdPartyDetail.setSsn("newSsn");
        privateThirdPartyDetail.setBirthCity("Verona");
        privateThirdPartyDetail.setBirthDate(new Date(0));

        ThirdPartyPrivateWrapper thirdPartyPrivateWrapper  = new ThirdPartyPrivateWrapper();
        thirdPartyPrivateWrapper.setPrivateThirdPartyDetail(privateThirdPartyDetail);
        thirdPartyPrivateWrapper.setThirdPartyCustomer(thirdPartyCustomer);

        HttpEntity<ThirdPartyPrivateWrapper> entity = new HttpEntity<>(thirdPartyPrivateWrapper, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/tpaccountservice/thirdparties/privates"),
                HttpMethod.POST, entity, String.class);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        ExceptionResponseBody exceptionResponseBody = mapper.readValue(response.getBody(), ExceptionResponseBody.class);

        assertEquals(HttpStatus.BAD_REQUEST.value(), exceptionResponseBody.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), exceptionResponseBody.getError());
        assertEquals(new AlreadyPresentEmailException("tp1@provider.com").getMessage(), exceptionResponseBody.getMessage());

    }

    /**
     * Test when not all the parameters required are set
     */
    @Test
    public void testNotAllParameterSpecified() throws IOException {
        ThirdPartyCustomer thirdPartyCustomer = new ThirdPartyCustomer();
        thirdPartyCustomer.setEmail("tp121@provider.com");
        thirdPartyCustomer.setPassword("newPassword");

        PrivateThirdPartyDetail privateThirdPartyDetail = new PrivateThirdPartyDetail();
        privateThirdPartyDetail.setThirdPartyCustomer(new ThirdPartyCustomer());
        privateThirdPartyDetail.setSsn("newSsn");
        privateThirdPartyDetail.setBirthCity("Verona");
        privateThirdPartyDetail.setBirthDate(new Date(0));

        ThirdPartyPrivateWrapper thirdPartyPrivateWrapper  = new ThirdPartyPrivateWrapper();
        thirdPartyPrivateWrapper.setPrivateThirdPartyDetail(privateThirdPartyDetail);
        thirdPartyPrivateWrapper.setThirdPartyCustomer(thirdPartyCustomer);

        HttpEntity<ThirdPartyPrivateWrapper> entity = new HttpEntity<>(thirdPartyPrivateWrapper, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/tpaccountservice/thirdparties/privates"),
                HttpMethod.POST, entity, String.class);



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
