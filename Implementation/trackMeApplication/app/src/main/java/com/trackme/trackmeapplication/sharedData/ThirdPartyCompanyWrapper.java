package com.trackme.trackmeapplication.sharedData;

import java.io.Serializable;

public class ThirdPartyCompanyWrapper implements Serializable {
    private CompanyDetail companyDetail;

    public void setCompanyDetail(CompanyDetail companyDetail) {
        this.companyDetail = companyDetail;
    }
}
