package com.poianitibaldizhou.trackme.sharedataservice.util;


import java.sql.Timestamp;

import static com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QUnionDataPath.*;

/**
 * Type of field that can be used on a filter statement
 */
public enum FieldType {
    TIMESTAMP(Timestamp.class, ALIAS_TIMESTAMP), LATITUDE(Double.class, ALIAS_LATITUDE),
    LONGITUDE(Double.class, ALIAS_LONGITUDE), HEART_BEAT(Integer.class, ALIAS_HEARTBEAT),
    PRESSURE_MIN(Integer.class, ALIAS_PRESSURE_MIN), PRESSURE_MAX(Integer.class, ALIAS_PRESSURE_MAX),
    BLOOD_OXYGEN_LEVEL(Integer.class, ALIAS_BLOOD_OXYGEN_LEVEL);

    private Class<?> fieldClass;
    private String fieldName;

    /**
     * Constructor.
     * Create a FieldType of a specific field class and the name of the field
     *
     * @param fieldClass the class of the field
     * @param fieldName the name of the field
     */
    private FieldType(Class<?> fieldClass, String fieldName){
        this.fieldClass = fieldClass;
        this.fieldName = fieldName;
    }

    /**
     * @return the field class of the fieldType
     */
    public Class<?> getFieldClass() {
        return fieldClass;
    }

    /**
     * @return the name of the field of a specific fieldType
     */
    public String getFieldName() {
        return fieldName;
    }
}
