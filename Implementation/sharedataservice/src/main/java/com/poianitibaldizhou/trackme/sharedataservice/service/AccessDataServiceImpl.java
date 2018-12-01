package com.poianitibaldizhou.trackme.sharedataservice.service;

import com.poianitibaldizhou.trackme.sharedataservice.entity.*;
import com.poianitibaldizhou.trackme.sharedataservice.exception.GroupRequestNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.repository.*;
import com.poianitibaldizhou.trackme.sharedataservice.repository.specification.CustomSpecificationBuilder;
import com.poianitibaldizhou.trackme.sharedataservice.util.FilterableTable;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class AccessDataServiceImpl implements AccessDataService {

    @PersistenceContext
    private EntityManager entityManager;

    private UserRepository userRepository;
    private HealthDataRepository healthDataRepository;
    private PositionDataRepository positionDataRepository;
    private GroupRequestRepository groupRequestRepository;
    private FilterStatementRepository filterStatementRepository;

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

    @Override
    public String getGroupRequestData(Long requestId) {
        GroupRequest groupRequest = groupRequestRepository.findById(requestId)
                .orElseThrow(() -> new GroupRequestNotFoundException(requestId));
        JPAQuery query = new JPAQuery();


        



        return null;
    }
}
