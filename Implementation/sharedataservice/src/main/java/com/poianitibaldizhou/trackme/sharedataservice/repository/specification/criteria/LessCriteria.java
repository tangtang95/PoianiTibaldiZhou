package com.poianitibaldizhou.trackme.sharedataservice.repository.specification.criteria;

import com.poianitibaldizhou.trackme.sharedataservice.entity.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class LessCriteria implements OperationCriteria {

    @Override
    public Predicate getCriteria(SearchCriteria criteria, Root<User> root, CriteriaBuilder builder) {
        return builder.lessThanOrEqualTo(
                root.<String> get(criteria.getKey()), criteria.getValue().toString());
    }
}
