package com.poianitibaldizhou.trackme.sharedataservice.repository.specification.criteria;

import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Component
public class GreaterCriteria implements OperationCriteria{

    @Override
    public Predicate getCriteria(SearchCriteria criteria, Root<User> root, CriteriaBuilder builder) {
        return builder.greaterThan(
                root.get(criteria.getKey()), criteria.getValue().toString());
    }
}
