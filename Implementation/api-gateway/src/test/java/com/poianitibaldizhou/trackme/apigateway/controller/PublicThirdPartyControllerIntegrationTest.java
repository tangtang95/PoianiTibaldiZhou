package com.poianitibaldizhou.trackme.apigateway.controller;

import com.poianitibaldizhou.trackme.apigateway.ApiGatewayApplication;
import com.poianitibaldizhou.trackme.apigateway.TestUtils;
import com.poianitibaldizhou.trackme.apigateway.entity.CompanyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.PrivateThirdPartyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.ThirdPartyCustomer;
import com.poianitibaldizhou.trackme.apigateway.repository.CompanyDetailRepository;
import com.poianitibaldizhou.trackme.apigateway.repository.PrivateThirdPartyDetailRepository;
import com.poianitibaldizhou.trackme.apigateway.repository.ThirdPartyRepository;
import com.poianitibaldizhou.trackme.apigateway.util.ThirdPartyCompanyWrapper;
import com.poianitibaldizhou.trackme.apigateway.util.ThirdPartyPrivateWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Integration test for the public part of the controller that manages the third party accounts
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiGatewayApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"classpath:IntegrationTPControllerTestData"})
@Transactional
public class PublicThirdPartyControllerIntegrationTest {

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

        restTemplate.exchange(
                createURLWithPort("/public/thirdparties/companies"),
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
    public void testCompanyRegisterThirdPartyWhenEmailAlreadyPresent() {
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

        try {
            restTemplate.exchange(
                    createURLWithPort("/public/thirdparties/companies"),
                    HttpMethod.POST, entity, String.class);
        } catch(RestClientException e) {
            assertEquals("400 ", e.getMessage());
        }
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

        restTemplate.exchange(
                createURLWithPort("/public/thirdparties/privates"),
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
    public void testPrivateRegisterThirdPartyWhenEmailAlreadyPresent()  {
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

        try {
            restTemplate.exchange(
                    createURLWithPort("/public/thirdparties/privates"),
                    HttpMethod.POST, entity, String.class);
            fail("Exception expected");
        } catch(RestClientException e) {
            assertEquals("400 ", e.getMessage());
        }
    }

    /**
     * Test when not all the parameters required are set
     */
    @Test
    public void testNotAllParameterSpecified() {
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

        try {
            restTemplate.exchange(
                    createURLWithPort("/public/thirdparties/privates"),
                    HttpMethod.POST, entity, String.class);
            fail("Exception expected");
        } catch(RestClientException e) {
            assertEquals("400 ", e.getMessage());
        }
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
