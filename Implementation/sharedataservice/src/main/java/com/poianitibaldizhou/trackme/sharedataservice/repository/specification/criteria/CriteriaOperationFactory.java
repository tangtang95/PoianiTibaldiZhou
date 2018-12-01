package com.poianitibaldizhou.trackme.sharedataservice.repository.specification.criteria;

import com.poianitibaldizhou.trackme.sharedataservice.util.ComparisonSymbol;

import java.util.HashMap;
import java.util.Map;

public class CriteriaOperationFactory {

    private Map<ComparisonSymbol, OperationCriteria> operationCriteriaMapper;

    public CriteriaOperationFactory(){
        operationCriteriaMapper = new HashMap<>();
        operationCriteriaMapper.put(ComparisonSymbol.LESS, new LessCriteria());
        operationCriteriaMapper.put(ComparisonSymbol.EQUALS, new EqualCriteria());
        operationCriteriaMapper.put(ComparisonSymbol.GREATER, new GreaterCriteria());
    }

    public OperationCriteria getOperationCriteria(ComparisonSymbol comparisonSymbol) {
        return operationCriteriaMapper.get(comparisonSymbol);
    }
}
