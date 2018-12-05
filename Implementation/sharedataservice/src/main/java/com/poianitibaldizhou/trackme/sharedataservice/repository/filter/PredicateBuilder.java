package com.poianitibaldizhou.trackme.sharedataservice.repository.filter;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QUnionDataPath;
import com.poianitibaldizhou.trackme.sharedataservice.util.ComparisonSymbolUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder pattern class to create dynamic predicate based on filter statements
 */
public class PredicateBuilder {

    private List<FilterStatement> filterStatements;

    /**
     * Constructor.
     * Create a new predicate builder by initializing the list of filter statements
     */
    public PredicateBuilder(){
        filterStatements = new ArrayList<>();
    }

    /**
     * Add a filter statement to the list of filterStatements
     *
     * @param filterStatement the filter statement to be added
     * @return true if the insertion is successful otherwise false
     */
    public boolean addFilterStatement(FilterStatement filterStatement){
        return filterStatements.add(filterStatement);
    }

    /**
     * Builds a where expression containing all the constraint defined by all the filter statements concatenated by
     * an AND
     *
     * @param unionDataPath the QUnionDataPath of a specific alias of the union data (health data + position data)
     * @return the where predicate containing all the constraint defined by filter statements
     */
    public Predicate build(QUnionDataPath unionDataPath){
        BooleanBuilder where = new BooleanBuilder();
        for(FilterStatement filterStatement: filterStatements) {
            Operator operator = ComparisonSymbolUtils.getOperator(filterStatement.getComparisonSymbol());

            Path<?> propPath = Expressions.path(filterStatement.getColumn().getFieldClass(), unionDataPath.alias,
                    filterStatement.getColumn().getFieldName());

            Predicate predicate = Expressions.predicate(operator, propPath,
                    Expressions.constant(filterStatement.getValue()));
            where.and(predicate);
        }
        return where;
    }

}
