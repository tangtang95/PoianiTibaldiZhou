package com.poianitibaldizhou.trackme.individualrequestservice.service;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.BlockedThirdPartyKey;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.IncompatibleDateException;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.RequestNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.BlockedThirdPartyRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.UserRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Implementation of the individual request manager service.
*/
@Service
public class IndividualRequestManagerServiceImpl implements IndividualRequestManagerService {

    private final IndividualRequestRepository individualRequestRepository;
    private final BlockedThirdPartyRepository blockedThirdPartyRepository;
    private final UserRepository userRepository;

    /**
     * Creates an individual request manager service.
     * It needs some repository in order to make some operations on data (e.g. saving a new request).
     *
     * @param individualRequestRepository repository regarding the individual requests, useful to manage (i.e.
     *                                    saving and retrieving) requests
     * @param blockedThirdPartyRepository repository regarding the blocked third party, useful to check if a user
     *                                    blocked a certain third party
     * @param userRepository repository regarding the users, useful to match that users exist
     */
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
    public List<IndividualRequest> getUserPendingRequests(User user) {
        // Check if the request regards a registered user
        if(!userRepository.findById(user.getSsn()).isPresent())
            throw new UserNotFoundException(user);

        return individualRequestRepository.findAllByUserAndStatus(user, IndividualRequestStatus.PENDING);
    }

    @Override
    public IndividualRequest addRequest(IndividualRequest newRequest) {
        // Check if the request regards a registered user
        if(!userRepository.findById(newRequest.getUser().getSsn()).isPresent()) {
            throw new UserNotFoundException(newRequest.getUser());
        }

        if(newRequest.getStartDate().after(newRequest.getEndDate())) {
            throw new IncompatibleDateException();
        }

        // Check if the request is blocked
        BlockedThirdPartyKey key = new BlockedThirdPartyKey(newRequest.getThirdPartyID(), newRequest.getUser());
        if(blockedThirdPartyRepository.findById(key).isPresent()) {
            newRequest.setStatus(IndividualRequestStatus.REFUSED);
        } else {
            newRequest.setStatus(IndividualRequestStatus.PENDING);
        }

        newRequest.setTimestamp(Timestamp.from(Instant.now()));

        // Save the request
        return individualRequestRepository.save(newRequest);
    }

    @Override
    public IndividualRequest getRequestById(Long id) {
        return individualRequestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));
    }
}
