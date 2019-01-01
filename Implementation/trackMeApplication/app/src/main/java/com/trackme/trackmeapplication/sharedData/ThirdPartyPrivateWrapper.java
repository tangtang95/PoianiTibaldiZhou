package com.trackme.trackmeapplication.sharedData;

import java.io.Serializable;

public class ThirdPartyPrivateWrapper implements Serializable {
    private ThirdPartyCustomer thirdPartyCustomer;
    private PrivateThirdPartyDetail privateThirdPartyDetail;

    public void setPrivateThirdPartyDetail(PrivateThirdPartyDetail privateThirdPartyDetail) {
        this.privateThirdPartyDetail = privateThirdPartyDetail;
    }

    public void setThirdPartyCustomer(ThirdPartyCustomer thirdPartyCustomer) {
        this.thirdPartyCustomer = thirdPartyCustomer;
    }

    public PrivateThirdPartyDetail getPrivateThirdPartyDetail() {
        return privateThirdPartyDetail;
    }

    public ThirdPartyCustomer getThirdPartyCustomer() {
        return thirdPartyCustomer;
    }
}
