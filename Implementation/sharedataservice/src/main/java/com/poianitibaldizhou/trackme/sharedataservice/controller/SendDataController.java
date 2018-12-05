package com.poianitibaldizhou.trackme.sharedataservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.poianitibaldizhou.trackme.sharedataservice.assembler.HealthDataResourceAssembler;
import com.poianitibaldizhou.trackme.sharedataservice.assembler.PositionDataResourceAssembler;
import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import com.poianitibaldizhou.trackme.sharedataservice.service.SendDataService;
import com.poianitibaldizhou.trackme.sharedataservice.util.DataWrapper;
import com.poianitibaldizhou.trackme.sharedataservice.util.Views;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Entry point for accessing services regarding the sending of data
 */
@RestController
@RequestMapping("/senddata")
public class SendDataController {

    private SendDataService sendDataService;
    private HealthDataResourceAssembler healthDataAssembler;
    private PositionDataResourceAssembler positionDataAssembler;

    /**
     * Constructor.
     * Creates a new object of SendDataController given a list of beans autowired
     *
     * @param sendDataService the service of sending data to the server
     * @param healthDataAssembler the resource assembler of health data
     * @param positionDataAssembler the resource assembler of position data
     */
    public SendDataController(SendDataService sendDataService, HealthDataResourceAssembler healthDataAssembler,
                              PositionDataResourceAssembler positionDataAssembler){
        this.sendDataService = sendDataService;
        this.healthDataAssembler = healthDataAssembler;
        this.positionDataAssembler = positionDataAssembler;
    }

    /**
     * Adds a new health data of the user {userId} contains the database
     *
     * @param userId the social security number of the user
     * @param healthData the new health data
     * @return a CREATED http response with the health data added inside a resource
     */
    @JsonView(Views.Public.class)
    @PostMapping("/healthdata/{userId}")
    public @ResponseBody ResponseEntity<Resource<HealthData>> sendHealthData(@PathVariable(name = "userId") String userId,
                                        @RequestBody HealthData healthData){
        return new ResponseEntity<>(healthDataAssembler
                .toResource(sendDataService.sendHealthData(userId, healthData)), HttpStatus.CREATED);
    }

    /**
     * Adds a new position data of the user {userId} contains the database
     *
     * @param userId the social security number of the user
     * @param positionData the new position data
     * @return a CREATED http response with the position data added inside a resource
     */
    @JsonView(Views.Public.class)
    @PostMapping("/positiondata/{userId}")
    public @ResponseBody ResponseEntity<Resource<PositionData>> sendPositionData(@PathVariable(name="userId") String userId,
                                                   @RequestBody PositionData positionData){
        return new ResponseEntity<>(positionDataAssembler
                .toResource(sendDataService.sendPositionData(userId, positionData)), HttpStatus.CREATED);
    }

    /**
     * Adds a list of new position data and a list of health position data of the user {userId} contains the database
     *
     * @param userId the social security number of the user
     * @param data the data wrapper containing a list of health data and a list of position data
     * @return a CREATED http response with the list of data added
     */
    @JsonView(Views.Public.class)
    @PostMapping("/clusterdata/{userId}")
    public @ResponseBody ResponseEntity<DataWrapper> sendClusterOfData(@PathVariable(name = "userId") String userId,
                                  @RequestBody DataWrapper data) {
        return new ResponseEntity<>(sendDataService.sendClusterOfData(userId, data), HttpStatus.CREATED);
    }

}
