package com.trackme.trackmeapplication.sharedData;

import java.io.Serializable;

public class ThirdPartyCustomer implements Serializable {

    private String email;
    private String password;

    public ThirdPartyCustomer(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
