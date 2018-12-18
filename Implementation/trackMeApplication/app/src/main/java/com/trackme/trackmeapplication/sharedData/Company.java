package com.trackme.trackmeapplication.sharedData;

public class Company implements ThirdPartyInterface{

    private String companyName;
    private String email;
    private String password;
    private String address;
    private String dunsNumber;

    public Company(String companyName, String email, String password, String address, String dunsNumber) {
        this.companyName = companyName;
        this.email = email;
        this.password = password;
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
    public String getName() {
        return companyName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
