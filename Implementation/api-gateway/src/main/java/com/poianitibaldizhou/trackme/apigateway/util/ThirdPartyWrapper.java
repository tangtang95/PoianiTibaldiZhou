package com.poianitibaldizhou.trackme.apigateway.util;

import com.poianitibaldizhou.trackme.apigateway.entity.ThirdPartyCustomer;
import lombok.Data;

import java.io.Serializable;

@Data
public class ThirdPartyWrapper implements Serializable {
    private ThirdPartyCustomer thirdPartyCustomer;
}
