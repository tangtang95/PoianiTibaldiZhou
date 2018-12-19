package com.trackme.trackmeapplication.sharedData;

public class ThirdPartyCustomer {

    private Long id;
    private String email;
    private String password;

    public ThirdPartyCustomer(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
