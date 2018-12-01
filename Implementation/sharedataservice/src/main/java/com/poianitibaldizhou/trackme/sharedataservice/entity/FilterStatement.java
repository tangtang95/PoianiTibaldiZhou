package com.poianitibaldizhou.trackme.sharedataservice.entity;

import com.poianitibaldizhou.trackme.sharedataservice.util.ComparisonSymbol;
import com.poianitibaldizhou.trackme.sharedataservice.util.FilterableTable;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class FilterStatement {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "filter_table", length = 10, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private FilterableTable table;

    @Column(name = "filter_column", length = 20, nullable = false)
    private String column;

    @Column(length = 50, nullable = false)
    private String value;

    @Column(length = 10, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ComparisonSymbol comparisonSymbol = ComparisonSymbol.EQUALS;

    @ManyToOne
    @JoinColumn(name = "group_request_id", nullable = false)
    private GroupRequest groupRequest;

}
