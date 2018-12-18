package com.trackme.trackmeapplication.sharedData;

public class ThirdParty implements ThirdPartyInterface {

    private String firstName;
    private String lastName;
    private String ssn;
    private String email;
    private String password;
    private String birthDay;
    private String birthCity;
    private String birthNation;


    public ThirdParty(String ssn, String email, String password, String firstName, String lastName, String birthDay, String birthCity, String birthNation) {
        this.ssn = ssn;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.birthCity = birthCity;
        this.birthNation = birthNation;

    }

    @Override
    public String getName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getSsn() {
        return ssn;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public String getBirthNation() {
        return birthNation;
    }
}
