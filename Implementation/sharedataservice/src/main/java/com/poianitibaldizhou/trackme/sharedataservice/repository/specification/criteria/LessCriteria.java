package com.poianitibaldizhou.trackme.sharedataservice.repository.specification.criteria;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class LessCriteria implements OperationCriteria {

    @Override
    public Predicate getCriteria(FilterStatement filter, Root<?> root, CriteriaBuilder builder) {
        return builder.lessThan(root.get(filter.getColumn()), filter.getValue());
    }
}
