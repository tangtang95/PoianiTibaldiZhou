package com.poianitibaldizhou.trackme.sharedataservice.repository.specification;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.repository.specification.criteria.CriteriaOperationFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CustomSpecification<A> implements Specification<A>{

    private FilterStatement filter;

    public CustomSpecification(FilterStatement filter){
        this.filter = filter;
    }

    @Nullable
    @Override
    public Predicate toPredicate(Root<A> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        CriteriaOperationFactory criteriaOperationFactory = new CriteriaOperationFactory();
        return criteriaOperationFactory.getOperationCriteria(filter.getComparisonSymbol())
                .getCriteria(filter, root, criteriaBuilder);
    }
}
