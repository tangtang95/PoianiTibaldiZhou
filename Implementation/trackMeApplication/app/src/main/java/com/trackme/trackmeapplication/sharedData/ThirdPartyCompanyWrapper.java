package com.trackme.trackmeapplication.sharedData;

import java.io.Serializable;

public class ThirdPartyCompanyWrapper implements Serializable {
    private ThirdPartyCustomer thirdPartyCustomer;
    private CompanyDetail companyDetail;

    public void setCompanyDetail(CompanyDetail companyDetail) {
        this.companyDetail = companyDetail;
    }

    public void setThirdPartyCustomer(ThirdPartyCustomer thirdPartyCustomer) {
        this.thirdPartyCustomer = thirdPartyCustomer;
    }

    public CompanyDetail getCompanyDetail() {
        return companyDetail;
    }

    public ThirdPartyCustomer getThirdPartyCustomer() {
        return thirdPartyCustomer;
    }
}
