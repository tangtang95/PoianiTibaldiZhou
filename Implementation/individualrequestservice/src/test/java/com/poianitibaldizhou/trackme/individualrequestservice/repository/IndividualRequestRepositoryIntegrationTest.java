package com.poianitibaldizhou.trackme.individualrequestservice.repository;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration test among the individual request repository and the database.
 * Unit test is not reasonable in this case.
 * Check: https://stackoverflow.com/questions/23435937/how-to-test-spring-data-repositories
 */
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode =DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class IndividualRequestRepositoryIntegrationTest {

    @Autowired
    private IndividualRequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setUpPersistentUnit() {
        entityManager.persist(new User("user1"));
        entityManager.persist(new User("user3"));
        entityManager.flush();

        entityManager.persist(getIndividualRequest1());

        entityManager.persist(getIndividualRequest2());

        entityManager.persist(getIndividualRequest3());

        entityManager.flush();
    }

    @After
    public void tearDown() {
        entityManager.clear();
    }

    private  IndividualRequest getIndividualRequest1() {
        Timestamp timestamp = new Timestamp(0);
        IndividualRequest request = new IndividualRequest();
        request.setStatus(IndividualRequestStatus.PENDING);
        request.setEndDate(new Date(90000000));
        request.setStartDate(new Date(0));
        request.setTimestamp(timestamp);
        request.setUser(new User("user1"));
        request.setThirdPartyID((long) 1);
        return request;
    }

    private IndividualRequest getIndividualRequest2() {
        Timestamp timestamp = new Timestamp(0);
        IndividualRequest request2 = new IndividualRequest();
        request2.setStatus(IndividualRequestStatus.ACCEPTED);
        request2.setEndDate(new Date(40000000));
        request2.setStartDate(new Date(1000000));
        request2.setTimestamp(timestamp);
        request2.setUser(new User("user1"));
        request2.setThirdPartyID((long) 2);
        return request2;
    }

    private IndividualRequest getIndividualRequest3() {
        Timestamp timestamp = new Timestamp(0);
        timestamp = new Timestamp(System.currentTimeMillis());
        IndividualRequest request3 = new IndividualRequest();
        request3.setStatus(IndividualRequestStatus.REFUSED);
        request3.setEndDate(new Date(90000000));
        request3.setStartDate(new Date(1000000));
        request3.setTimestamp(timestamp);
        request3.setUser(new User("user3"));
        request3.setThirdPartyID((long) 1);
        return request3;
    }

    // Test retrieve by third party id

    /**
     * Test the custom method of repository that retrieves all the requests associated with a certain third party
     * customer.
     * In the test, three requests are given in an embedded database, two of them regards the same third party.
     */
    @Test
    public void retrieveRequestByThirdPartyID() {
        // given: three request (see set up)


        // when: the third party is present and is related with two of them
        List<IndividualRequest> requestList = requestRepository.findAllByThirdPartyID((long) 1);

        // then: expect that the size of retrieval is 2 and that the two request are related with the third party specified
        assertEquals(2, requestList.size());
        for(int i = 0; i < requestList.size(); i++) {
            assertEquals((long)1, (long)requestList.get(i).getThirdPartyID());
        }
    }

    /**
     * Test the custom method of repository that retrieves all the requests associated with a certain third party
     * customer.
     * In this situation no request associated to that specific third party is present
     */
    @Test
    public void retrieveByNonExistingThirdParty() {
        // given: three request (see setup)

        // when: retrieving the request of a third party not related with the present request
        List<IndividualRequest> requestList = requestRepository.findAllByThirdPartyID((long)3);

        // then: the size of the response is 0
        assertTrue(requestList.isEmpty());
    }

    // Test retrieving by user and status

    /**
     * Test the custom method of repository that retrieves all the requests associated with a certain user
     * and with a certain status
     * In this situation no request associated to that specific user is present
     */
    @Test
    public void retrieveByNonExistingUser() {
        // given: three requests (see setup)

        // when: retrieving the request of a user not related with the present request
        List<IndividualRequest> requestList = requestRepository.findAllByUserAndStatus(new User("notPresentUser"), IndividualRequestStatus.PENDING);

        // then: the size of the response is 0
        assertTrue(requestList.isEmpty());
    }

    /**
     * Test the custom method of repository that retrieves all the requests associated with a certain user
     * and with a certain status
     * In this situation no pending request associated to that specific user is present
     */
    @Test
    public void retrieveByNonPresentStatusForThatUser() {
        // given: three request (see setup)

        // when: retrieving the request of a user that has no pending request
        List<IndividualRequest> requestList = requestRepository.findAllByUserAndStatus(new User("user1"), IndividualRequestStatus.ELAPSED);

        // then: the size of the response is 0
        assertTrue(requestList.isEmpty());

    }

    /**
     * Test the custom method of repository that retrieves all the requests associated with a certain third party
     * customer.
     * In the test, three requests are given in an embedded database, two of them regards the same third party.
     */
    @Test
    public void retrieveByUserAndStatus() {
        // given: three request (see setup)

        // when: the list of the accepted request by user1 is requested
        List<IndividualRequest> requestList = requestRepository.findAllByUserAndStatus(new User("user1"), IndividualRequestStatus.ACCEPTED);

        // then: expect that the size of retrieval is 1 and that the accepted request is the one present in the list
        IndividualRequest request2 = getIndividualRequest2();
        assertEquals(1, requestList.size());
        assertEquals(request2.getUser(), requestList.get(0).getUser());
        assertEquals(request2.getThirdPartyID(), requestList.get(0).getThirdPartyID());
        assertEquals(request2.getStatus(), requestList.get(0).getStatus());
        assertEquals(request2.getStartDate(), requestList.get(0).getStartDate());
        assertEquals(request2.getEndDate(), requestList.get(0).getEndDate());
        assertEquals(request2.getTimestamp(), requestList.get(0).getTimestamp());
    }

}
