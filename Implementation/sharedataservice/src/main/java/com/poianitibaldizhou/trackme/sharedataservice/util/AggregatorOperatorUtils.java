package com.poianitibaldizhou.trackme.sharedataservice.util;

import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;

import java.util.Map;

/**
 * The utility class regarding AggregatorOperator
 */
public class AggregatorOperatorUtils {

    private AggregatorOperatorUtils(){}

    /**
     * Returns the operator (of queryDsl, useful for dynamic query) from a enum aggregator operator
     *
     * @param aggregatorOperator the aggregator operator to be mapped
     * @return the counterpart operator of the aggregator operator given
     */
    public static Operator getAggregatorOperator(AggregatorOperator aggregatorOperator) {
        Map<String, Operator> operators = ImmutableMap.<String, Operator>builder()
                .put(AggregatorOperator.COUNT.name(), Ops.AggOps.COUNT_AGG)
                .put(AggregatorOperator.DISTINCT_COUNT.name(), Ops.AggOps.COUNT_DISTINCT_AGG)
                .put(AggregatorOperator.AVG.name(), Ops.AggOps.AVG_AGG)
                .put(AggregatorOperator.MAX.name(), Ops.AggOps.MAX_AGG)
                .put(AggregatorOperator.MIN.name(), Ops.AggOps.MIN_AGG)
                .build();

        return operators.get(aggregatorOperator.name());
    }

    /**
     * Returns if an aggregator operator is valid w.r.t. a request type
     *
     * @param operator the aggregator operator to be checked
     * @param requestType the request type to be checked
     * @return true if the aggregator operator is valid w.r.t. the request type otherwise false
     */
    public static boolean isValidOperator(AggregatorOperator operator, RequestType requestType){
        return operator.contains(AggregatorOperator.getNumberAggregatorOperators()) && requestType.isNumber();
    }
}
