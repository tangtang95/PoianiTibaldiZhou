package com.trackme.trackmeapplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackme.trackmeapplication.sharedData.CompanyDetail;
import com.trackme.trackmeapplication.sharedData.PrivateThirdPartyDetail;
import com.trackme.trackmeapplication.sharedData.ThirdPartyCompanyWrapper;
import com.trackme.trackmeapplication.sharedData.ThirdPartyCustomer;
import com.trackme.trackmeapplication.sharedData.ThirdPartyPrivateWrapper;
import com.trackme.trackmeapplication.sharedData.User;

import org.junit.Test;

import java.sql.Date;


public class ConnectionTest {


    @Test
    public void userToJson() throws JsonProcessingException {
        User user = new User("000-00-0000", "Tiba", "pass", "Mattia",
                "Tibaldi", new Date(0), "Villa","Italy");
        ObjectMapper objectMapper= new ObjectMapper();
        System.out.print(objectMapper.writeValueAsString(user).replace("\"","\\\""));
    }

    @Test
    public void userBusinessToJson() throws JsonProcessingException {
        PrivateThirdPartyDetail privateThirdPartyDetail = new PrivateThirdPartyDetail("000-00-0000",
                new ThirdPartyCustomer("a@b.c", "pass"), "Mattia", "Tibaldi", new Date(0), "Villa");
        ThirdPartyPrivateWrapper thirdPartyPrivateWrapper = new ThirdPartyPrivateWrapper();
        thirdPartyPrivateWrapper.setPrivateThirdPartyDetail(privateThirdPartyDetail);
        thirdPartyPrivateWrapper.setThirdPartyCustomer(privateThirdPartyDetail.getThirdPartyCustomer());
        ObjectMapper objectMapper= new ObjectMapper();
        System.out.print(objectMapper.writeValueAsString(thirdPartyPrivateWrapper));
    }

    @Test
    public void companyToJson() throws JsonProcessingException {
        CompanyDetail companyDetail = new CompanyDetail("Intercomp",
                new ThirdPartyCustomer("a@b.c","pass"), "12", "123");
        ThirdPartyCompanyWrapper thirdPartyCompanyWrapper = new ThirdPartyCompanyWrapper();
        thirdPartyCompanyWrapper.setCompanyDetail(companyDetail);
        thirdPartyCompanyWrapper.setThirdPartyCustomer(companyDetail.getThirdPartyCustomer());
        ObjectMapper objectMapper= new ObjectMapper();
        System.out.print(objectMapper.writeValueAsString(thirdPartyCompanyWrapper));

    }

}
