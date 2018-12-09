package com.poianitibaldizhou.trackme.sharedataservice.message.protocol;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.ComparisonSymbolProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.FieldTypeProtocolMessage;
import lombok.Data;

@Data
public class FilterStatementProtocolMessage {

    private FieldTypeProtocolMessage column;
    private String value;
    private ComparisonSymbolProtocolMessage comparisonSymbol;

}
