package com.poianitibaldizhou.trackme.individualrequestservice.service;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.BlockedThirdPartyKey;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.RequestNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.BlockedThirdPartyRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.UserRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndividualRequestManagerServiceImpl implements IndividualRequestManagerService {

    private final IndividualRequestRepository individualRequestRepository;
    private final BlockedThirdPartyRepository blockedThirdPartyRepository;
    private final UserRepository userRepository;

    public IndividualRequestManagerServiceImpl(IndividualRequestRepository individualRequestRepository,
                                               BlockedThirdPartyRepository blockedThirdPartyRepository,
                                               UserRepository userRepository) {
        this.individualRequestRepository = individualRequestRepository;
        this.blockedThirdPartyRepository = blockedThirdPartyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<IndividualRequest> getThirdPartyRequests(Long thirdPartyID) {
        return individualRequestRepository.findAllByThirdPartyID(thirdPartyID);
    }

    @Override
    public IndividualRequest addRequest(IndividualRequest newRequest) {
        // Check if the request regards some registered user
        if(userRepository.findById(newRequest.getSsn()).isPresent()) {
            throw new UserNotFoundException(newRequest.getSsn());
        }

        // Check if the request is blocked
        BlockedThirdPartyKey key = new BlockedThirdPartyKey(newRequest.getThirdPartyID(), newRequest.getSsn());
        if(blockedThirdPartyRepository.findById(key).isPresent())
            newRequest.setStatus(IndividualRequestStatus.REFUSED);
        else
            newRequest.setStatus(IndividualRequestStatus.PENDING);

        // Save the request
        return individualRequestRepository.save(newRequest);
    }

    @Override
    public IndividualRequest getRequestById(Long id) {
        return individualRequestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));
    }
}
