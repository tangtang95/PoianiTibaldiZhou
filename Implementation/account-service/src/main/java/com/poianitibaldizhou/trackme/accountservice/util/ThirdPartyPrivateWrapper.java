package com.poianitibaldizhou.trackme.accountservice.util;

import com.poianitibaldizhou.trackme.accountservice.entity.PrivateThirdPartyDetail;
import com.poianitibaldizhou.trackme.accountservice.entity.ThirdPartyCustomer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * This class wraps the information regarding third party customer that are private (i.e. non company)
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ThirdPartyPrivateWrapper extends ThirdPartyWrapper implements Serializable {
    private PrivateThirdPartyDetail privateThirdPartyDetail;
}
