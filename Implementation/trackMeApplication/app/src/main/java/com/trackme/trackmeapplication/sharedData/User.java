package com.trackme.trackmeapplication.sharedData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {

    private String username;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String birthCity;
    private String birthNation;

    private String ssn;
    private String password;

    public User(){}

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

    public String extractName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String extractSsn() {
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
