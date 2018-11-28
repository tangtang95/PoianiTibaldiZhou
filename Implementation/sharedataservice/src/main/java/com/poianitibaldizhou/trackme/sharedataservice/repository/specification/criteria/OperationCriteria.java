package com.poianitibaldizhou.trackme.sharedataservice.repository.specification.criteria;

import com.poianitibaldizhou.trackme.sharedataservice.entity.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface OperationCriteria {

    Predicate getCriteria(SearchCriteria criteria, Root<User> root, CriteriaBuilder builder);

}
