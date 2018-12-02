package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QHealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QPositionData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QUser;
import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.entity.GroupRequest;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLTemplates;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.List;

/**
 * Implementation of the custom user repository
 */
@Slf4j
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Double complexUnionQuery(GroupRequest groupRequest, List<FilterStatement> filters) {
        QUser user = QUser.user;
        QHealthData healthData= QHealthData.healthData;
        QPositionData positionData = QPositionData.positionData;
        Path<Void> unionData = Expressions.path(Void.class, "unionData");
        Path<String> userSsn = Expressions.stringPath(unionData, "userSsn");
        Path<Timestamp> timestamp = Expressions.dateTimePath(Timestamp.class, unionData,"timestamp");
        Path<Double> latitude = Expressions.numberPath(Double.class, unionData,"latitude");
        Path<Double> longitude = Expressions.numberPath(Double.class, unionData,"longitude");
        Path<Integer> heartBeat = Expressions.numberPath(Integer.class, unionData,"heartBeat");
        Path<Integer> pressureMin = Expressions.numberPath(Integer.class, unionData,"pressureMin");
        Path<Integer> pressureMax = Expressions.numberPath(Integer.class, unionData,"pressureMax");
        Path<Integer> bloodOxygenLevel = Expressions.numberPath(Integer.class, unionData,"bloodOxygenLevel");

        JPASQLQuery healthQuery = query().from(healthData).select(
                healthData.userSsn.as(userSsn),
                healthData.timestamp.as(timestamp),
                Expressions.as(null, latitude),
                Expressions.as(null, longitude),
                healthData.heartBeat.as(heartBeat),
                healthData.pressureMin.as(pressureMin),
                healthData.pressureMax.as(pressureMax),
                healthData.bloodOxygenLevel.as(bloodOxygenLevel));
        JPASQLQuery positionQuery = query().from(positionData).select(
                positionData.userSsn.as(userSsn),
                positionData.timestamp.as(timestamp),
                positionData.latitude.as(latitude),
                positionData.longitude.as(longitude),
                Expressions.as(null , heartBeat),
                Expressions.as(null, pressureMin),
                Expressions.as(null, pressureMax),
                Expressions.as(null, bloodOxygenLevel));
        Path joinData = Expressions.path(Void.class, "joinData");
        JPASQLQuery unionQuery = query().select(Expressions.path(Tuple.class, "*")).from(query()
                .union(healthQuery, positionQuery).as(joinData));
        List<Timestamp> result = query().select(timestamp).from(user).join(unionQuery, unionData)
                .on(user.ssn.eq(userSsn)).fetch();
        log.info(result.toString());
        return 1D;
    }

    private JPASQLQuery<?> query(){
        SQLTemplates templates = MySQLTemplates.builder().build();
        return new JPASQLQuery<>(entityManager, templates);
    }
}
