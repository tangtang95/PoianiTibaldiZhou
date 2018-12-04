package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.ShareDataServiceApplication;
import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatorOperator;
import com.poianitibaldizhou.trackme.sharedataservice.util.ComparisonSymbol;
import com.poianitibaldizhou.trackme.sharedataservice.util.FieldType;
import com.poianitibaldizhou.trackme.sharedataservice.util.RequestType;
import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(value = Enclosed.class)
public class UserRepositoryCustomImplUnitTest {

    @RunWith(value = Parameterized.class)
    public static class ParameterizedPart {

        @ClassRule
        public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

        @Rule
        public final SpringMethodRule springMethodRule = new SpringMethodRule();

        private GroupRequest groupRequest;
        private List<FilterStatement> filterStatementList;
        private Double result;

        static FilterStatement newFilterStatement(FieldType fieldType, ComparisonSymbol symbol, String value) {
            FilterStatement filterStatement = new FilterStatement();
            filterStatement.setColumn(fieldType);
            filterStatement.setComparisonSymbol(symbol);
            filterStatement.setValue(value);
            return filterStatement;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {AggregatorOperator.COUNT, RequestType.ALL,
                            Arrays.asList(newFilterStatement(FieldType.TIMESTAMP, ComparisonSymbol.EQUALS, new Timestamp(0).toString())),
                            1D},
                    {AggregatorOperator.DISTINCT_COUNT, RequestType.BIRTH_YEAR,
                            Arrays.asList(newFilterStatement(FieldType.BLOOD_OXYGEN_LEVEL, ComparisonSymbol.GREATER, "20")),
                            1D}
            });
        }

        public ParameterizedPart(AggregatorOperator aggregatorOperator, RequestType requestType,
                                 List<FilterStatement> filterStatements, Double result) {
            this.groupRequest = new GroupRequest();
            this.groupRequest.setAggregatorOperator(aggregatorOperator);
            this.groupRequest.setRequestType(requestType);
            this.filterStatementList = filterStatements;
            this.result = result;
        }

        @Before
        public void setUp() throws Exception {
        }

        @After
        public void tearDown() throws Exception {
        }

        @Test
        public void getAggregateData() throws Exception {
        }

        @Test
        public void getNumberOfPeopleInvolved() throws Exception {
        }
    }

}