package com.trackme.trackmeapplication.sharedData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyDetail implements ThirdPartyInterface{

    private ThirdPartyCustomer thirdPartyCustomer;
    private String companyName;
    private String address;
    private String dunsNumber;

    public CompanyDetail(){}

    public CompanyDetail(String companyName, ThirdPartyCustomer thirdPartyCustomer, String address, String dunsNumber) {
        this.companyName = companyName;
        this.thirdPartyCustomer = thirdPartyCustomer;
        this.address = address;
        this.dunsNumber = dunsNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getDunsNumber() {
        return dunsNumber;
    }

    @Override
    public String extractName() {
        return companyName;
    }

    @Override
    public String extractEmail() {
        return thirdPartyCustomer.getEmail();
    }

    @Override
    public String extractPassword() {
        return thirdPartyCustomer.getPassword();
    }


    public String getCompanyName() {
        return companyName;
    }

    public ThirdPartyCustomer getThirdPartyCustomer() {
        return thirdPartyCustomer;
    }
}
