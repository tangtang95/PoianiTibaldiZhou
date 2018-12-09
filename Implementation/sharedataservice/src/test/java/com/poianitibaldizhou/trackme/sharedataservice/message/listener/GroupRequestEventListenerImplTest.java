package com.poianitibaldizhou.trackme.sharedataservice.message.listener;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.sharedataservice.exception.GroupRequestNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.FilterStatementProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.GroupRequestProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.*;
import com.poianitibaldizhou.trackme.sharedataservice.message.publisher.NumberOfUserInvolvedDataPublisherImpl;
import com.poianitibaldizhou.trackme.sharedataservice.repository.FilterStatementRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.GroupRequestRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.UserRepository;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatorOperator;
import com.poianitibaldizhou.trackme.sharedataservice.util.ComparisonSymbol;
import com.poianitibaldizhou.trackme.sharedataservice.util.FieldType;
import com.poianitibaldizhou.trackme.sharedataservice.util.RequestType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(Enclosed.class)
public class GroupRequestEventListenerImplTest {

    /**
     * Integration test of group request event listener without the message broker
     */
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    @ActiveProfiles("test")
    @DataJpaTest
    @RunWith(SpringRunner.class)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Sql("classpath:sql/testEventListener.sql")
    public static class IntegrationTestWithoutMessageBroker {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private GroupRequestRepository groupRequestRepository;

        @Autowired
        private FilterStatementRepository filterStatementRepository;

        @Mock
        private NumberOfUserInvolvedDataPublisherImpl numberOfUserInvolvedDataPublisher;

        private GroupRequestEventListener groupRequestEventListener;

        private GroupRequestProtocolMessage groupRequestProtocolMessage;

        @Before
        public void setUp() throws Exception {
            MockitoAnnotations.initMocks(this);
            groupRequestEventListener = new GroupRequestEventListenerImpl(userRepository, groupRequestRepository,
                    filterStatementRepository, numberOfUserInvolvedDataPublisher);
        }


        @After
        public void tearDown() throws Exception {
            numberOfUserInvolvedDataPublisher = null;
        }

