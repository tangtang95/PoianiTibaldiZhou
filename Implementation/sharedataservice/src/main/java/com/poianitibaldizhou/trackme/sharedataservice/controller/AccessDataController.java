package com.poianitibaldizhou.trackme.sharedataservice.controller;

import com.poianitibaldizhou.trackme.sharedataservice.service.AccessDataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/accessdata")
public class AccessDataController {

    private AccessDataService accessDataService;

    public AccessDataController(AccessDataService accessDataService) {
        this.accessDataService = accessDataService;
    }

    @GetMapping("/individualrequest/{request_id}")
    public @ResponseBody String getIndividualRequestData(@PathVariable(name = "request_id") Long requestId){
        return null;
    }

    @GetMapping("/grouprequest/{request_id}")
    public @ResponseBody String getGroupRequestData(@PathVariable(name = "request_id") Long requestId){
        return accessDataService.getGroupRequestData(requestId);
    }
}
