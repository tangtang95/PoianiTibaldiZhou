package com.poianitibaldizhou.trackme.sharedataservice.controller;

import com.poianitibaldizhou.trackme.sharedataservice.repository.HealthDataRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.PositionDataRepository;
import com.poianitibaldizhou.trackme.sharedataservice.service.AccessDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/access_data")
public class AccessDataController implements AccessDataService {

    @Autowired
    private HealthDataRepository healthDataRepository;

    @Autowired
    private PositionDataRepository positionDataRepository;

    @GetMapping("/individual_request/{request_id}")
    @Override
    public @ResponseBody String getIndividualRequestData(@PathVariable(name = "request_id") Long requestId){
        return null;
    }

    @GetMapping("/group_request/{request_id}")
    @Override
    public @ResponseBody String getGroupRequestData(@PathVariable(name = "request_id") Long requestId){
        return  null;
    }
}
