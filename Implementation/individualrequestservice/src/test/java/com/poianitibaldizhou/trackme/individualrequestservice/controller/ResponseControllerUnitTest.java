package com.poianitibaldizhou.trackme.individualrequestservice.controller;

import com.poianitibaldizhou.trackme.individualrequestservice.assembler.BlockedThirdPartyResourceAssembler;
import com.poianitibaldizhou.trackme.individualrequestservice.assembler.ResponseResourceAssembler;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.*;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.*;
import com.poianitibaldizhou.trackme.individualrequestservice.service.UploadResponseService;
import com.poianitibaldizhou.trackme.individualrequestservice.util.ResponseType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.sql.Timestamp;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ResponseController.class)
@Import({ResponseResourceAssembler.class, BlockedThirdPartyResourceAssembler.class})
public class ResponseControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UploadResponseService service;

    /**
     * Test the add of a response when the service does not produce any error
     * @throws Exception exception due to the mvc mock post
     */
    @Test
    public void testAddResponseWithAccept() throws Exception {
        IndividualRequest request = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user1"), (long)1);
        request.setId((long)1);

        Response response = new Response();
        response.setAcceptanceTimeStamp(new Timestamp(0));
        response.setResponse(ResponseType.ACCEPT);
        response.setRequestID((long) 1);
        response.setRequest(request);

        given(service.addResponse((long)1, ResponseType.ACCEPT, new User("user1"))).willReturn(response);

        String json = "ACCEPT";

        mvc.perform(post("/uploadresponseservice/response/user1/1").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.response", is(ResponseType.ACCEPT.toString())))
                .andExpect(jsonPath("$.requestID", is(1)))
                .andExpect(jsonPath("$.acceptanceTimeStamp", is("1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("$._links.userPendingRequest.href", is("http://localhost/individualrequestservice/requests/users/user1")))
                .andExpect(jsonPath("$._links.request.href", is("http://localhost/individualrequestservice/requests/1")));
    }

    /**
     * Test the add of a response when the service does not produce any error
     * @throws Exception exception due to the mvc mock post
     */
    @Test
    public void testAddResponseWithRefuse() throws Exception {
        IndividualRequest request = new IndividualRequest(new Timestamp(0), new Date(0), new Date(0), new User("user1"), (long)1);
        request.setId((long)1);

        Response response = new Response();
        response.setAcceptanceTimeStamp(new Timestamp(0));
        response.setResponse(ResponseType.REFUSE);
        response.setRequestID((long) 1);
        response.setRequest(request);

        given(service.addResponse((long)1, ResponseType.REFUSE, new User("user1"))).willReturn(response);

        String json = "REFUSE";

        mvc.perform(post("/uploadresponseservice/response/user1/1").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.response", is(ResponseType.REFUSE.toString())))
                .andExpect(jsonPath("$.requestID", is(1)))
                .andExpect(jsonPath("$.acceptanceTimeStamp", is("1970-01-01T00:00:00.000+0000")))
                .andExpect(jsonPath("$._links.userPendingRequest.href", is("http://localhost/individualrequestservice/requests/users/user1")))
                .andExpect(jsonPath("$._links.request.href", is("http://localhost/individualrequestservice/requests/1")));
    }

    /**
     * Test the add of a response when the type of response is not allowed
     */
    @Test
    public void testAddResponseWithInvalidResponseType() throws Exception {
        String json = "invalidtype";

        mvc.perform(post("/uploadresponseservice/response/user1/1").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test the add of a response when the add is performed by an user that is not the one related with the request
     */
    @Test
    public void testAddResponseWhenNonMatchingUser() throws Exception {
        String json = "REFUSE";

        given(service.addResponse((long)1, ResponseType.REFUSE, new User("user1"))).willThrow(NonMatchingUserException.class);

        mvc.perform(post("/uploadresponseservice/response/user1/1").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test the add of a response when a response has already been provided to that request
     */
    @Test
    public void testAddResponseWhenResponseAlreadyPresent() throws Exception {
        String json = "REFUSE";

        given(service.addResponse((long)1, ResponseType.REFUSE, new User("user1"))).willThrow(ResponseAlreadyPresentException.class);

        mvc.perform(post("/uploadresponseservice/response/user1/1").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test the add of a response when the add is performed by an user has not received any request from that
     * third party
     */
    @Test
    public void testAddResponseWhenNoRequestFound() throws Exception {
        String json = "REFUSE";

        given(service.addResponse((long)1, ResponseType.REFUSE, new User("user1"))).willThrow(ThirdPartyNotFoundException.class);

        mvc.perform(post("/uploadresponseservice/response/user1/1").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isNotFound());
    }

    /**
     * Test the add of a response when the method of the service throws an exception.
     * In particular, the case of non existing user is tried
     */
    @Test
    public void testAddResponseOnNonExistingUser() throws Exception {
        String json = "REFUSE";

        given(service.addResponse((long)1, ResponseType.REFUSE, new User("user1"))).willThrow(UserNotFoundException.class);

        mvc.perform(post("/uploadresponseservice/response/user1/1").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isNotFound());
    }

    /**
     * Test the add of a block when the service does not produce any error
     */
    @Test
    public void testAddBlock() throws Exception {
        BlockedThirdPartyKey blockedThirdPartyKey = new BlockedThirdPartyKey( (long) 1, new User("user1"));
        BlockedThirdParty blockedThirdParty = new BlockedThirdParty();
        blockedThirdParty.setBlockDate(new Date(0));
        blockedThirdParty.setKey(blockedThirdPartyKey);

        given(service.addBlock(new User("user1"), (long) 1)).willReturn(blockedThirdParty);

        mvc.perform(post("/uploadresponseservice/blockedThirdParty/user1/1").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isCreated());
    }

    /**
     * Test the add of a block when the service throws an error.
     * In particular, in this case a block has already been added
     */
    @Test
    public void testAddBlockWhenSomethingIsWrong() throws Exception {
        BlockedThirdPartyKey blockedThirdPartyKey = new BlockedThirdPartyKey( (long) 1, new User("user1"));
        BlockedThirdParty blockedThirdParty = new BlockedThirdParty();
        blockedThirdParty.setBlockDate(new Date(0));
        blockedThirdParty.setKey(blockedThirdPartyKey);

        given(service.addBlock(new User("user1"), (long) 1)).willThrow(new BlockAlreadyPerformedException((long)1));

        mvc.perform(post("/uploadresponseservice/blockedThirdParty/user1/1").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isBadRequest());
    }
}
