package com.trackme.trackmeapplication.sharedData;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private String firstName;
    private String lastName;
    private String ssn;
    private String username;
    private String password;
    private Date birthDate;
    private String birthCity;
    private String birthNation;


    public User(String ssn, String username, String password, String firstName, String lastName, Date birthDate, String birthCity, String birthNation) {
        this.ssn = ssn;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.birthCity = birthCity;
        this.birthNation = birthNation;

    }

    public String getUsername() { return username; }

    public String   getName() {
        return firstName + " " + lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getSsn() {
        return ssn;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public String getBirthNation() {
        return birthNation;
    }
}
