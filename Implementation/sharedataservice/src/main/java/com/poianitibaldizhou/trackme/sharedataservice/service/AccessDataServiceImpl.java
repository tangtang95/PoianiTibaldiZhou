package com.poianitibaldizhou.trackme.sharedataservice.service;

import com.poianitibaldizhou.trackme.sharedataservice.domain.QHealthData;
import com.poianitibaldizhou.trackme.sharedataservice.domain.QPositionData;
import com.poianitibaldizhou.trackme.sharedataservice.domain.QUser;
import com.poianitibaldizhou.trackme.sharedataservice.repository.*;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.mysql.MySQLQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class AccessDataServiceImpl implements AccessDataService {

    @Autowired
    private MySQLQueryFactory queryFactory;

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

    @Transactional
    @Override
    public String getGroupRequestData(Long requestId) {
        /*GroupRequest groupRequest = groupRequestRepository.findById(requestId)
                .orElseThrow(() -> new GroupRequestNotFoundException(requestId));
        List<FilterStatement> filters = filterStatementRepository.findAllByGroupRequest(groupRequest);*/
        QUser user = QUser.user;
        QHealthData healthData= QHealthData.healthData;
        QPositionData positionData = QPositionData.positionData;
        SubQueryExpression<Tuple> healthQuery = queryFactory.query().from(healthData).select(healthData.userSsn.as("userssn"),
                healthData.timestamp.as("timestamp1"), Expressions.as(null, positionData.latitude),
                Expressions.as(null, positionData.longitude),
                healthData.heartBeat, healthData.pressureMin, healthData.pressureMax,
                healthData.bloodOxygenLevel);
        SubQueryExpression<Tuple> positionQuery = queryFactory.query().from(positionData).select(positionData.userSsn.as("userssn"),
                positionData.timestamp.as("timestamp1"), positionData.latitude, positionData.longitude,
                Expressions.as(null , healthData.heartBeat), Expressions.as(null, healthData.pressureMin),
                Expressions.as(null, healthData.pressureMax), Expressions.as(null, healthData.bloodOxygenLevel));
        Expression<Tuple> unionData = queryFactory.query().union(positionQuery, healthQuery).as("unionData");
        List<Tuple> list = queryFactory.query().select(user.all()).from(user)
                .from(unionData).fetch();
        /*for (FilterStatement filter: filters) {

        }*/
        return list.toString();
    }
}
