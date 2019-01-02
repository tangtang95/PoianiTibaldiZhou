package com.trackme.trackmeapplication.sharedData;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrivateThirdPartyDetail implements ThirdPartyInterface {

    private ThirdPartyCustomer thirdPartyCustomer;
    private String ssn;
    private String name;
    private String surname;
    private Date birthDate;
    private String birthCity;

    public PrivateThirdPartyDetail(){}

    public PrivateThirdPartyDetail(String ssn, ThirdPartyCustomer thirdPartyCustomer, String firstName, String lastName, Date birthDate, String birthCity) {
        this.ssn = ssn;
        this.thirdPartyCustomer = thirdPartyCustomer;
        this.name = firstName;
        this.surname = lastName;
        this.birthDate = birthDate;
        this.birthCity = birthCity;

    }

    @Override
    public String extractName() {
        return name + " " + surname;
    }

    @Override
    public String extractEmail() {
        return thirdPartyCustomer.getEmail();
    }

    @Override
    public String extractPassword() {return thirdPartyCustomer.getPassword();}

    public String getSsn() {
        return ssn;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public ThirdPartyCustomer getThirdPartyCustomer() {
        return thirdPartyCustomer;
    }

    public String getSurname() {
        return surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getName() {
        return name;
    }
}
