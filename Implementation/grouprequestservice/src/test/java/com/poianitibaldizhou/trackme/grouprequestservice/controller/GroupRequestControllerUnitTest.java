package com.poianitibaldizhou.trackme.grouprequestservice.controller;

import com.poianitibaldizhou.trackme.grouprequestservice.assembler.GroupRequestWrapperAssembler;
import com.poianitibaldizhou.trackme.grouprequestservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.grouprequestservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.grouprequestservice.exception.BadOperatorRequestTypeException;
import com.poianitibaldizhou.trackme.grouprequestservice.exception.GroupRequestNotFoundException;
import com.poianitibaldizhou.trackme.grouprequestservice.service.GroupRequestManagerService;
import com.poianitibaldizhou.trackme.grouprequestservice.util.*;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(GroupRequestController.class)
@Import({GroupRequestWrapperAssembler.class})
public class GroupRequestControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GroupRequestManagerService service;

    /**
     * Test group request by id
     *
     * @throws Exception due to the mvc mock get method
     */
    @Test
    public void getRequestById() throws Exception {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId(1L);
        groupRequest.setRequestType(RequestType.HEARTBEAT);
        groupRequest.setAggregatorOperator(AggregatorOperator.COUNT);
        groupRequest.setStatus(RequestStatus.UNDER_ANALYSIS);
        groupRequest.setDate(new Date(0));
        groupRequest.setThirdPartyId(1L);

        FilterStatement filterStatement = new FilterStatement();
        filterStatement.setGroupRequest(groupRequest);
        filterStatement.setColumn(FieldType.HEART_BEAT);
        filterStatement.setValue("100");
        filterStatement.setComparisonSymbol(ComparisonSymbol.LESS);
        filterStatement.setId(1L);

        GroupRequestWrapper groupRequestWrapper = new GroupRequestWrapper(groupRequest, Collections.singletonList(filterStatement));

        given(service.getById(1L)).willReturn(groupRequestWrapper);

        mvc.perform(get("/grouprequestservice/requests/1").accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupRequest.id", is(groupRequest.getId().intValue())))
                .andExpect(jsonPath("$.groupRequest.thirdPartyId", is(groupRequest.getThirdPartyId().intValue())))
                .andExpect(jsonPath("$.groupRequest.date", is(groupRequest.getDate().toString())))
                .andExpect(jsonPath("$.groupRequest.aggregatorOperator", is(groupRequest.getAggregatorOperator().toString())))
                .andExpect(jsonPath("$.groupRequest.requestType", is(groupRequest.getRequestType().toString())))
                .andExpect(jsonPath("$.groupRequest.status", is(groupRequest.getStatus().toString())))
                .andExpect(jsonPath("$.filterStatementList", hasSize(1)))
                .andExpect(jsonPath("$.filterStatementList[0].id", is(filterStatement.getId().intValue())))
                .andExpect(jsonPath("$.filterStatementList[0].column", is(filterStatement.getColumn().toString())))
                .andExpect(jsonPath("$.filterStatementList[0].value", is(filterStatement.getValue())))
                .andExpect(jsonPath("$.filterStatementList[0].comparisonSymbol", is(filterStatement.getComparisonSymbol().toString())))
                .andExpect(jsonPath("$.filterStatementList[0].groupRequest.id", is(groupRequest.getId().intValue())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/grouprequestservice/requests/1")));

    }

    /**
     * Test the get of a request by a specified id, when no request with that id is present
     *
     * @throws Exception due to mock mvc get perform
     */
    @Test
    public void testGetRequestByIdWhenRequestNotPresent() throws Exception{
        given(service.getById(1L)).willThrow(new GroupRequestNotFoundException(1L));

        mvc.perform(get("/grouprequestservice/requests/1").accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isNotFound());

    }

    /**
     * Test the get of group request performed by a specified third party customer
     *
     * @throws Exception due to mock mvc get perform
     */
    @Test
    public void testGetRequestByThirdPartyId() throws Exception {
        GroupRequest groupRequest1 = new GroupRequest();
        groupRequest1.setId(1L);
        groupRequest1.setRequestType(RequestType.PRESSURE_MAX);
        groupRequest1.setAggregatorOperator(AggregatorOperator.MAX);
        groupRequest1.setStatus(RequestStatus.REFUSED);
        groupRequest1.setDate(new Date(0));
        groupRequest1.setThirdPartyId(1L);

        FilterStatement filterStatement1 = new FilterStatement();
        filterStatement1.setGroupRequest(groupRequest1);
        filterStatement1.setColumn(FieldType.BLOOD_OXYGEN_LEVEL);
        filterStatement1.setValue("70");
        filterStatement1.setComparisonSymbol(ComparisonSymbol.EQUALS);
        filterStatement1.setId(1L);

        GroupRequest groupRequest2 = new GroupRequest();
        groupRequest2.setId(2L);
        groupRequest2.setRequestType(RequestType.BIRTH_CITY);
        groupRequest2.setAggregatorOperator(AggregatorOperator.DISTINCT_COUNT);
        groupRequest2.setStatus(RequestStatus.REFUSED);
        groupRequest2.setDate(new Date(0));
        groupRequest2.setThirdPartyId(1L);

        FilterStatement filterStatement2 = new FilterStatement();
        filterStatement2.setGroupRequest(groupRequest2);
        filterStatement2.setColumn(FieldType.HEART_BEAT);
        filterStatement2.setValue("100");
        filterStatement2.setComparisonSymbol(ComparisonSymbol.LESS);
        filterStatement2.setId(2L);

        List<GroupRequestWrapper> groupRequestWrapperList = new ArrayList<>();
        groupRequestWrapperList.add(new GroupRequestWrapper(groupRequest1, Collections.singletonList(filterStatement1)));
        groupRequestWrapperList.add(new GroupRequestWrapper(groupRequest2, Collections.singletonList(filterStatement2)));

        given(service.getByThirdPartyId(1L)).willReturn(groupRequestWrapperList);

        mvc.perform(get("/grouprequestservice/requests/thirdparties/1").accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].groupRequest.id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].groupRequest.thirdPartyId", containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].groupRequest.date", containsInAnyOrder(groupRequest1.getDate().toString(), groupRequest2.getDate().toString())))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].groupRequest.aggregatorOperator", containsInAnyOrder(groupRequest1.getAggregatorOperator().toString(), groupRequest2.getAggregatorOperator().toString())))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].groupRequest.requestType", containsInAnyOrder(groupRequest1.getRequestType().toString(), groupRequest2.getRequestType().toString())))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].groupRequest.status", containsInAnyOrder(groupRequest1.getStatus().toString(), groupRequest2.getStatus().toString())))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].filterStatementList[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].filterStatementList[*].column", containsInAnyOrder(filterStatement1.getColumn().toString(), filterStatement2.getColumn().toString())))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].filterStatementList[*].value", containsInAnyOrder(filterStatement1.getValue(), filterStatement2.getValue())))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*].filterStatementList[*].comparisonSymbol", containsInAnyOrder(filterStatement1.getComparisonSymbol().toString(), filterStatement2.getComparisonSymbol().toString())))
                .andExpect(jsonPath("$._embedded.groupRequestWrapperList[*]._links.self.href", containsInAnyOrder("http://localhost/grouprequestservice/requests/1", "http://localhost/grouprequestservice/requests/2")))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/grouprequestservice/requests/thirdparties/1")));
    }

    /**
     * Test the add of a new request when everything is fine
     */
    @Test
    public void testAddNewRequest() {
        GroupRequest groupRequest = new GroupRequest();
        // TODO
    }

    /**
     * Test the add of a new request when the operator is not matched with the request type
     *
     * @throws Exception due to the mock mvc method post
     */
    @Test
    public void testAddNewRequestWhenBadOperators() throws Exception{
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setThirdPartyId(1L);
        groupRequest.setId(1L);
        groupRequest.setDate(new Date(0));
        groupRequest.setAggregatorOperator(AggregatorOperator.AVG);
        groupRequest.setRequestType(RequestType.ALL);

        GroupRequestWrapper groupRequestWrapper = new GroupRequestWrapper(groupRequest, new ArrayList<>());

        given(service.addGroupRequest(groupRequestWrapper)).willThrow(new BadOperatorRequestTypeException(
                groupRequest.getAggregatorOperator(), groupRequest.getRequestType()));

        mvc.perform(post("/grouprequestservice/requests"))
                .andExpect(status().isBadRequest());
    }
}
