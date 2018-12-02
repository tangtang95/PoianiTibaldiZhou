package com.poianitibaldizhou.trackme.sharedataservice.service;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.sharedataservice.exception.GroupRequestNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Implementation of the access data service.
 */
@Slf4j
@Service
public class AccessDataServiceImpl implements AccessDataService {

    private UserRepository userRepository;
    private HealthDataRepository healthDataRepository;
    private PositionDataRepository positionDataRepository;
    private GroupRequestRepository groupRequestRepository;
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
                                 FilterStatementRepository filterStatementRepository){
        this.userRepository = userRepository;
        this.healthDataRepository = healthDataRepository;
        this.positionDataRepository = positionDataRepository;
        this.groupRequestRepository = groupRequestRepository;
        this.filterStatementRepository = filterStatementRepository;
    }

    @Override
    public String getIndividualRequestData(Long requestId) {
        return null;
    }

    @Transactional
    @Override
    public String getGroupRequestData(Long requestId) {
        GroupRequest groupRequest = groupRequestRepository.findById(requestId)
                .orElseThrow(() -> new GroupRequestNotFoundException(requestId));
        List<FilterStatement> filters = filterStatementRepository.findAllByGroupRequest(groupRequest);
        log.info(userRepository.complexUnionQuery(groupRequest, filters).toString());
        return null;
    }
}
