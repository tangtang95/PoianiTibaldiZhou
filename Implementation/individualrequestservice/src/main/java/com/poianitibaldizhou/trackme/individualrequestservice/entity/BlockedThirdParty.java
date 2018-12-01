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

    @Column(nullable = false)
    private Date blockDate;
}
