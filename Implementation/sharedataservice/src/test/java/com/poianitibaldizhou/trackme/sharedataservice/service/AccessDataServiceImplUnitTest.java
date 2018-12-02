package com.poianitibaldizhou.trackme.sharedataservice.service;

import com.poianitibaldizhou.trackme.sharedataservice.repository.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Unit test for the access data service
 */
@RunWith(SpringRunner.class)
public class AccessDataServiceImplUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HealthDataRepository healthDataRepository;

    @Mock
    private PositionDataRepository positionDataRepository;

    @Mock
    private GroupRequestRepository groupRequestRepository;

    @Mock
    private FilterStatementRepository filterStatementRepository;

    @InjectMocks
    private AccessDataService accessDataService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        accessDataService = null;
    }

    @Test
    public void getIndividualRequestData() throws Exception {

    }

    @Test
    public void getGroupRequestData() throws Exception {

    }

}