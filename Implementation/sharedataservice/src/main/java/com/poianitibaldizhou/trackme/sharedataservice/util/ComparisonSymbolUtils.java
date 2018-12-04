package com.poianitibaldizhou.trackme.sharedataservice.util;

import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;

import java.util.Map;

/**
 * The utility class regarding ComparisonSymbol
 */
public class ComparisonSymbolUtils {

    private ComparisonSymbolUtils(){}

    /**
     * Returns the counterpart operator (of queryDsl) about the comparisonSymbol given
     *
     * @param comparisonSymbol the comparison symbol to be mapped
     * @return the counter part operator of the comparisonSymbol
     */
    public static Operator getOperator(ComparisonSymbol comparisonSymbol) {
        Map<String, Operator> operators = ImmutableMap.<String, Operator>builder()
                .put(ComparisonSymbol.EQUALS.name(), Ops.EQ)
                .put(ComparisonSymbol.NOT_EQUALS.name(), Ops.NE)
                .put(ComparisonSymbol.LESS.name(), Ops.LT)
                .put(ComparisonSymbol.GREATER.name(), Ops.GT)
                .build();

        return operators.get(comparisonSymbol.name());
    }

}
