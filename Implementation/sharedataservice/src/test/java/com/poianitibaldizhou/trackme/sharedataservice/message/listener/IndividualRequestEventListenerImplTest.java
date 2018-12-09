package com.poianitibaldizhou.trackme.sharedataservice.message.listener;

import com.poianitibaldizhou.trackme.sharedataservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import com.poianitibaldizhou.trackme.sharedataservice.exception.IndividualRequestNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.IndividualRequestProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.IndividualRequestStatusProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(Enclosed.class)
public class IndividualRequestEventListenerImplTest {

    /**
     * Integration test of individual request event listener without the message broker
     */
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    @DataJpaTest
    @ActiveProfiles("test")
    @RunWith(SpringRunner.class)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Sql("classpath:sql/testEventListener.sql")
    public static class IntegrationTestWithoutMessageBroker {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private IndividualRequestRepository individualRequestRepository;

        private IndividualRequestEventListener individualRequestEventListener;

        @Before
        public void setUp() throws Exception {
            individualRequestEventListener = new IndividualRequestEventListenerImpl(userRepository,
                    individualRequestRepository);
        }

        @After
        public void tearDown() throws Exception {
            individualRequestEventListener = null;
        }

        /**
         *
         * @throws Exception
         */
        @Test
        public void onIndividualRequestAcceptedSuccessful() throws Exception {
            IndividualRequestProtocolMessage individualRequestProtocolMessage = new IndividualRequestProtocolMessage();
            individualRequestProtocolMessage.setId(1L);
            individualRequestProtocolMessage.setCreationTimestamp(new Timestamp(0));
            individualRequestProtocolMessage.setStatus(IndividualRequestStatusProtocolMessage.ACCEPTED);
            individualRequestProtocolMessage.setThirdPartyId(1L);
            individualRequestProtocolMessage.setStartDate(new Date(0));
            individualRequestProtocolMessage.setEndDate(new Date(1));
            individualRequestProtocolMessage.setUserSsn("user1");

            User user = userRepository.findById("user1").orElseThrow(() -> new UserNotFoundException("user1"));

            individualRequestEventListener.onIndividualRequestAccepted(individualRequestProtocolMessage);

            IndividualRequest individualRequest = individualRequestRepository.findByIdAndThirdPartyId(1L, 1L)
                    .orElseThrow(() -> new IndividualRequestNotFoundException(1L));

            assertEquals(new Timestamp(0), individualRequest.getCreationTimestamp());
            assertEquals(new Date(0), individualRequest.getStartDate());
            assertEquals(new Date(1), individualRequest.getEndDate());
            assertEquals(user, individualRequest.getUser());

        }

        @Test(expected = IndividualRequestNotFoundException.class)
        public void onIndividualRequestAcceptedWithIndividualRequestNotAccepted() throws Exception {
            IndividualRequestProtocolMessage individualRequestProtocolMessage = new IndividualRequestProtocolMessage();
            individualRequestProtocolMessage.setId(1L);
            individualRequestProtocolMessage.setCreationTimestamp(new Timestamp(0));
            individualRequestProtocolMessage.setStatus(IndividualRequestStatusProtocolMessage.REFUSED);
            individualRequestProtocolMessage.setThirdPartyId(1L);
            individualRequestProtocolMessage.setStartDate(new Date(0));
            individualRequestProtocolMessage.setEndDate(new Date(1));
            individualRequestProtocolMessage.setUserSsn("user2");

            individualRequestEventListener.onIndividualRequestAccepted(individualRequestProtocolMessage);

            IndividualRequest individualRequest = individualRequestRepository.findByIdAndThirdPartyId(1L, 1L)
                    .orElseThrow(() -> new IndividualRequestNotFoundException(1L));
        }
    }
}