package com.poianitibaldizhou.trackme.sharedataservice.message.protocol;

import lombok.Data;

import java.sql.Date;

@Data
public class UserProtocolMessage {

    private String ssn;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String birthCity;
    private String birthNation;

}
