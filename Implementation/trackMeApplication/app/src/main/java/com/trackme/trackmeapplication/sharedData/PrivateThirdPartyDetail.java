package com.trackme.trackmeapplication.sharedData;


import java.util.Date;

public class PrivateThirdPartyDetail implements ThirdPartyInterface {

    private Long id;
    private ThirdPartyCustomer thirdPartyCustomer;
    private String ssn;
    private String name;
    private String surname;
    private Date birthDate;
    private String birthCity;


    public PrivateThirdPartyDetail(String ssn, String email, String password, String firstName, String lastName, Date birthDate, String birthCity) {
        this.ssn = ssn;
        this.thirdPartyCustomer = new ThirdPartyCustomer(email, password);
        this.name = firstName;
        this.surname = lastName;
        this.birthDate = birthDate;
        this.birthCity = birthCity;

    }

    @Override
    public String getName() {
        return name + " " + surname;
    }

    public String getSsn() {
        return ssn;
    }

    @Override
    public String getEmail() {
        return thirdPartyCustomer.getEmail();
    }

    @Override
    public String getPassword() {return thirdPartyCustomer.getPassword();}

    public Date getBirthDay() {
        return birthDate;
    }

    public String getBirthCity() {
        return birthCity;
    }

}
