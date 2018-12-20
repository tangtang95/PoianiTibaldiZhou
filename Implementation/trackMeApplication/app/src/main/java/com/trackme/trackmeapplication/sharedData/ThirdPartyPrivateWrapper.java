package com.trackme.trackmeapplication.sharedData;

import java.io.Serializable;

public class ThirdPartyPrivateWrapper implements Serializable {
    private PrivateThirdPartyDetail privateThirdPartyDetail;

    public void setPrivateThirdPartyDetail(PrivateThirdPartyDetail privateThirdPartyDetail) {
        this.privateThirdPartyDetail = privateThirdPartyDetail;
    }
}
