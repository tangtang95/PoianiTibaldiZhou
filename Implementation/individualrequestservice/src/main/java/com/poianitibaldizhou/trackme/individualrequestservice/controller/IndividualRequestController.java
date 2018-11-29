package com.poianitibaldizhou.trackme.individualrequestservice.controller;

import com.poianitibaldizhou.trackme.individualrequestservice.assembler.IndividualRequestResourceAssembler;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.service.IndividualRequestManagerService;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/individualrequestservice")
public class IndividualRequestController {

    private final IndividualRequestManagerService requestManagerService;

    private final IndividualRequestResourceAssembler assembler;

    IndividualRequestController(IndividualRequestManagerService individualRequestManagerService, IndividualRequestResourceAssembler assembler) {
        this.requestManagerService = individualRequestManagerService;
        this.assembler = assembler;
    }

    @GetMapping("/requests/{id}")
    public @ResponseBody Resource<IndividualRequest> getRequestById(@PathVariable Long id) {
        return assembler.toResource(requestManagerService.getRequestById(id));
    }


    @GetMapping("/requests/thirdparty/{thirdPartyID}")
    public @ResponseBody Resources<Resource<IndividualRequest>> getThirdPartyRequests(@PathVariable Long thirdPartyID) {
        List<Resource<IndividualRequest>> requests = requestManagerService.getThirdPartyRequests(thirdPartyID).stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(requests,
                linkTo(methodOn(IndividualRequestController.class).getThirdPartyRequests(thirdPartyID)).withSelfRel());
    }

    @PostMapping("/requests")
    public @ResponseBody ResponseEntity<?> newRequest(@RequestBody IndividualRequest newRequest) throws URISyntaxException {
        Resource<IndividualRequest> resource = assembler.toResource(requestManagerService.addRequest(newRequest));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref())).body(resource);
    }
}
