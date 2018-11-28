package com.poianitibaldizhou.trackme.individualrequestservice.repository;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

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
public class IndividualRequestRepositoryIntegrationTest {

    @Autowired
    private IndividualRequestRepository requestRepository;

    @Autowired
    private TestEntityManager entityManager;

    /**
     * Test the custom method of repository that retrieves all the requests associated with a certain third party
     * customer.
     * In the test, three requests are given in an embedded database, two of them regards the same third party.
     */
    @Test
    public void retrieveRequestByThirdPartyID() {
        // given: three request (two from the same third party)
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        IndividualRequest request = new IndividualRequest();
        request.setStatus(IndividualRequestStatus.PENDING);
        request.setEndDate(new Date(90000000));
        request.setStartDate(new Date(0));
        request.setTimestamp(timestamp);
        request.setSsn("1111111111111111");
        request.setThirdPartyID((long) 1);
        entityManager.persist(request);
        entityManager.flush();

        timestamp = new Timestamp(System.currentTimeMillis());
        IndividualRequest request2 = new IndividualRequest();
        request2.setStatus(IndividualRequestStatus.ACCEPTED);
        request2.setEndDate(new Date(40000000));
        request2.setStartDate(new Date(1000000));
        request2.setTimestamp(timestamp);
        request2.setSsn("2222222222222222");
        request2.setThirdPartyID((long) 2);
        entityManager.persist(request2);
        entityManager.flush();

        timestamp = new Timestamp(System.currentTimeMillis());
        IndividualRequest request3 = new IndividualRequest();
        request3.setStatus(IndividualRequestStatus.REFUSED);
        request3.setEndDate(new Date(90000000));
        request3.setStartDate(new Date(1000000));
        request3.setTimestamp(timestamp);
        request3.setSsn("3333333333333333");
        request3.setThirdPartyID((long) 1);
        entityManager.persist(request3);
        entityManager.flush();

        // when
        List<IndividualRequest> requestList = requestRepository.findAllByThirdPartyID((long) 1);

        // then
        assertEquals(requestList.size(), 2);
        assertTrue(requestList.contains(request));
        assertTrue(requestList.contains(request3));
    }

    /**
     * Test the custom method of repository that retrieves all the requests associated with a certain third party
     * customer.
     * In this situation no request associated to that specific third party is present
     */
    @Test
    public void retrieveByNonExistingThirdParty() {
        // given: a single request
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        IndividualRequest request = new IndividualRequest();
        request.setStatus(IndividualRequestStatus.PENDING);
        request.setEndDate(new Date(90000000));
        request.setStartDate(new Date(0));
        request.setTimestamp(timestamp);
        request.setSsn("1111111111111111");
        request.setThirdPartyID((long) 1);
        entityManager.persist(request);
        entityManager.flush();

        // when
        List<IndividualRequest> requestList = requestRepository.findAllByThirdPartyID((long)2);

        // then
        assertTrue(requestList.isEmpty());
    }

}
