package com.poianitibaldizhou.trackme.sharedataservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.poianitibaldizhou.trackme.sharedataservice.assembler.ResourceDataWrapperResourceAssembler;
import com.poianitibaldizhou.trackme.sharedataservice.assembler.HealthDataResourceAssembler;
import com.poianitibaldizhou.trackme.sharedataservice.assembler.PositionDataResourceAssembler;
import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import com.poianitibaldizhou.trackme.sharedataservice.exception.ImpossibleAccessException;
import com.poianitibaldizhou.trackme.sharedataservice.service.SendDataService;
import com.poianitibaldizhou.trackme.sharedataservice.util.Constants;
import com.poianitibaldizhou.trackme.sharedataservice.util.DataWrapper;
import com.poianitibaldizhou.trackme.sharedataservice.util.ResourceDataWrapper;
import com.poianitibaldizhou.trackme.sharedataservice.util.Views;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Entry point for accessing services regarding the sending of data
 */
@RestController
@RequestMapping(Constants.SEND_DATA_API)
public class SendDataController {

    private SendDataService sendDataService;
    private HealthDataResourceAssembler healthDataAssembler;
    private PositionDataResourceAssembler positionDataAssembler;
    private ResourceDataWrapperResourceAssembler dataWrapperResourceAssembler;

    /**
     * Constructor.
     * Creates a new object of SendDataController given a list of beans autowired
     *
     * @param sendDataService the service of sending data to the server
     * @param healthDataAssembler the resource assembler of health data
     * @param positionDataAssembler the resource assembler of position data
     */
    public SendDataController(SendDataService sendDataService, HealthDataResourceAssembler healthDataAssembler,
                              PositionDataResourceAssembler positionDataAssembler,
                              ResourceDataWrapperResourceAssembler dataWrapperResourceAssembler){
        this.sendDataService = sendDataService;
        this.healthDataAssembler = healthDataAssembler;
        this.positionDataAssembler = positionDataAssembler;
        this.dataWrapperResourceAssembler = dataWrapperResourceAssembler;
    }

    /**
     * Adds a new health data of the user {userId} contains the database
     *
     * @param requestingUser user that is trying to access the controller method
     * @param userId the social security number of the user
     * @param healthData the new health data
     * @return a CREATED http response with the health data added inside a resource
     */
    @JsonView(Views.Public.class)
    @PostMapping(Constants.SEND_HEALTH_DATA_API)
    public @ResponseBody ResponseEntity<Resource<HealthData>> sendHealthData(@RequestHeader(value= Constants.HEADER_USER_SSN) String requestingUser,
                                                                             @PathVariable String userId,
                                        @RequestBody HealthData healthData){
        if(!requestingUser.equals(userId))
            throw new ImpossibleAccessException();

        return new ResponseEntity<>(healthDataAssembler
                .toResource(sendDataService.sendHealthData(userId, healthData)), HttpStatus.CREATED);
    }

    /**
     * Adds a new position data of the user {userId} contains the database
     *
     * @param requestingUser user that is trying to access this method
     * @param userId the social security number of the user
     * @param positionData the new position data
     * @return a CREATED http response with the position data added inside a resource
     */
    @JsonView(Views.Public.class)
    @PostMapping(Constants.SEND_POSITION_DATA_API)
    public @ResponseBody ResponseEntity<Resource<PositionData>> sendPositionData(@RequestHeader(value = Constants.HEADER_USER_SSN) String requestingUser,
                                                                                 @PathVariable String userId,
                                                                                 @RequestBody PositionData positionData){
        if(!requestingUser.equals(userId))
            throw new ImpossibleAccessException();
        return new ResponseEntity<>(positionDataAssembler
                .toResource(sendDataService.sendPositionData(userId, positionData)), HttpStatus.CREATED);
    }

    /**
     * Adds a list of new position data and a list of health position data of the user {userId} contains the database
     *
     * @param requestingUser user that is trying to access this method
     * @param userId the social security number of the user
     * @param data the data wrapper containing a list of health data and a list of position data
     * @return a CREATED http response with the list of data added
     */
    @JsonView(Views.Public.class)
    @PostMapping(Constants.SEND_DATA_CLUSTER_API)
    public @ResponseBody ResponseEntity<Resource<ResourceDataWrapper>> sendClusterOfData(@RequestHeader(value = Constants.HEADER_USER_SSN) String requestingUser,
                                                                                         @PathVariable String userId,
                                                                                         @RequestBody DataWrapper data) {
        if(!requestingUser.equals(userId))
            throw new ImpossibleAccessException();

        DataWrapper result = sendDataService.sendClusterOfData(userId, data);
        ResourceDataWrapper resourceDataWrapper = new ResourceDataWrapper();
        resourceDataWrapper.setHealthDataList(result.getHealthDataList().stream()
                .map(healthDataAssembler::toResource).collect(Collectors.toList()));
        resourceDataWrapper.setPositionDataList(result.getPositionDataList().stream()
                .map(positionDataAssembler::toResource).collect(Collectors.toList()));
        resourceDataWrapper.setUserSsn(userId);
        return new ResponseEntity<>(dataWrapperResourceAssembler.toResource(resourceDataWrapper),
                HttpStatus.CREATED);
    }

}
