package com.poianitibaldizhou.trackme.accountservice.util;

import com.poianitibaldizhou.trackme.accountservice.entity.ThirdPartyCustomer;
import lombok.Data;

import java.io.Serializable;

@Data
public class ThirdPartyWrapper implements Serializable {
    private ThirdPartyCustomer thirdPartyCustomer;
}
