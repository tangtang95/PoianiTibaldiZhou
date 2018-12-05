package com.poianitibaldizhou.trackme.sharedataservice.service;

import com.poianitibaldizhou.trackme.sharedataservice.entity.*;
import com.poianitibaldizhou.trackme.sharedataservice.exception.GroupRequestNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.exception.IndividualRequestNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.repository.*;
import com.poianitibaldizhou.trackme.sharedataservice.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit test for the access data service
 */
public class AccessDataServiceImplUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HealthDataRepository healthDataRepository;

    @Mock
    private PositionDataRepository positionDataRepository;

    @Mock
    private GroupRequestRepository groupRequestRepository;

    @Mock
    private IndividualRequestRepository individualRequestRepository;

    @Mock
    private FilterStatementRepository filterStatementRepository;

    @InjectMocks
    private AccessDataServiceImpl accessDataService;


    private static final String USER_1 = "user1";

    private static final Long INDIVIDUAL_REQUEST_1_ID = 1L;
    private static final Long NOT_EXISTING_INDIVIDUAL_REQUEST_ID = 2L;

    private static final Long GROUP_REQUEST_1_ID = 1L;
    private static final Long NOT_EXISTING_GROUP_REQUEST_ID = 2L;

    private static final Long THIRD_PARTY_ID_1 = 1L;
    private static final Long NOT_EXISTING_THIRD_PARTY_ID = 2L;

    private static final Double GROUP_REQUEST_RESULT = 1D;

    @Mock
    private User user1;
    @Mock
    private HealthData healthData1, healthData2, healthData3;
    @Mock
    private PositionData positionData1, positionData2;
    @Mock
    private IndividualRequest individualRequest1;
    @Mock
    private GroupRequest groupRequest1;
    @Mock
    private FilterStatement filterStatement1, filterStatement2, filterStatement3;

    private List<FilterStatement> filterStatements;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        filterStatements = Arrays.asList(filterStatement1, filterStatement2, filterStatement3);

        setUpIndividualRequestRepository();
        setUpHealthDataRepository();
        setUpPositionDataRepository();
        setUpGroupRequestRepository();
        setUpFilterStatementRepository();
        setUpUserRepository();
    }

    private void setUpFilterStatementRepository() {
        when(filterStatementRepository.findAllByGroupRequest(groupRequest1)).thenReturn(filterStatements);
    }

    private void setUpGroupRequestRepository() {
        when(groupRequest1.getAggregatorOperator()).thenReturn(AggregatorOperator.COUNT);
        when(groupRequest1.getRequestType()).thenReturn(RequestType.ALL);

        when(groupRequestRepository.findByIdAndThirdPartyId(INDIVIDUAL_REQUEST_1_ID, THIRD_PARTY_ID_1))
                .thenReturn(Optional.of(groupRequest1));
        when(groupRequestRepository.findByIdAndThirdPartyId(INDIVIDUAL_REQUEST_1_ID, NOT_EXISTING_THIRD_PARTY_ID))
                .thenReturn(Optional.empty());
        when(groupRequestRepository.findByIdAndThirdPartyId(NOT_EXISTING_INDIVIDUAL_REQUEST_ID, THIRD_PARTY_ID_1))
                .thenReturn(Optional.empty());
        when(groupRequestRepository.findByIdAndThirdPartyId(NOT_EXISTING_INDIVIDUAL_REQUEST_ID, NOT_EXISTING_THIRD_PARTY_ID))
                .thenReturn(Optional.empty());
    }

    private void setUpIndividualRequestRepository() {
        when(individualRequest1.getUser()).thenReturn(user1);
        when(individualRequest1.getStartDate()).thenReturn(Date.valueOf(LocalDate.of(2010, 11, 10)));
        when(individualRequest1.getEndDate()).thenReturn(Date.valueOf(LocalDate.of(2010, 12, 10)));

        when(individualRequestRepository.findByIdAndThirdPartyId(INDIVIDUAL_REQUEST_1_ID, THIRD_PARTY_ID_1))
                .thenReturn(Optional.of(individualRequest1));
        when(individualRequestRepository.findByIdAndThirdPartyId(INDIVIDUAL_REQUEST_1_ID, NOT_EXISTING_THIRD_PARTY_ID))
                .thenReturn(Optional.empty());
        when(individualRequestRepository.findByIdAndThirdPartyId(NOT_EXISTING_INDIVIDUAL_REQUEST_ID, THIRD_PARTY_ID_1))
                .thenReturn(Optional.empty());
        when(individualRequestRepository.findByIdAndThirdPartyId(NOT_EXISTING_INDIVIDUAL_REQUEST_ID, NOT_EXISTING_THIRD_PARTY_ID))
                .thenReturn(Optional.empty());
    }

    private void setUpUserRepository() {
        when(userRepository.getAggregateData(groupRequest1.getAggregatorOperator(), groupRequest1.getRequestType(),
                filterStatements)).thenReturn(GROUP_REQUEST_RESULT);
    }

    private void setUpHealthDataRepository() {
        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(
                individualRequest1.getStartDate().toLocalDate(), LocalTime.MIN));
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(
                individualRequest1.getEndDate().toLocalDate(), LocalTime.MAX));

        List<HealthData> healthDataList = new ArrayList<>();
        healthDataList.add(healthData1);
        healthDataList.add(healthData2);
        healthDataList.add(healthData3);
        when(healthDataRepository.findAllByUserAndTimestampBetween(user1, startTimestamp, endTimestamp))
                .thenReturn(healthDataList);
    }

    private void setUpPositionDataRepository() {
        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(
                individualRequest1.getStartDate().toLocalDate(), LocalTime.MIN));
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(
                individualRequest1.getEndDate().toLocalDate(), LocalTime.MAX));

        List<PositionData> positionDataList = new ArrayList<>();
        positionDataList.add(positionData1);
        positionDataList.add(positionData2);
        when(positionDataRepository.findAllByUserAndTimestampBetween(user1, startTimestamp, endTimestamp))
                .thenReturn(positionDataList);
    }

    @After
    public void tearDown() throws Exception {
        accessDataService = null;
    }

    /**
     * Test get individual request data when the individual request id and third party id are correct
     *
     * @throws Exception no exception expected
     */
    @Test
    public void getIndividualRequestDataSuccessful() throws Exception {
        List<PositionData> positionDataList = new ArrayList<>();
        positionDataList.add(positionData1);
        positionDataList.add(positionData2);

        List<HealthData> healthDataList = new ArrayList<>();
        healthDataList.add(healthData1);
        healthDataList.add(healthData2);
        healthDataList.add(healthData3);

        DataWrapper output = new DataWrapper();
        output.setHealthDataList(healthDataList);
        output.setPositionDataList(positionDataList);

        assertEquals(output, accessDataService.getIndividualRequestData(THIRD_PARTY_ID_1, INDIVIDUAL_REQUEST_1_ID));
    }

    /**
     * Test get individual request data method when the third party does not exist
     *
     * @throws Exception IndividualRequestNotFoundException
     */
    @Test(expected = IndividualRequestNotFoundException.class)
    public void getIndividualRequestDataNotExistingThirdParty() throws Exception {
        accessDataService.getIndividualRequestData(NOT_EXISTING_THIRD_PARTY_ID, INDIVIDUAL_REQUEST_1_ID);
    }

    /**
     * Test get individual request data method when the individual request does not exist
     *
     * @throws Exception IndividualRequestNotFoundException
     */
    @Test(expected = IndividualRequestNotFoundException.class)
    public void getIndividualRequestDataNotExistingIndividualRequest() throws Exception {
        accessDataService.getIndividualRequestData(THIRD_PARTY_ID_1, NOT_EXISTING_INDIVIDUAL_REQUEST_ID);
    }

    /**
     * Test get individual request data method when the individual request and third party do not exist
     *
     * @throws Exception IndividualRequestNotFoundException
     */
    @Test(expected = IndividualRequestNotFoundException.class)
    public void getIndividualRequestDataNotExistingIndividualRequestAndThirdPartyId() throws Exception {
        accessDataService.getIndividualRequestData(NOT_EXISTING_THIRD_PARTY_ID, NOT_EXISTING_INDIVIDUAL_REQUEST_ID);
    }

    /**
     * Test get group request data when the group request id and third party id are correct
     *
     * @throws Exception no exception expected
     */
    @Test
    public void getGroupRequestDataSuccessful() throws Exception {
        assertEquals(GROUP_REQUEST_RESULT, accessDataService.getGroupRequestData(THIRD_PARTY_ID_1, GROUP_REQUEST_1_ID));
    }

    /**
     * Test get group request data when the third party does not exist
     *
     * @throws Exception GroupRequestNotFoundException
     */
    @Test(expected = GroupRequestNotFoundException.class)
    public void getGroupRequestDataNotExistingThirdParty() throws Exception {
        accessDataService.getGroupRequestData(NOT_EXISTING_THIRD_PARTY_ID, GROUP_REQUEST_1_ID);
    }

    /**
     * Test get group request data when the individual request id and third party id are correct
     *
     * @throws Exception GroupRequestNotFoundException
     */
    @Test(expected = GroupRequestNotFoundException.class)
    public void getGroupRequestDataNotExistingGroupRequest() throws Exception {
        accessDataService.getGroupRequestData(THIRD_PARTY_ID_1, NOT_EXISTING_GROUP_REQUEST_ID);
    }

    /**
     * Test get group request data when the group request and third party do not exist
     *
     * @throws Exception GroupRequestNotFoundException
     */
    @Test(expected = GroupRequestNotFoundException.class)
    public void getGroupRequestDataNotExistingGroupRequestAndThirdParty() throws Exception {
        accessDataService.getGroupRequestData(NOT_EXISTING_THIRD_PARTY_ID, NOT_EXISTING_GROUP_REQUEST_ID);
    }

}