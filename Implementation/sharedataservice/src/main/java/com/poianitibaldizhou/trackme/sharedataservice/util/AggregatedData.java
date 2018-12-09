package com.poianitibaldizhou.trackme.sharedataservice.util;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
public class AggregatedData {

    @JsonView(Views.Internal.class)
    private Long thirdPartyId;

    @JsonView(Views.Internal.class)
    private Long groupRequestId;

    private Double value;


    public static AggregatedData newAggregatedData(Long thirdPartyId, Long groupRequestId, Double value) {
        AggregatedData aggregatedData = new AggregatedData();
        aggregatedData.setThirdPartyId(thirdPartyId);
        aggregatedData.setGroupRequestId(groupRequestId);
        aggregatedData.setValue(value);
        return aggregatedData;
    }
}
