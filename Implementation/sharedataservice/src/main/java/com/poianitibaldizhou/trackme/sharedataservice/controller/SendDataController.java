package com.poianitibaldizhou.trackme.sharedataservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.poianitibaldizhou.trackme.sharedataservice.assembler.HealthDataResourceAssembler;
import com.poianitibaldizhou.trackme.sharedataservice.assembler.PositionDataResourceAssembler;
import com.poianitibaldizhou.trackme.sharedataservice.entity.DataWrapper;
import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import com.poianitibaldizhou.trackme.sharedataservice.service.SendDataService;
import com.poianitibaldizhou.trackme.sharedataservice.util.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/send_data")
public class SendDataController {

    @Autowired
    private SendDataService sendDataService;

    @Autowired
    private HealthDataResourceAssembler healthDataAssembler;

    @Autowired
    private PositionDataResourceAssembler positionDataAssembler;

    @JsonView(Views.Public.class)
    @PostMapping("/new_health_data/{userId}")
    public @ResponseBody Resource<HealthData> sendHealthData(@PathVariable(name = "userId") String userId,
                                        @Valid @RequestBody HealthData healthData){
        return healthDataAssembler.toResource(sendDataService.sendHealthData(userId, healthData));
    }

    @JsonView(Views.Public.class)
    @PostMapping("/new_position_data/{userId}")
    public @ResponseBody Resource<PositionData> sendPosition(@PathVariable(name="userId") String userId,
                                                   @Valid @RequestBody PositionData positionData){
        return positionDataAssembler.toResource(sendDataService.sendPosition(userId, positionData));
    }

    @JsonView(Views.Public.class)
    @PostMapping("/new_cluster_data/{userId}")
    public @ResponseBody DataWrapper sendClusterOfData(@PathVariable(name = "userId") String userId,
                                  @RequestBody DataWrapper data) {
        return sendDataService.sendClusterOfData(userId, data);
    }

}
