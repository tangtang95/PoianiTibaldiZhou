package com.poianitibaldizhou.trackme.sharedataservice.repository.specification;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomSpecificationBuilder<A> {


    private final List<FilterStatement> filters;

    public CustomSpecificationBuilder(){
        filters = new ArrayList<>();
    }

    public CustomSpecificationBuilder with(FilterStatement filter){
        filters.add(filter);
        return this;
    }

    public Specification<A> build(){
        List<Specification<A>> specifications = filters.stream()
                .map(CustomSpecification<A>::new).collect(Collectors.toList());

        Specification<A> result = new CustomSpecification<>(filters.get(0));
        for (int i = 1; i < filters.size(); i++) {
            result = Specification.where(result).and(specifications.get(i)) ;
        }
        return result;
    }

}
