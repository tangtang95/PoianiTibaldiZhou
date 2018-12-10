package com.poianitibaldizhou.trackme.accountservice.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for custom methods of user repository
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@Sql({"classpath:IntegrationTestData"})
public class UserRepositoryIntegrationTest {


    @Autowired
    private UserRepository userRepository;

    /**
     * Test the get of a user by username when a user with that username is present in the system
     *
     * @throws Exception test failed: no user with the specified username exists
     */
    @Test
    public void testGetByUsernameWhenPresent() throws Exception {
        assertEquals("username1", userRepository.findByUserName("username1").orElseThrow(Exception::new).getUserName());
    }

    /**
     *
     */
    @Test
    public void testGetByUsernameWhenNotPresent() {
        assertTrue(!userRepository.findByUserName("notPresentUsername").isPresent());
    }
}
