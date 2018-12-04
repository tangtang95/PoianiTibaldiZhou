package com.poianitibaldizhou.trackme.sharedataservice.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Type of aggregator functions regarding the group request
 */
public enum AggregatorOperator {
    AVG, COUNT, DISTINCT_COUNT, MAX, MIN;

    /**
     * The set operation contains that returns true if THIS is inside the list of aggregators otherwise false
     *
     * @param aggregators the list of aggregators to check on
     * @return true if THIS is inside the list of aggregators otherwise false
     */
    public boolean contains(List<AggregatorOperator> aggregators) {
        return aggregators.stream().filter(this::equals).count() > 0;
    }

    /**
     * Returns the list of aggregator operators which are applicable on a number
     *
     * @return the list of aggregator operators applicable on a number
     */
    public static List<AggregatorOperator> getNumberAggregatorOperators(){
        List<AggregatorOperator> aggregatorOperators = new ArrayList<>();
        aggregatorOperators.add(AVG);
        aggregatorOperators.add(MAX);
        aggregatorOperators.add(MIN);
        return aggregatorOperators;
    }
}
