package com.poianitibaldizhou.trackme.individualrequestservice.service;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.*;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.*;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.BlockedThirdPartyRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.ResponseRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.UserRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.util.ResponseType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Unit test for the uploading response service
 */
@RunWith(SpringRunner.class)
public class UploadResponseServiceImplUnitTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BlockedThirdPartyRepository blockedThirdPartyRepository;

    @MockBean
    private IndividualRequestRepository individualRequestRepository;

    @MockBean
    private ResponseRepository responseRepository;

    private UploadResponseServiceImpl uploadResponseService;

    @Before
    public void setUp() {
        setUpUserRepository();
        setUpRequestRepository();
        setUpBlockRepository();
        setUpResponseRepository();
        uploadResponseService = new UploadResponseServiceImpl(userRepository, blockedThirdPartyRepository, individualRequestRepository,
                responseRepository);
    }

    private void setUpResponseRepository() {
        IndividualRequest request2 = new IndividualRequest(
                new Timestamp(10000), new Date(10000), new Date(10000), new User("user1"), (long) 2);
        request2.setId((long) 2);

        Response response = new Response();
        response.setRequestID((long) 2);
        response.setRequest(request2);
        response.setResponse(ResponseType.REFUSE);
        response.setAcceptanceTimeStamp(new Timestamp(0));

        when(responseRepository.findById((long) 2)).thenReturn(java.util.Optional.ofNullable(response));
    }

    private void setUpRequestRepository() {
        IndividualRequest request1 = new IndividualRequest(
                new Timestamp(0), new Date(0), new Date(0), new User("user1"), (long) 1);
        request1.setId((long) 1);
        IndividualRequest request2 = new IndividualRequest(
                new Timestamp(10000), new Date(10000), new Date(10000), new User("user1"), (long) 2);
        request2.setId((long) 2);
        IndividualRequest request3 = new IndividualRequest(
                new Timestamp(0), new Date(0), new Date(0), new User("user3"), (long) 3);
        request3.setId((long) 3);
        IndividualRequest request4 = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user4"), (long) 1);
        request4.setId((long) 4);

        when(individualRequestRepository.findById((long)1)).thenReturn(java.util.Optional.of(request1));
        when(individualRequestRepository.findById((long)2)).thenReturn(java.util.Optional.of(request2));
        when(individualRequestRepository.findById((long)3)).thenReturn(java.util.Optional.of(request3));
        when(individualRequestRepository.findById((long)4)).thenReturn(java.util.Optional.of(request4));

        List<IndividualRequest> list = new ArrayList<>();
        list.add(request1);
        list.add(request4);

        when(individualRequestRepository.findAllByThirdPartyID((long) 1)).thenReturn(list);
        when(individualRequestRepository.findAllByThirdPartyID((long) 2)).thenReturn(Collections.singletonList(request2));
        when(individualRequestRepository.findAllByThirdPartyID((long) 3)).thenReturn(Collections.singletonList(request3));
    }

    private void setUpBlockRepository() {
        BlockedThirdPartyKey blockedThirdPartyKey = new BlockedThirdPartyKey((long)2, new User("user1"));
        BlockedThirdParty blockedThirdParty = new BlockedThirdParty();
        blockedThirdParty.setKey(blockedThirdPartyKey);
        blockedThirdParty.setBlockDate(new Date(0));

        when(blockedThirdPartyRepository.findById(blockedThirdPartyKey)).thenReturn(java.util.Optional.ofNullable(blockedThirdParty));
    }

    /**
     * Set up the user repository, with three users
     */
    private void setUpUserRepository() {
        User user = new User();
        user.setSsn("user1");
        when(userRepository.findById("user1")).thenReturn(java.util.Optional.of(user));

        user = new User();
        user.setSsn("user2");
        when(userRepository.findById("user2")).thenReturn(java.util.Optional.ofNullable(user));

        user = new User();
        user.setSsn("user3");
        when(userRepository.findById("user3")).thenReturn(java.util.Optional.ofNullable(user));

        user = new User();
        user.setSsn("user4");
        when(userRepository.findById("user4")).thenReturn(java.util.Optional.ofNullable(user));
    }
    

    @After
    public void tearDown() {
        uploadResponseService = null;
    }

    /**
     * Test the add of a response when everything is fine, that means that the user is registered, that a request
     * to that user is present, that the user calling the service is matched with the one of the request and that
     * no response is already present
     */
    @Test
    public void testAddResponse(){
        uploadResponseService.addResponse((long) 1, ResponseType.ACCEPT, new User("user1"));

        verify(responseRepository, times(1)).save(any(Response.class));
    }

    /**
     * Test the add of a response when it is performed by a non existing user
     */
    @Test(expected = UserNotFoundException.class)
    public void testAddResponseNonExistingUser() {
        uploadResponseService.addResponse((long) 1, ResponseType.ACCEPT, new User("notExistingUser"));
    }

    /**
     * Test the add of a response when it is performed on a non existing request
     */
    @Test(expected = RequestNotFoundException.class)
    public void testAddResponseNonExistingRequest() {
        uploadResponseService.addResponse((long)100, ResponseType.REFUSE, new User("user1"));
    }

    /**
     * Test the add of a response when a response for that request is already present
     */
    @Test(expected = ResponseAlreadyPresentException.class)
    public void testAddResponseWhenResponseAlreadyPresent() {
        uploadResponseService.addResponse((long)2, ResponseType.REFUSE, new User("user2"));
    }

    /**
     * Test the add of a response when the user who is responding is not the one related with the request
     */
    @Test(expected = NonMatchingUserException.class)
    public void testAddResponseNonMatchingUser() {
        uploadResponseService.addResponse((long) 1, ResponseType.REFUSE, new User("user2"));
    }

    /**
     * Test the block when the user is not registered
     */
    @Test(expected = UserNotFoundException.class)
    public void testBlockOfNonExistingUser() {
        uploadResponseService.addBlock(new User("nonExistingUser"), (long) 1);
    }

    /**
     * Test the block when no third party has performed request toward that user
     */
    @Test(expected = ThirdPartyNotFoundException.class)
    public void testBlockWhenNoThirdHasPerformedRequestToThatUser() {
        uploadResponseService.addBlock(new User("user1"), (long)3);
    }

    /**
     * Test the block when the third party is already blocked from that user
     */
    @Test(expected = BlockAlreadyPerformedException.class)
    public void testBlockWhenAlreadyPerformed() {
        uploadResponseService.addBlock(new User("user1"), (long)2);
    }

    /**
     * Test the block when the block is inserted successfully
     */
    @Test
    public void testBlock() {
        uploadResponseService.addBlock(new User("user4"), (long)1);

        BlockedThirdPartyKey blockedThirdPartyKey = new BlockedThirdPartyKey((long) 1, new User("user4"));
        BlockedThirdParty blockedThirdParty = new BlockedThirdParty();
        blockedThirdParty.setKey(blockedThirdPartyKey);
        blockedThirdParty.setBlockDate(Date.valueOf(LocalDate.now()));

        verify(blockedThirdPartyRepository, times(1)).save(blockedThirdParty);
    }
}
