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

    public final NumberPath<Long> id = createNumber("id", Long.class);

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
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

