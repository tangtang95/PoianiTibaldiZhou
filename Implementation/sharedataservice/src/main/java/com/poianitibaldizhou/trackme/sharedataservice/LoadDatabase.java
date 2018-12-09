package com.poianitibaldizhou.trackme.sharedataservice;

import com.poianitibaldizhou.trackme.sharedataservice.entity.*;
import com.poianitibaldizhou.trackme.sharedataservice.repository.*;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatorOperator;
import com.poianitibaldizhou.trackme.sharedataservice.util.ComparisonSymbol;
import com.poianitibaldizhou.trackme.sharedataservice.util.FieldType;
import com.poianitibaldizhou.trackme.sharedataservice.util.RequestType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Profile("!test")
public class LoadDatabase implements CommandLineRunner{

    @Autowired
    UserRepository userRepository;

    @Autowired
    HealthDataRepository healthDataRepository;

    @Autowired
    PositionDataRepository positionDataRepository;

    @Autowired
    GroupRequestRepository groupRequestRepository;

    @Autowired
    FilterStatementRepository filterStatementRepository;

    @Autowired
    IndividualRequestRepository individualRequestRepository;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setSsn("SSN");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setBirthCity("milan");
        user.setBirthNation("italy");
        user.setBirthDate(Date.valueOf(LocalDate.now()));
        log.info("Preloading: " + user);
        userRepository.save(user);
        HealthData healthData = new HealthData();
        healthData.setId(1L);
        healthData.setUser(user);
        healthData.setHeartBeat(60);
        healthData.setTimestamp(Timestamp.from(Instant.now()));
        healthData.setPressureMax(80);
        healthData.setPressureMin(60);
        healthData.setBloodOxygenLevel(30);
        log.info("Preloading: " + healthData);
        healthDataRepository.save(healthData);
        healthData.setId(2L);
        healthData.setUser(user);
        healthData.setHeartBeat(50);
        healthData.setTimestamp(Timestamp.from(Instant.now()));
        healthData.setPressureMax(100);
        healthData.setPressureMin(70);
        healthData.setBloodOxygenLevel(40);
        log.info("Preloading: " + healthData);
        healthDataRepository.save(healthData);
        PositionData positionData = new PositionData();
        positionData.setUser(user);
        positionData.setTimestamp(Timestamp.from(Instant.now()));
        positionData.setLongitude(30D);
        positionData.setLatitude(40D);
        log.info("Preloading: " + positionData);
        positionDataRepository.save(positionData);
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setAggregatorOperator(AggregatorOperator.COUNT);
        groupRequest.setRequestType(RequestType.USER_SSN);
        groupRequest.setThirdPartyId(1L);
        groupRequest.setCreationTimestamp(new Timestamp(0));
        log.info("Preloading: " + groupRequest);
        groupRequestRepository.save(groupRequest);
        List<GroupRequest> gr = groupRequestRepository.findAll();
        FilterStatement filterStatement = new FilterStatement();
        filterStatement.setId(1L);
        filterStatement.setColumn(FieldType.PRESSURE_MAX);
        filterStatement.setComparisonSymbol(ComparisonSymbol.EQUALS);
        filterStatement.setValue("100");
        filterStatement.setGroupRequest(gr.get(0));
        log.info("Preloading: " + filterStatement);
        filterStatementRepository.save(filterStatement);

        IndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setStartDate(Date.valueOf(LocalDate.of(2018, 12, 6)));
        individualRequest.setEndDate(Date.valueOf(LocalDate.of(2018, 12, 6)));
        individualRequest.setThirdPartyId(1L);
        individualRequest.setCreationTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        individualRequest.setUser(user);
        log.info("Preloading: " + individualRequest);
        individualRequestRepository.save(individualRequest);

    }
}