        /**
         * Test on group request created when the group request protocol message is correct
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onGroupRequestCreatedSuccessful() throws Exception {
            groupRequestProtocolMessage = new GroupRequestProtocolMessage();
            groupRequestProtocolMessage.setAggregatorOperator(AggregatorOperatorProtocolMessage.AVG);
            groupRequestProtocolMessage.setRequestType(RequestTypeProtocolMessage.BLOOD_OXYGEN_LEVEL);
            groupRequestProtocolMessage.setStatus(GroupRequestStatusProtocolMessage.UNDER_ANALYSIS);

            List<FilterStatementProtocolMessage> filterStatementProtocolMessageList = new ArrayList<>();
            FilterStatementProtocolMessage filterStatementProtocolMessage = new FilterStatementProtocolMessage();
            filterStatementProtocolMessage.setColumn(FieldTypeProtocolMessage.BIRTH_CITY);
            filterStatementProtocolMessage.setComparisonSymbol(ComparisonSymbolProtocolMessage.EQUALS);
            filterStatementProtocolMessage.setValue("BRESCIA");
            filterStatementProtocolMessageList.add(filterStatementProtocolMessage);

            groupRequestProtocolMessage.setFilterStatements(filterStatementProtocolMessageList);

            groupRequestEventListener.onGroupRequestCreated(groupRequestProtocolMessage);

            verify(numberOfUserInvolvedDataPublisher, times(1)).publishNumberOfUserInvolvedData(1.0);
        }

        /**
         * Test on group request created if the group request protocol message sent is an accepted one
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onGroupRequestCreatedWithGroupRequestNotUnderAnalysis() throws Exception {
            groupRequestProtocolMessage = new GroupRequestProtocolMessage();
            groupRequestProtocolMessage.setAggregatorOperator(AggregatorOperatorProtocolMessage.AVG);
            groupRequestProtocolMessage.setRequestType(RequestTypeProtocolMessage.BLOOD_OXYGEN_LEVEL);
            groupRequestProtocolMessage.setStatus(GroupRequestStatusProtocolMessage.ACCEPTED);

            List<FilterStatementProtocolMessage> filterStatementProtocolMessageList = new ArrayList<>();
            groupRequestProtocolMessage.setFilterStatements(filterStatementProtocolMessageList);

            groupRequestEventListener.onGroupRequestCreated(groupRequestProtocolMessage);

            verify(numberOfUserInvolvedDataPublisher, times(0))
                    .publishNumberOfUserInvolvedData(anyDouble());
        }

        /**
         * Test on group request accepted when the group request protocol message sent is correct
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onGroupRequestAcceptedSuccessful() throws Exception {
            groupRequestProtocolMessage = new GroupRequestProtocolMessage();
            groupRequestProtocolMessage.setId(1L);
            groupRequestProtocolMessage.setCreationTimestamp(new Timestamp(0));
            groupRequestProtocolMessage.setStatus(GroupRequestStatusProtocolMessage.ACCEPTED);
            groupRequestProtocolMessage.setThirdPartyId(1L);
            groupRequestProtocolMessage.setAggregatorOperator(AggregatorOperatorProtocolMessage.AVG);
            groupRequestProtocolMessage.setRequestType(RequestTypeProtocolMessage.BLOOD_OXYGEN_LEVEL);

            List<FilterStatementProtocolMessage> filterStatementProtocolMessageList = new ArrayList<>();
            FilterStatementProtocolMessage filterStatementProtocolMessage = new FilterStatementProtocolMessage();
            filterStatementProtocolMessage.setColumn(FieldTypeProtocolMessage.BIRTH_CITY);
            filterStatementProtocolMessage.setComparisonSymbol(ComparisonSymbolProtocolMessage.EQUALS);
            filterStatementProtocolMessage.setValue("BRESCIA");
            filterStatementProtocolMessageList.add(filterStatementProtocolMessage);

            groupRequestProtocolMessage.setFilterStatements(filterStatementProtocolMessageList);

            groupRequestEventListener.onGroupRequestAccepted(groupRequestProtocolMessage);

            GroupRequest groupRequest = groupRequestRepository.findById(1L)
                    .orElseThrow(() -> new GroupRequestNotFoundException(1L));
            assertEquals(new Long(1), groupRequest.getId());
            assertEquals(new Timestamp(0), groupRequest.getCreationTimestamp());
            assertEquals(new Long(1), groupRequest.getThirdPartyId());
            assertEquals(AggregatorOperator.AVG, groupRequest.getAggregatorOperator());
            assertEquals(RequestType.BLOOD_OXYGEN_LEVEL, groupRequest.getRequestType());

            List<FilterStatement> filterStatementList = filterStatementRepository.findAllByGroupRequest_Id(1L);
            assertEquals(1, filterStatementList.size());
            assertEquals(FieldType.BIRTH_CITY, filterStatementList.get(0).getColumn());
            assertEquals(ComparisonSymbol.EQUALS, filterStatementList.get(0).getComparisonSymbol());
            assertEquals("BRESCIA", filterStatementList.get(0).getValue());
        }

        /**
         * Test on group request accepted when the status is not accepted
         *
         * @throws Exception no exception expected
         */
        @Test
        public void onGroupRequestAcceptedWithGroupRequestNotAccepted() throws Exception {
            groupRequestProtocolMessage = new GroupRequestProtocolMessage();
            groupRequestProtocolMessage.setId(1L);
            groupRequestProtocolMessage.setCreationTimestamp(new Timestamp(0));
            groupRequestProtocolMessage.setStatus(GroupRequestStatusProtocolMessage.UNDER_ANALYSIS);
            groupRequestProtocolMessage.setThirdPartyId(1L);
            groupRequestProtocolMessage.setAggregatorOperator(AggregatorOperatorProtocolMessage.AVG);
            groupRequestProtocolMessage.setRequestType(RequestTypeProtocolMessage.BLOOD_OXYGEN_LEVEL);

            List<FilterStatementProtocolMessage> filterStatementProtocolMessageList = new ArrayList<>();
            groupRequestProtocolMessage.setFilterStatements(filterStatementProtocolMessageList);

            groupRequestEventListener.onGroupRequestAccepted(groupRequestProtocolMessage);
            assertFalse(groupRequestRepository.existsById(1L));
        }
    }
}