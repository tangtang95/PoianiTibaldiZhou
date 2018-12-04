package com.poianitibaldizhou.trackme.sharedataservice.util;

import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QUnionDataPath;
import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QUser;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;

import java.sql.Timestamp;

/**
 * Type of object that can be requested from a group request
 */
public enum  RequestType {
    ALL(false, Wildcard.all),
    USER_SSN(false, Expressions.stringPath(QUnionDataPath.ALIAS_USER_SSN)),
    BIRTH_YEAR(true, DateTimeExpression.currentDate(Timestamp.class).year()),
    BIRTH_CITY(false, Expressions.stringPath(QUser.user.birthCity.getMetadata().getName())),
    HEARTBEAT(true, Expressions.numberPath(Integer.class, QUnionDataPath.ALIAS_HEARTBEAT)),
    PRESSURE_MIN(true, Expressions.numberPath(Integer.class, QUnionDataPath.ALIAS_PRESSURE_MIN)),
    PRESSURE_MAX(true, Expressions.numberPath(Integer.class, QUnionDataPath.ALIAS_PRESSURE_MAX)),
    BLOOD_OXYGEN_LEVEL(true, Expressions.numberPath(Integer.class, QUnionDataPath.ALIAS_BLOOD_OXYGEN_LEVEL));

    private boolean isNumber;
    private Expression<?> fieldPath;

    /**
     * Constructor.
     * Creates a request type which is defined by a boolean isNumber and an expression fieldPath
     *
     * @param isNumber true if the request type is a number, false otherwise
     * @param fieldPath the field path of the specific request type
     */
    private RequestType(boolean isNumber, Expression<?> fieldPath){
        this.isNumber = isNumber;
        this.fieldPath = fieldPath;
    }

    /**
     * @return true if the request type is a number, false otherwise
     */
    public boolean isNumber() {
        return isNumber;
    }

    /**
     * @return the expression of the field path (useful for dynamic query)
     */
    public Expression getFieldPath() {
        return fieldPath;
    }
}
