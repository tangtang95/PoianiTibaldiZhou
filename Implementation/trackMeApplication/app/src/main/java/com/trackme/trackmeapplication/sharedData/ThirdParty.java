package com.trackme.trackmeapplication.sharedData;

public class ThirdParty implements ThirdPartyInterface {

    private String firstName;
    private String lastName;
    private String ssn;
    private String email;
    private String password;
    private String birthDate;
    private String birthCity;
    private String birthNation;


    public ThirdParty(String ssn, String email, String password, String firstName, String lastName, String birthDate, String birthCity, String birthNation) {
        this.ssn = ssn;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
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

    public String getBirthDate() {
        return birthDate;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public String getBirthNation() {
        return birthNation;
    }
}
