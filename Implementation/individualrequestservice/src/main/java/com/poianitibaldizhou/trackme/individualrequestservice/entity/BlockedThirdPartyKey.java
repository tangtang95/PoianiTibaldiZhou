package com.poianitibaldizhou.trackme.individualrequestservice.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class BlockedThirdPartyKey implements Serializable {

    @Column(name = "thirdPartyID", nullable = false)
    private Long thirdPartyID;

    @Column(name = "ssn", nullable = false, length = 16)
    private String ssn;

    public BlockedThirdPartyKey(Long thirdPartyID, String ssn) {
        this.thirdPartyID = thirdPartyID;
        this.ssn = ssn;
    }

    public Long getThirdPartyID() {
        return thirdPartyID;
    }

    public void setThirdPartyID(Long thirdPartyID) {
        this.thirdPartyID = thirdPartyID;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
}
