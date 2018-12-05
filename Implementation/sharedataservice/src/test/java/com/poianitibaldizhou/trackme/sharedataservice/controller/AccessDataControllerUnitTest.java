package com.poianitibaldizhou.trackme.sharedataservice.controller;

import com.poianitibaldizhou.trackme.sharedataservice.assembler.HealthDataResourceAssembler;
import com.poianitibaldizhou.trackme.sharedataservice.assembler.PositionDataResourceAssembler;
import com.poianitibaldizhou.trackme.sharedataservice.service.AccessDataService;
import com.poianitibaldizhou.trackme.sharedataservice.service.SendDataService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AccessDataController.class)
@Import({HealthDataResourceAssembler.class, PositionDataResourceAssembler.class})
public class AccessDataControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccessDataService service;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getIndividualRequestData() throws Exception {
    }

    @Test
    public void getGroupRequestData() throws Exception {
    }

}