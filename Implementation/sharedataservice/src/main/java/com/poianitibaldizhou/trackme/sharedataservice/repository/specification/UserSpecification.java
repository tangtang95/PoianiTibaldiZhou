package com.poianitibaldizhou.trackme.sharedataservice.repository.specification;

import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import com.poianitibaldizhou.trackme.sharedataservice.repository.specification.criteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserSpecification implements Specification<User>{

    private SearchCriteria searchCriteria;

    public UserSpecification(SearchCriteria searchCriteria){
        this.searchCriteria = searchCriteria;
    }

    @Nullable
    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return searchCriteria.getOperation().getCriteria(searchCriteria, root, criteriaBuilder);
    }

}
