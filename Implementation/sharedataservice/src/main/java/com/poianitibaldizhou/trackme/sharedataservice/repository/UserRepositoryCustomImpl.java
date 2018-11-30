package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.QHealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.QPositionData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class UserRepositoryCustomImpl implements UserRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Integer complexQueryJoinedWithData(Long requestId) {
        JPAQuery query = new JPAQuery(entityManager);
        QUser user = QUser.user;
        QHealthData healthData = QHealthData.healthData;
        QPositionData positionData = QPositionData.positionData;
        SubQueryExpression<Tuple> sq1 = query.from(healthData).select(healthData.user.ssn.as("user_ssn"),
                healthData.timestamp.as("timestamp"), healthData.heartBeat, healthData.pressureMin,
                healthData.pressureMax, healthData.bloodOxygenLevel, null, null);
        SubQueryExpression<Tuple> sq2 = query.from(positionData).select(positionData.user.ssn.as("user_ssn"),
                positionData.timestamp.as("timestamp"),
                null, null, null, null, positionData.latitude, positionData.longitude);
        query.from(user).
        return null;
    }
}
