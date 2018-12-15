package com.poianitibaldizhou.trackme.individualrequestservice.controller;

import com.poianitibaldizhou.trackme.individualrequestservice.assembler.IndividualRequestResourceAssembler;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.RequestNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.service.IndividualRequestManagerService;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Individual test of the controller
 */
@RunWith(SpringRunner.class)
@WebMvcTest(IndividualRequestController.class)
@Import({IndividualRequestResourceAssembler.class})
public class IndividualRequestControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IndividualRequestManagerService service;

    /**
     * Test the retrieval of the pending request of a certain user, when this list is non empty (in particular,
     * size 1)
     *
     * @throws Exception exception due to mock mvc method get
     */
    @Test
    public void getUserRequestTest() throws Exception {
        IndividualRequest request = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user1"), (long)2);
        request.setId((long) 3);

        List<IndividualRequest> allRequests = Collections.singletonList(request);
        given(service.getUserPendingRequests(new User("user1"))).willReturn(allRequests);

        mvc.perform(get("/individualrequestservice/requests/users/user1").accept(MediaTypes.HAL_JSON_VALUE).
                header("ssn", "user1"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.individualRequests", hasSize(1)))
                .andExpect(jsonPath("$._embedded.individualRequests[0].thirdPartyID", is(2)))
                .andExpect(jsonPath("$._embedded.individualRequests[0].status", is(IndividualRequestStatus.PENDING.toString())))
                //.andExpect(jsonPath("$._embedded.individualRequests[0].timestamp", is(request.getTimestamp().toString())))
                .andExpect(jsonPath("$._embedded.individualRequests[0].startDate", is(new Date(0).toString())))
                .andExpect(jsonPath("$._embedded.individualRequests[0].endDate", is(new Date(0).toString())))
                .andExpect(jsonPath("$._embedded.individualRequests[0]._links.self.href", is("http://localhost/individualrequestservice/requests/3")))
                .andExpect(jsonPath("$._embedded.individualRequests[0]._links.thirdPartyRequest.href", is("http://localhost/individualrequestservice/requests/thirdparty/2")))
                .andExpect(jsonPath("$._embedded.individualRequests[0]._links.userPendingRequest.href", is("http://localhost/individualrequestservice/requests/users/user1")))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/individualrequestservice/requests/users/user1")));
    }

    /**
     * Test the retrieval of the pending request of a certain user, when this list is empty
     *
     * @throws Exception exception due to mock mvc method get
     */
    @Test
    public void getUserRequestTestWhenTheListIsEmpty() throws Exception {
        mvc.perform(get("/individualrequestservice/requests/users/user1").accept(MediaTypes.HAL_JSON_VALUE).header("ssn", "user1"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/individualrequestservice/requests/users/user1")));
    }

    /**
     * Test the retrieval of the pending request of a certain user, when the user is not registered into the system
     *
     * @throws Exception exception due to mock mvc method get
     */
    @Test
    public void getUserRequestTestWhenUserNotRegistered() throws Exception {
        given(service.getUserPendingRequests(new User("user1"))).willThrow(new UserNotFoundException(new User("user1")));

        mvc.perform(get("/individualrequestservice/requests/users/user1").accept(MediaTypes.HAL_JSON_VALUE).header("ssn", "user1"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test the access to the rest api provided by the IndividualRequest controller.
     * In particular this method tests the retrieval of requests performed by a certain third party customer
     * when the set of his requests is non-empty
     *
     * @throws Exception exception to mock mvc method perform
     */
    @Test
    public void getThirdPartyRequestTest() throws Exception {
        IndividualRequest request = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user1"), (long)1);
        request.setId((long)1);

        List<IndividualRequest> allRequests = Collections.singletonList(request);
        given(service.getThirdPartyRequests((long) 1)).willReturn(allRequests);

        // TODO: fix timestamp format issue (here, but also in other methods of this test, when the timestamp is needed)
        mvc.perform(get("/individualrequestservice/requests/thirdparty/1").accept(MediaTypes.HAL_JSON_VALUE).header("id", "1"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.individualRequests", hasSize(1)))
                .andExpect(jsonPath("$._embedded.individualRequests[0].thirdPartyID", is(1)))
                .andExpect(jsonPath("$._embedded.individualRequests[0].status", is(IndividualRequestStatus.PENDING.toString())))
                //.andExpect(jsonPath("$._embedded.individualRequests[0].timestamp", is(request.getTimestamp().toString())))
                .andExpect(jsonPath("$._embedded.individualRequests[0].startDate", is(new Date(0).toString())))
                .andExpect(jsonPath("$._embedded.individualRequests[0].endDate", is(new Date(0).toString())))
                .andExpect(jsonPath("$._embedded.individualRequests[0]._links.self.href", is("http://localhost/individualrequestservice/requests/1")))
                .andExpect(jsonPath("$._embedded.individualRequests[0]._links.thirdPartyRequest.href", is("http://localhost/individualrequestservice/requests/thirdparty/1")))
                .andExpect(jsonPath("$._embedded.individualRequests[0]._links.userPendingRequest.href", is("http://localhost/individualrequestservice/requests/users/user1")))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/individualrequestservice/requests/thirdparty/1")));
    }

    /**
     * Test the access to the rest api provided by the IndividualRequestController.
     * In particular this method tests the retrieval of requests performed by a certain third party customer
     * when the set of his requests is empty
     *
     * @throws Exception exception due to mock mvc method perform
     */
    @Test
    public void getThirdPartyRequestWhenNoRequestPerformedTest() throws Exception {
        mvc.perform(get("/individualrequestservice/requests/thirdparty/2").accept(MediaTypes.HAL_JSON_VALUE).header("id", "2"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/individualrequestservice/requests/thirdparty/2")));
    }

    /**
     * Test the access to the rest api provided by the IndividualRequestController.
     * In particular, this method tests the retrieval of requests with a certain id, when that requests is
     * not present
     *
     * @throws Exception exception due to mock mvc method perform
     */
    @Test
    public void getRequestByIdTestWhenRequestNotFound() throws Exception {
        given(service.getRequestById((long) 1)).willThrow(new RequestNotFoundException((long)1));

        mvc.perform(get("/individualrequestservice/requests/1").accept(MediaTypes.HAL_JSON_VALUE).header("id", "1")
        .header("ssn", "user1"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test the access to the rest api provided by the IndividualRequestController.
     * In particular, this method tests the retrieval of requests with a certain id, when that requests is
     * present
     *
     * @throws Exception exception due to mock mvc method perform
     */
    @Test
    public void getRequestByIdTest() throws Exception {
        IndividualRequest request = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user1"), (long)1);
        request.setId((long)1);

        given(service.getRequestById((long) 1)).willReturn(request);

        mvc.perform(get("/individualrequestservice/requests/1").accept(MediaTypes.HAL_JSON_VALUE).header("ssn", "user1")
        .header("id", ""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(request.getStatus().toString())))
                //.andExpect(jsonPath("$.timestamp", is(request.getTimestamp().toString())))
                .andExpect(jsonPath("$.startDate", is(request.getStartDate().toString())))
                .andExpect(jsonPath("$.endDate", is(request.getEndDate().toString())))
                .andExpect(jsonPath("$.thirdPartyID", is(request.getThirdPartyID().intValue())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/individualrequestservice/requests/1")))
                .andExpect(jsonPath("$._links.thirdPartyRequest.href", is("http://localhost/individualrequestservice/requests/thirdparty/1")));
    }

    /**
     * Test the access to the rest api provided by the IndividualRequestController.
     * In particular, this method tests the add of a new request, when the related user exists
     *
     * @throws Exception exception due to mock mvc method perform
     */
    @Test
    public void newRequestTest() throws Exception {
        IndividualRequest request = new IndividualRequest();
        request.setUser(new User("user1"));
        request.setThirdPartyID((long) 1);
        request.setStartDate(new Date(0));
        request.setEndDate(new Date(0));
        request.setId((long) 1);
        request.setStatus(IndividualRequestStatus.PENDING);

        IndividualRequest mockedRequest = new IndividualRequest();
        mockedRequest.setUser(new User("user1"));
        mockedRequest.setThirdPartyID((long) 1);
        mockedRequest.setStartDate(new Date(0));
        mockedRequest.setEndDate(new Date(0));
        mockedRequest.setId((long) 1);
        mockedRequest.setTimestamp(new Timestamp(0));
        mockedRequest.setStatus(IndividualRequestStatus.PENDING);

        String json = "{\n" +
                "\t\"id\": 1,\n" +
                "\t\"status\": \"PENDING\",\n" +
                "\t\"startDate\": \"1970-01-01\",\n" +
                "\t\"endDate\": \"1970-01-01\",\n" +
                "\t\"ssn\": \"user1\",\n" +
                "\t\"thirdPartyID\": 1\n" +
                "}";

        given(service.addRequest(request)).willReturn(mockedRequest);

        mvc.perform(post("/individualrequestservice/requests/user1").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").
                content(json)
                .header("id", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is(IndividualRequestStatus.PENDING.toString())))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.timestamp", is("1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("$.startDate", is(request.getStartDate().toString())))
                .andExpect(jsonPath("$.endDate", is(request.getEndDate().toString())))
                .andExpect(jsonPath("$.thirdPartyID", is(request.getThirdPartyID().intValue())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/individualrequestservice/requests/1")))
                .andExpect(jsonPath("$._links.thirdPartyRequest.href", is("http://localhost/individualrequestservice/requests/thirdparty/1")))
                .andExpect(jsonPath("$._links.userPendingRequest.href", is("http://localhost/individualrequestservice/requests/users/user1")));
    }

    /**
     * Test the access to the rest api provided by the IndividualRequestController.
     * In particular, this method tests the add of a new request, when the related user exists
     *
     * @throws Exception exception due to mock mvc method perform
     */
    @Test
    public void newRequestTestWhenRelatedUserNotExist() throws Exception {
        IndividualRequest request = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user1"), (long)1);
        request.setId((long)1);

        given(service.addRequest(request)).willThrow(new UserNotFoundException(new User("user1")));

        mvc.perform(post("/individualrequestservice/requests").header("id", 1))
                .andExpect(status().isNotFound());
    }
}
