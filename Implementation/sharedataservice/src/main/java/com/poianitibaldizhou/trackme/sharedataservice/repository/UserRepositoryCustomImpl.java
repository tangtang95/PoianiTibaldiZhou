package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QHealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QPositionData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QUnionDataPath;
import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QUser;
import com.poianitibaldizhou.trackme.sharedataservice.repository.filter.PredicateBuilder;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatorOperator;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatorOperatorUtils;
import com.poianitibaldizhou.trackme.sharedataservice.util.RequestType;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLTemplates;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Implementation of the custom user repository
 */
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Double getAggregateData(AggregatorOperator aggregatorOperator, RequestType requestType, List<FilterStatement> filters) {
        QUser user = QUser.user;
        QHealthData healthData= QHealthData.healthData;
        QPositionData positionData = QPositionData.positionData;
        QUnionDataPath unionDataPath = new QUnionDataPath("unionData");

        JPASQLQuery healthQuery = query().from(healthData).select(
                healthData.userSsn.as(unionDataPath.userSsn),
                healthData.timestamp.as(unionDataPath.timestamp),
                Expressions.as(null, unionDataPath.latitude),
                Expressions.as(null, unionDataPath.longitude),
                healthData.heartBeat.as(unionDataPath.heartBeat),
                healthData.pressureMin.as(unionDataPath.pressureMin),
                healthData.pressureMax.as(unionDataPath.pressureMax),
                healthData.bloodOxygenLevel.as(unionDataPath.bloodOxygenLevel));
        JPASQLQuery positionQuery = query().from(positionData).select(
                positionData.userSsn.as(unionDataPath.userSsn),
                positionData.timestamp.as(unionDataPath.timestamp),
                positionData.latitude.as(unionDataPath.latitude),
                positionData.longitude.as(unionDataPath.longitude),
                Expressions.as(null , unionDataPath.heartBeat),
                Expressions.as(null, unionDataPath.pressureMin),
                Expressions.as(null, unionDataPath.pressureMax),
                Expressions.as(null, unionDataPath.bloodOxygenLevel));
        Path joinData = Expressions.path(Void.class, "joinData");
        JPASQLQuery unionQuery = query().select(Expressions.path(Tuple.class, "*")).from(query()
                .union(healthQuery, positionQuery).as(joinData));
        JPASQLQuery<Double> query = query()
                .select(Expressions.numberOperation(Double.class,
                        AggregatorOperatorUtils.getAggregatorOperator(aggregatorOperator),
                        requestType.getFieldPath())).from(user)
                .join(unionQuery, unionDataPath.alias).on(user.ssn.eq(unionDataPath.userSsn));
        PredicateBuilder predicateBuilder = new PredicateBuilder();
        filters.forEach(predicateBuilder::addFilterStatement);
        List<Double> result = query.where(predicateBuilder.build(unionDataPath)).fetch();
        return result.get(0);
    }

    @Override
    public Double getNumberOfPeopleInvolved(List<FilterStatement> filters) {
        return getAggregateData(AggregatorOperator.COUNT, RequestType.USER_SSN, filters);
    }

    private JPASQLQuery<?> query(){
        SQLTemplates templates = MySQLTemplates.builder().build();
        return new JPASQLQuery<>(entityManager, templates);
    }
}
