package com.poianitibaldizhou.trackme.accountservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

/**
 * JPA entity object regarding the company details of a third party customer that is private person, and
 * not a company
 */
@Data
@Entity
public class PrivateThirdPartyDetail {

    @Id
    private Long id;

    @MapsId
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "thirdPartyCustomer")
    private ThirdPartyCustomer thirdPartyCustomer;

    @Column(nullable = false, unique = true)
    private String ssn;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private Date birthDate;

    @Column(nullable = false)
    private String birthCity;

}