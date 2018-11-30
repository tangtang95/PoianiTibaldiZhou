package com.poianitibaldizhou.trackme.individualrequestservice.service;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.BlockedThirdParty;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.BlockedThirdPartyKey;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.RequestNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.BlockedThirdPartyRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.UserRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for the individual request manager service
 */
@RunWith(SpringRunner.class)
public class IndividualRequestManagerServiceImplUnitTest {


    @MockBean
    private IndividualRequestRepository individualRequestRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BlockedThirdPartyRepository blockedThirdPartyRepository;

    private IndividualRequestManagerService requestManagerService;

    @Before
    public void setUp() {
        setUpMockUserRepo(userRepository);
        setUpMockRequestRepo(individualRequestRepository);
        setUpMockBlockedRepo(blockedThirdPartyRepository);
        requestManagerService = new IndividualRequestManagerServiceImpl(individualRequestRepository, blockedThirdPartyRepository, userRepository);
    }

    @After
    public void tearDown() {
        requestManagerService = null;
    }

    /**
     * Set up the mocks for the user repository
     * @param userRepo user repository that will be mocked
     */
    private void setUpMockUserRepo(UserRepository userRepo) {
        User user = new User();
        user.setSsn("user1");
        Mockito.when(userRepo.findById("user1")).thenReturn(java.util.Optional.of(user));

        user = new User();
        user.setSsn("user3");
        Mockito.when(userRepo.findById("user3")).thenReturn(java.util.Optional.ofNullable(user));
    }

    /**
     * Set up the mocks for the individual request repository
     * @param requestRepo individual request repository that will be mocked
     */
    private void setUpMockRequestRepo(IndividualRequestRepository requestRepo) {
        IndividualRequest request1 = new IndividualRequest(
                new Timestamp(0), new Date(0), new Date(0), "user1", (long) 1);
        request1.setId((long) 1);
        IndividualRequest request2 = new IndividualRequest(
                new Timestamp(10000), new Date(10000), new Date(10000), "user1", (long) 1);
        request2.setId((long) 2);
        IndividualRequest request3 = new IndividualRequest(
                new Timestamp(0), new Date(0), new Date(0), "user3", (long) 2);
        request3.setId((long) 3);

        List<IndividualRequest> listOfFirstTP = new ArrayList<>();
        listOfFirstTP.add(request1);
        listOfFirstTP.add(request2);

        List<IndividualRequest> listOfSecondTP = new ArrayList<>();
        listOfSecondTP.add(request3);

        Mockito.when(requestRepo.findById((long)1)).thenReturn(java.util.Optional.of(request1));
        Mockito.when(requestRepo.findById((long)2)).thenReturn(java.util.Optional.of(request2));
        Mockito.when(requestRepo.findById((long)3)).thenReturn(java.util.Optional.of(request3));

        Mockito.when(requestRepo.findAllByThirdPartyID((long) 1)).thenReturn(listOfFirstTP);
        Mockito.when(requestRepo.findAllByThirdPartyID((long) 2)).thenReturn(listOfSecondTP);
    }

    /**
     * Set up the mocks for the blocked third party repository
     * @param blockedRepo blocked third party repository that will be mocked
     */
    private void setUpMockBlockedRepo(BlockedThirdPartyRepository blockedRepo) {
        BlockedThirdPartyKey key = new BlockedThirdPartyKey((long)4, "user1");
        BlockedThirdParty blockedThirdParty = new BlockedThirdParty();
        blockedThirdParty.setKey(key);
        Mockito.when(blockedRepo.findById(key)).thenReturn(java.util.Optional.ofNullable(blockedThirdParty));
    }

    /**
     * Test the getRequestById method, when a request with a certain is present in the repository
     */
    @Test
    public void getRequestByIdTest(){
        IndividualRequest individualRequest = requestManagerService.getThirdPartyRequests((long) 1).get(0);

        assertEquals(individualRequest, requestManagerService.getRequestById(individualRequest.getId()));
    }

    /**
     * Test the getRequestById method, when a request with a certain id is not present in the repository
     */
    @Test(expected = RequestNotFoundException.class)
    public void getRequestByIdWhenRequestNotPresent() {
        requestManagerService.getRequestById((long)1000);
    }

    /**
     * Test the getThirdPartyRequests method, when a request performed by a specific third party is present in the
     * repository
     */
    @Test
    public void getThirdPartyRequestsTest() {
        List<IndividualRequest> requestList = requestManagerService.getThirdPartyRequests((long) 2);
        assertEquals(1, requestList.size());
        assertEquals((long)3, (long)requestList.get(0).getId());
    }

    /**
     * Test the getThirdPartyRequests method, when no request was performed by a given third party customer
     */
    @Test
    public void getThirdPartyRequestsTestWhenRequestNotPresent() {
        assertTrue(requestManagerService.getThirdPartyRequests((long) 5).isEmpty());
    }

    /**
     * Test the add of a new request, when the user related with it is present (i.e. registered), and when
     * that user didn't block the petitioner
     */
    @Test
    public void addRequestTest() {
        IndividualRequest newRequest = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), "user3", (long) 4);
        requestManagerService.addRequest(newRequest);

        assertEquals(IndividualRequestStatus.PENDING, newRequest.getStatus());
    }

    /**
     * Test the add of a new request, when the user related with is not present (i.e. not registered)
     */
    @Test(expected = UserNotFoundException.class)
    public void addRequestTestWhenUserNotPresent() {
        requestManagerService.addRequest(new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), "user4", (long) 1));
    }


    /**
     * Test the add of a new request, when the user related with it is present (i.e. registered), but has already
     * blocked the petitioner
     */
    @Test
    public void addRequestTestWhenBlocked() {
        IndividualRequest newRequest = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), "user1", (long) 4);
        requestManagerService.addRequest(newRequest);

        assertEquals(IndividualRequestStatus.REFUSED, newRequest.getStatus());
    }
}
