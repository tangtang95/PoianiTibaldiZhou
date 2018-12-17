package com.trackme.trackmeapplication.sharedData;

public class ThirdParty {

    private String companyName = null;
    private String firstName;
    private String lastName;

    public ThirdParty(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public ThirdParty(String companyName) {
        this.companyName = companyName;
    }

    public String getThirdPartyName() {
        if(companyName == null)
            return firstName + " " + lastName;
        return companyName;
    }
}
