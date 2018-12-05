package com.poianitibaldizhou.trackme.sharedataservice.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QIndividualRequest is a Querydsl query type for QIndividualRequest
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QIndividualRequest extends com.querydsl.sql.RelationalPathBase<QIndividualRequest> {

    private static final long serialVersionUID = -974374657;

    public static final QIndividualRequest individualRequest = new QIndividualRequest("individual_request");

    public final DatePath<java.sql.Date> endDate = createDate("endDate", java.sql.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.sql.Date> startDate = createDate("startDate", java.sql.Date.class);

    public final NumberPath<Long> thirdPartyId = createNumber("thirdPartyId", Long.class);

    public final DateTimePath<java.sql.Timestamp> timestamp = createDateTime("timestamp", java.sql.Timestamp.class);

    public final StringPath userSsn = createString("userSsn");

    public final com.querydsl.sql.PrimaryKey<QIndividualRequest> primary = createPrimaryKey(id);

    public QIndividualRequest(String variable) {
        super(QIndividualRequest.class, forVariable(variable), "null", "individual_request");
        addMetadata();
    }

    public QIndividualRequest(String variable, String schema, String table) {
        super(QIndividualRequest.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QIndividualRequest(String variable, String schema) {
        super(QIndividualRequest.class, forVariable(variable), schema, "individual_request");
        addMetadata();
    }

    public QIndividualRequest(Path<? extends QIndividualRequest> path) {
        super(path.getType(), path.getMetadata(), "null", "individual_request");
        addMetadata();
    }

    public QIndividualRequest(PathMetadata metadata) {
        super(QIndividualRequest.class, metadata, "null", "individual_request");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(endDate, ColumnMetadata.named("end_date").withIndex(2).ofType(Types.DATE).withSize(10).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(startDate, ColumnMetadata.named("start_date").withIndex(3).ofType(Types.DATE).withSize(10).notNull());
        addMetadata(thirdPartyId, ColumnMetadata.named("third_party_id").withIndex(4).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(timestamp, ColumnMetadata.named("timestamp").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(userSsn, ColumnMetadata.named("user_ssn").withIndex(6).ofType(Types.VARCHAR).withSize(16).notNull());
    }

}

