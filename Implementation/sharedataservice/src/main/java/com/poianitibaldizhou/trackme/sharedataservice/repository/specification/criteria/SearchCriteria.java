package com.poianitibaldizhou.trackme.sharedataservice.repository.specification.criteria;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@Immutable
public class SearchCriteria {
    private String key;
    private OperationCriteria operation;
    private Object value;

    public SearchCriteria(String key, OperationCriteria operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
}
