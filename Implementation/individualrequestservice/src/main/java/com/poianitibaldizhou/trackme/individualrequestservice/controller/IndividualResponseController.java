package com.poianitibaldizhou.trackme.individualrequestservice.controller;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.BlockedThirdParty;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualResponse;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.BlockedThirdPartyRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualResponseRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/individualresponseservice")
public class IndividualResponseController {

    private final IndividualResponseRepository individualResponseRepository;
    private final BlockedThirdPartyRepository blockedThirdPartyRepository;

    IndividualResponseController (IndividualResponseRepository individualResponseRepository,
                                  BlockedThirdPartyRepository blockedThirdPartyRepository){
        this.individualResponseRepository = individualResponseRepository;
        this.blockedThirdPartyRepository = blockedThirdPartyRepository;
    }

    @PostMapping("/response")
    IndividualResponse newResponse(@RequestBody IndividualResponse newResponse) {
        return individualResponseRepository.save(newResponse);
    }

    @PostMapping("/blockedThirdParty")
    BlockedThirdParty blockThirdParty(@RequestBody BlockedThirdParty newBlock) {
        return blockedThirdPartyRepository.save(newBlock);
    }
}
