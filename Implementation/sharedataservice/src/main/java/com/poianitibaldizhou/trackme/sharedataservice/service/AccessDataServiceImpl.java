package com.poianitibaldizhou.trackme.sharedataservice.service;

import com.poianitibaldizhou.trackme.sharedataservice.entity.*;
import com.poianitibaldizhou.trackme.sharedataservice.exception.GroupRequestNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.exception.IndividualRequestNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.repository.*;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatedData;
import com.poianitibaldizhou.trackme.sharedataservice.util.DataWrapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Implementation of the access data service.
 */
@Service
public class AccessDataServiceImpl implements AccessDataService {

    private UserRepository userRepository;
    private HealthDataRepository healthDataRepository;
    private PositionDataRepository positionDataRepository;
    private GroupRequestRepository groupRequestRepository;
    private IndividualRequestRepository individualRequestRepository;
    private FilterStatementRepository filterStatementRepository;

    /**
     * Constructor.
     * Initialize SendDataService with autowired @repositories
     *
     * @param userRepository the @repository of users
     * @param healthDataRepository the @repository of health data
     * @param positionDataRepository the @repository of position data
     * @param groupRequestRepository the @repository of group requests
     * @param filterStatementRepository the @repository of filter statements
     */
    public AccessDataServiceImpl(UserRepository userRepository, HealthDataRepository healthDataRepository,
                                 PositionDataRepository positionDataRepository,
                                 GroupRequestRepository groupRequestRepository,
                                 IndividualRequestRepository individualRequestRepository,
                                 FilterStatementRepository filterStatementRepository){
        this.userRepository = userRepository;
        this.healthDataRepository = healthDataRepository;
        this.positionDataRepository = positionDataRepository;
        this.groupRequestRepository = groupRequestRepository;
        this.individualRequestRepository = individualRequestRepository;
        this.filterStatementRepository = filterStatementRepository;
    }

    @Override
    public DataWrapper getIndividualRequestData(Long thirdPartyId, Long requestId) {
        IndividualRequest individualRequest = individualRequestRepository.findByIdAndThirdPartyId(requestId, thirdPartyId)
                .orElseThrow(() -> new IndividualRequestNotFoundException(requestId));

        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(
                individualRequest.getStartDate().toLocalDate(), LocalTime.MIN));
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(
                individualRequest.getEndDate().toLocalDate(), LocalTime.MAX));
        List<HealthData> healthDataList = healthDataRepository.findAllByUserAndTimestampBetween(
                individualRequest.getUser(), startTimestamp, endTimestamp);
        List<PositionData> positionDataList = positionDataRepository.findAllByUserAndTimestampBetween(
                individualRequest.getUser(), startTimestamp, endTimestamp);

        DataWrapper dataWrapper = new DataWrapper();
        healthDataList.forEach(dataWrapper::addHealthData);
        positionDataList.forEach(dataWrapper::addPositionData);
        dataWrapper.setPositionDataList(positionDataList);
        dataWrapper.setHealthDataList(healthDataList);
        return dataWrapper;

    }

    @Transactional
    @Override
    public AggregatedData getGroupRequestData(Long thirdPartyId, Long requestId) {
        GroupRequest groupRequest = groupRequestRepository.findByIdAndThirdPartyId(requestId, thirdPartyId)
                .orElseThrow(() -> new GroupRequestNotFoundException(requestId));
        List<FilterStatement> filters = filterStatementRepository.findAllByGroupRequest_Id(requestId);
        AggregatedData result = new AggregatedData();
        result.setThirdPartyId(thirdPartyId);
        result.setGroupRequestId(requestId);
        result.setValue(userRepository.getAggregatedData(groupRequest.getAggregatorOperator(),
                groupRequest.getRequestType(), filters));
        return result;
    }
}
