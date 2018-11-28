package com.poianitibaldizhou.trackme.sharedataservice.assembler;

import com.poianitibaldizhou.trackme.sharedataservice.controller.SendDataController;
import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class HealthDataResourceAssembler implements ResourceAssembler<HealthData, Resource<HealthData>>{

    @Override
    public Resource<HealthData> toResource(HealthData healthData) {
        return new Resource<>(healthData,
                linkTo(methodOn(SendDataController.class).sendHealthData(healthData.getUser().getSsn(),healthData)).withSelfRel());
    }
}
