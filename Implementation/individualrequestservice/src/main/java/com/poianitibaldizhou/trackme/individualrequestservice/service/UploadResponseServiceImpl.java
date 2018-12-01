package com.poianitibaldizhou.trackme.individualrequestservice.service;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.*;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.*;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.BlockedThirdPartyRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.IndividualResponseRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.repository.UserRepository;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestResponse;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of the upload response service.
 */
public class UploadResponseServiceImpl implements UploadResponseService {

    private final UserRepository userRepository;
    private final BlockedThirdPartyRepository blockedThirdPartyRepository;
    private final IndividualRequestRepository individualRequestRepository;
    private final IndividualResponseRepository individualResponseRepository;

    /**
     * Creates an individual request manager service.
     * It needs some repository in order to make some operations on data (e.g. saving an uploaded response).
     *
     * @param userRepository repository regarding the user
     * @param blockedThirdPartyRepository repository regarding the third party customers that have been blocked by som
     *                                    users
     * @param individualRequestRepository repository regarding the individual request
     * @param individualResponseRepository repository regarding the responses to individual requests
     */
    public UploadResponseServiceImpl(UserRepository userRepository, BlockedThirdPartyRepository blockedThirdPartyRepository,
                                     IndividualRequestRepository individualRequestRepository, IndividualResponseRepository individualResponseRepository) {
        this.userRepository = userRepository;
        this.blockedThirdPartyRepository = blockedThirdPartyRepository;
        this.individualRequestRepository = individualRequestRepository;
        this.individualResponseRepository = individualResponseRepository;
    }

    @Override
    public IndividualResponse addResponse(Long requestID, IndividualRequestResponse response, User user) {
        // Check that the response is a valid one
        User finalUser = user;
        user = userRepository.findById(user.getSsn()).orElseThrow(() -> new UserNotFoundException(finalUser));
        IndividualRequest request = individualRequestRepository.findById(requestID).orElseThrow(() -> new RequestNotFoundException(requestID));

        if(individualResponseRepository.findById(requestID).isPresent())
            throw new ResponseAlreadyPresentException(requestID);
        if(individualRequestRepository.findById(requestID).map(IndividualRequest::getUser).
                filter(userRequest -> !userRequest.equals(finalUser)).isPresent()) {
            throw new NonMatchingUserException(user.getSsn());
        }

        // Update row in the database
        if (response == IndividualRequestResponse.ACCEPT) {
            request.setStatus(IndividualRequestStatus.ACCEPTED_UNDER_ANALYSIS);
        } else if (response == IndividualRequestResponse.REFUSE) {
            request.setStatus(IndividualRequestStatus.REFUSED);

        }

        // TODO fix
        // Save the individual response in the database
        /*IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setRequestID(request);
        individualResponse.setResponse(response);
        individualResponse.setAcceptanceTimeStamp(Timestamp.valueOf(LocalDateTime.now()));
        individualResponse.setSsn(user);

        return individualResponseRepository.save(individualResponse);*/
        return null;
    }

    @Override
    public BlockedThirdParty addBlock(User user, Long thirdPartyID) {
        // Check that the block is valid (i.e. user registerd, a request exist, and no other block exists
        if(!userRepository.findById(user.getSsn()).isPresent())
            throw new UserNotFoundException(user);
        List<IndividualRequest> requestList = individualRequestRepository.findAllByThirdPartyID(thirdPartyID);

        if(requestList.stream()
                .anyMatch(individualRequest -> individualRequest.getUser().equals(user))) {
            throw new ThirdPartyNotFoundException(thirdPartyID);
        }
        BlockedThirdPartyKey key = new BlockedThirdPartyKey(thirdPartyID, user);
        if(blockedThirdPartyRepository.findById(key).isPresent()) {
            throw new BlockAlreadyPerformedException(thirdPartyID);
        }

        // Change all the pending requests to refused
        requestList.stream().filter(individualRequest -> individualRequest.getStatus().equals(IndividualRequestStatus.PENDING))
                .forEach(individualRequest -> individualRequest.setStatus(IndividualRequestStatus.REFUSED));

        // Save the blocked third party
        BlockedThirdParty blockedThirdParty = new BlockedThirdParty();
        blockedThirdParty.setKey(key);
        blockedThirdParty.setBlockDate(Date.valueOf(LocalDate.now()));

        return blockedThirdPartyRepository.save(blockedThirdParty);
    }
}
