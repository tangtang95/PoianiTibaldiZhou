package com.poianitibaldizhou.trackme.individualrequestservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

/**
 * Information regarding the blocks that users have perfomed on some third party
 */
@Data
@Entity
public class BlockedThirdParty {

    @EmbeddedId
    private BlockedThirdPartyKey key;

    @MapsId("user")
    @ManyToOne
    private User user;

    @Column(nullable = false)
    private Date blockDate;
}
