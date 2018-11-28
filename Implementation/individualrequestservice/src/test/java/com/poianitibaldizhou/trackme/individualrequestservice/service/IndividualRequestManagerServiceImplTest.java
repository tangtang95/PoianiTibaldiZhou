package com.poianitibaldizhou.trackme.individualrequestservice.service;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.BlockedThirdPartyRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class IndividualRequestManagerServiceImplTest {

    @TestConfiguration
    static class IndividualRequestManagerServiceImplTestContextConfiguration {

        @MockBean
        private IndividualRequestRepository individualRequestRepository;

        @MockBean
        private UserRepository userRepository;

        @MockBean
        private BlockedThirdPartyRepository blockedThirdPartyRepository;

        @Bean
        public IndividualRequestManagerService individualRequestManagerService() {
            User user = new User();
            user.setSsn("ssntest");
            Mockito.when(userRepository.findById("ssntest")).thenReturn(java.util.Optional.ofNullable(user));
            return new IndividualRequestManagerServiceImpl(individualRequestRepository, blockedThirdPartyRepository, userRepository);
        }
    }

    @Autowired
    private IndividualRequestManagerService requestManagerService;

    @Test
    public void test(){

    }
}
