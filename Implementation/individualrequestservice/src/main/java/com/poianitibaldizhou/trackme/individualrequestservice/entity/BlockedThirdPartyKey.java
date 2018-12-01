package com.poianitibaldizhou.trackme.individualrequestservice.entity;

import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Key for the BlockedThirdParty entity
 */
@Embeddable
@EqualsAndHashCode
public class BlockedThirdPartyKey implements Serializable {

    @Column(name = "thirdPartyID", nullable = false)
    private Long thirdPartyID;

    @ManyToOne
    @JoinColumn(name = "ssn", nullable = false)
    private User user;

    /**
     * Creates a key for the BlockedThirdParty entity.
     * A key is formed by two fields: thirdPartyID and ssn.
     *
     * @param thirdPartyID third party that will be blocked to access data regarding user specified by the ssn
     * @param user user that blocked the third party identified by thirdPartyID
     */
    public BlockedThirdPartyKey(Long thirdPartyID, User user) {
        this.thirdPartyID = thirdPartyID;
        this.user = user;
    }

    public Long getThirdPartyID() {
        return thirdPartyID;
    }

    public void setThirdPartyID(Long thirdPartyID) {
        this.thirdPartyID = thirdPartyID;
    }

    public User getUser() {
        return user;
    }

    public void setSsn(User user) {
        this.user = user;
    }

}
