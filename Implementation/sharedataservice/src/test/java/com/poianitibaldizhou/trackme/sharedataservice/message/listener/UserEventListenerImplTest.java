package com.poianitibaldizhou.trackme.sharedataservice.message.listener;

import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import com.poianitibaldizhou.trackme.sharedataservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.UserProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(value = Enclosed.class)
public class UserEventListenerImplTest {

    /**
     * Integration test of user event listener without the message broker
     */
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    @RunWith(SpringRunner.class)
    @DataJpaTest
    @ActiveProfiles("test")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Sql("classpath:sql/testEventListener.sql")
    public static class IntegrationTestWithoutMessageBroker{

        @Autowired
        private UserRepository userRepository;

        private UserEventListener userEventListener;

        @Before
        public void setUp() throws Exception {
            userEventListener = new UserEventListenerImpl(userRepository);
        }

        @After
        public void tearDown() throws Exception {
            userEventListener = null;
        }

        /**
         *
         * @throws Exception
         */
        @Test
        public void onUserCreatedWithNewSsn() throws Exception {
            UserProtocolMessage userProtocolMessage = new UserProtocolMessage();
            userProtocolMessage.setSsn("user3");
            userProtocolMessage.setFirstName("tangtang");
            userProtocolMessage.setLastName("zhao");
            userProtocolMessage.setBirthDate(new Date(0));
            userProtocolMessage.setBirthCity("brescia");
            userProtocolMessage.setBirthNation("italy");

            userEventListener.onUserCreated(userProtocolMessage);

            User user = userRepository.findById("user3").orElseThrow(() -> new UserNotFoundException("user3"));
            assertEquals("tangtang", user.getFirstName());
            assertEquals("zhao", user.getLastName());
            assertEquals("brescia", user.getBirthCity());
            assertEquals("italy", user.getBirthNation());
            assertEquals(new Date(0), user.getBirthDate());
        }

        @Test
        public void onUserCreatedWithExistingSsn() throws Exception {
            UserProtocolMessage userProtocolMessage = new UserProtocolMessage();
            userProtocolMessage.setSsn("user1");
            userProtocolMessage.setFirstName("tang");
            userProtocolMessage.setLastName("zhao");
            userProtocolMessage.setBirthDate(new Date(0));
            userProtocolMessage.setBirthCity("berlin");
            userProtocolMessage.setBirthNation("germany");

            userEventListener.onUserCreated(userProtocolMessage);

            User user = userRepository.findById("user1").orElseThrow(() -> new UserNotFoundException("user1"));
            assertNotEquals("tangtang", user.getFirstName());
            assertNotEquals("berlin", user.getBirthCity());
            assertNotEquals("germany", user.getBirthNation());
            assertNotEquals(new Date(0), user.getBirthDate());
        }
    }

    /**
     * Integration test of user event listener with the message broker
     */
    @Slf4j
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    @RunWith(SpringRunner.class)
    @SpringBootTest
    @ActiveProfiles(value = {"usage-message-broker", "test"})
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Sql("classpath:sql/testEventListener.sql")
    public static class IntegrationTestWithMessageBroker{

        @Autowired
        private RabbitTemplate rabbitTemplate;

        @Autowired
        private TopicExchange userExchange;

        @Autowired
        private Queue userCreatedToShareDataServiceQueue;

        @Autowired
        @SpyBean
        private UserEventListener userEventListener;

        private RabbitAdmin rabbitAdmin;

        @Before
        public void setUp() throws Exception {
            rabbitAdmin = new RabbitAdmin(rabbitTemplate);
            rabbitAdmin.purgeQueue(userCreatedToShareDataServiceQueue.getName());
        }

        @After
        public void tearDown() throws Exception {
            rabbitAdmin = null;
        }


        @Test
        public void onUserCreatedWithNewSsn() throws Exception {
            UserProtocolMessage userProtocolMessage = new UserProtocolMessage();
            userProtocolMessage.setSsn("user3");
            userProtocolMessage.setFirstName("tangtang");
            userProtocolMessage.setLastName("zhao");
            userProtocolMessage.setBirthDate(new Date(0));
            userProtocolMessage.setBirthCity("brescia");
            userProtocolMessage.setBirthNation("italy");
            rabbitTemplate.convertAndSend(userExchange.getName(), "user.event.created", userProtocolMessage);

            verify(userEventListener, timeout(50000).times(1)).onUserCreated(userProtocolMessage);

        }

        @Test
        public void onUserCreatedWithAlreadyExistingSsn() throws Exception {
            UserProtocolMessage userProtocolMessage = new UserProtocolMessage();
            userProtocolMessage.setSsn("user1");
            userProtocolMessage.setFirstName("mattia");
            userProtocolMessage.setLastName("zhao");
            userProtocolMessage.setBirthDate(new Date(0));
            userProtocolMessage.setBirthCity("milan");
            userProtocolMessage.setBirthNation("italy");
            rabbitTemplate.convertAndSend(userExchange.getName(), "user.event.created", userProtocolMessage);

            verify(userEventListener, timeout(50000).times(1)).onUserCreated(userProtocolMessage);
        }


    }


}