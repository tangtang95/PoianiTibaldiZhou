package com.poianitibaldizhou.trackme.individualrequestservice.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.sql.Date;

@Data
@Entity
public class BlockedThirdParty {

    @EmbeddedId
    private BlockedThirdPartyKey key;

    @Column(nullable = false)
    private Date blockDate;
}
