package com.poianitibaldizhou.trackme.sharedataservice.repository.specification.criteria;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class EqualCriteria implements OperationCriteria{

    @Override
    public Predicate getCriteria(FilterStatement filter, Root<?> root, CriteriaBuilder builder) {
        if (root.get(filter.getColumn()).getJavaType() == String.class) {
            return builder.like(
                    root.get(filter.getColumn()), "%" + filter.getValue() + "%");
        } else {
            return builder.equal(root.get(filter.getColumn()), filter.getValue());
        }
    }
}
