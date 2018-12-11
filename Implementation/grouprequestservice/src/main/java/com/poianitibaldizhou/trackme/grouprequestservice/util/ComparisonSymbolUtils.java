package com.poianitibaldizhou.trackme.grouprequestservice.util;

import com.google.common.collect.ImmutableMap;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.enumerator.ComparisonSymbolProtocolMessage;

import java.util.Map;

/**
 * The utility class regarding ComparisonSymbol
 */
public class ComparisonSymbolUtils {

    private ComparisonSymbolUtils(){}

    /**
     * Returns the counterpart comparison symbols of this service about the comparisonSymbol of the protocol
     *
     * @param comparisonSymbolProtocol the comparison symbol of the protocol to be mapped
     * @return the counterpart comparison symbol from a comparison symbol of the protocol
     */
    public static ComparisonSymbol getComparisonSymbol(ComparisonSymbolProtocolMessage comparisonSymbolProtocol){
        Map<ComparisonSymbolProtocolMessage, ComparisonSymbol> comparisonSymbolsMap =
                ImmutableMap.<ComparisonSymbolProtocolMessage, ComparisonSymbol>builder()
                .put(ComparisonSymbolProtocolMessage.EQUALS, ComparisonSymbol.EQUALS)
                .put(ComparisonSymbolProtocolMessage.NOT_EQUALS, ComparisonSymbol.NOT_EQUALS)
                .put(ComparisonSymbolProtocolMessage.LESS, ComparisonSymbol.LESS)
                .put(ComparisonSymbolProtocolMessage.GREATER, ComparisonSymbol.GREATER)
                .put(ComparisonSymbolProtocolMessage.LIKE, ComparisonSymbol.LIKE)
                .build();

        return comparisonSymbolsMap.get(comparisonSymbolProtocol);
    }

}
