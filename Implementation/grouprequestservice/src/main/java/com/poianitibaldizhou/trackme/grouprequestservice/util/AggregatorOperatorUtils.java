package com.poianitibaldizhou.trackme.grouprequestservice.util;


import com.google.common.collect.ImmutableMap;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.enumerator.AggregatorOperatorProtocolMessage;

import java.util.Map;

/**
 * The utility class regarding AggregatorOperator
 */
public class AggregatorOperatorUtils {

    private AggregatorOperatorUtils(){}

    /**
     * Returns the aggregator operator of share data service w.r.t. the one of the protocol
     *
     * @param aggregatorOperatorProtocol the aggregator operator of the protocol to be mapped
     * @return the counterpart aggregator operator of the aggregator operator of the protocol
     */
    public static AggregatorOperator getAggregatorOperator(AggregatorOperatorProtocolMessage aggregatorOperatorProtocol){
        Map<String, AggregatorOperator> operators = ImmutableMap.<String, AggregatorOperator>builder()
                .put(AggregatorOperatorProtocolMessage.COUNT.name(), AggregatorOperator.COUNT)
                .put(AggregatorOperatorProtocolMessage.DISTINCT_COUNT.name(), AggregatorOperator.DISTINCT_COUNT)
                .put(AggregatorOperatorProtocolMessage.AVG.name(), AggregatorOperator.AVG)
                .put(AggregatorOperatorProtocolMessage.MAX.name(), AggregatorOperator.MAX)
                .put(AggregatorOperatorProtocolMessage.MIN.name(), AggregatorOperator.MIN)
                .build();
        return operators.get(aggregatorOperatorProtocol.name());
    }
}
