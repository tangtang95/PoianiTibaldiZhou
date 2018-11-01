/* 
 title: TrackMe's Alloy
 date: 2018/19
 description: It defines the world and shared phenomena of the system
 author: Riccardo Poiani, Mattia Tibaldi, Tang-Tang Zhou
*/

/* Useful data structure */
abstract sig Bool{}
one sig True extends Bool{}
one sig False extends Bool{}

abstract sig RequestStatus{}
one sig RequestInProgress extends RequestStatus{}
one sig RequestAccepted extends RequestStatus{}
one sig RequestRefused extends RequestStatus{}
one sig RequestExpired extends RequestStatus{}

abstract sig CallStatus{}
one sig CallAccepted extends CallStatus{}

/* Actors */
abstract sig Actor{}

sig User extends Actor{
    userData: set Data,
    ambulancesProvided: set Ambulance
}

sig ThirdParty extends Actor{
    accessibleData: set Data,
    accessibleAggregatedData: set AggregatedData,
    requests: set Request
}

sig EmergencyRoom{
    ambulances: set Ambulance
}

/* Objects */
abstract sig Data{
}

sig LocationData extends Data{
}

sig HealthData extends Data{
    belowThreshold : one Bool
}

sig AggregatedData{
    regardingData: some Data
}

abstract sig Request{
    status: one RequestStatus
}

sig GroupRequest extends Request{
    aggregatedData: one AggregatedData
}

sig IndividualRequest extends Request{
    user: one User,
    requestedData: some Data
} {
    requestedData in user.userData
}

sig SOSCall{
    status: one CallStatus,
    destinatedTo: one EmergencyRoom,
    performedBy: one User
}

sig Ambulance{

}

/* Domain Assumptions */
fact DataBelongOnlyToOneUser {
    all d: Data | some u : User | d in u.userData and (no u1 : User | u != u1 and d in u1.userData)
} 

fact RequestBelongOnlyToOneThirdParty{
    all r : Request | some tp : ThirdParty | r in tp.requests and (no tp1: ThirdParty | tp != tp1 and r in tp1.requests)
}

fact AggregatedDataBelongAlwaysToSomeGroupRequest {
    all ad: AggregatedData | some gr : GroupRequest | gr.aggregatedData = ad
}
 
fact ThereIsAtLeastAnSOSExternalServiceWithAmbulance{
    some er: EmergencyRoom | #er.ambulances > 0 
}

fact EmergencyRoomAcceptsNewSOSCall{
    //In this model, a SOSCall is performed only if it has not already performed in the last hour
    all sc: SOSCall | some er: EmergencyRoom | #er.ambulances > 0  and sc.destinatedTo = er and sc.status = CallAccepted
}

fact IfSOSCallAreAcceptedThanAnAmbulanceIsSent{
    all a: Ambulance |some u: User | a in u.ambulancesProvided iff (some sc: SOSCall | sc.status = CallAccepted and sc.performedBy = u)
}

fact ambulanceBelongToAtLeastAnEmergencyRoom{
    all a: Ambulance | some er: EmergencyRoom | a in er.ambulances
}

fact ForAllSOSCallAreProvidedOnlyOneAmbulance{
    all sc: SOSCall | #sc.performedBy.ambulancesProvided = 1
}

/* Help Function and Predicate */
fun getPeopleInvolved [d: set Data] : set User{
    userData.d
}

/* Requirements */

// Requirements for G3

/*
 * R12
 * When a user's health parameters has been observed below the threshold, an SOSCall is requested within 5 seconds
 */
pred parametersHasBeenBelowThenAnSOSCallIsRequested {
    all hd: HealthData |one sc: SOSCall | some u: User | sc.performedBy = u and  hd in u.userData and hd.belowThreshold = True 
}

/*
 * R13
 * All the automated SOS call are performed with devices of users whose health parameters are observed below a certain threshold
 */ 
pred AllSOSCallArePerformedByUserWithDataBelowTheThreshold{
    all sc: SOSCall | some hd: HealthData | hd in sc.performedBy.userData and hd.belowThreshold = True
}

// Requirements for G12

/*
 * R35
 * If an individual request is accepted, then the third party who has made the request can access the data specified in the request
 */ 
pred individualRequestAcceptedIfDataAccessible {
    all r : IndividualRequest, tp : ThirdParty | r in tp.requests and r.status = RequestAccepted implies {
         r.requestedData in tp.accessibleData
    }
}


/*
 * R36
 * For each piece of individual data accessible by a third part customer, exists an accepted request regarding it, performed by the same third party 
 */
pred individualDataAccessibleIfAnAcceptedRequestExist {
    all tp : ThirdParty,  d : Data | d in tp.accessibleData implies {
        some r : IndividualRequest | r in tp.requests and r.status = RequestAccepted and r.requestedData in d 
    }
}

// Requirements for G13

/*
 * R39
 * A group request is accepted if the aggregated data specified in the request is accessible to the third party who performed the demand
 */ 
pred groupRequestAcceptedIfAggregatedDataAccessible {
    all r : GroupRequest, tp : ThirdParty | r in tp.requests and r.status = RequestAccepted implies {
        r.aggregatedData in tp.accessibleAggregatedData		
    }
}

/*
 * R40 
 * Group requests is accepted if and only if the number of user involved is greater than 1000
 *
 * Note: the number of people involved in the request must be greater than 1000 to be accepted by 
 *          the system (here 1000 has been decreased for simplicity)
 */
pred groupRequestNumberOfPeopleInvolved {
    all gr : GroupRequest | #(getPeopleInvolved[gr.aggregatedData.regardingData]) > 5 implies {gr.status = RequestAccepted} else {gr.status = RequestRefused}
}


/*
 * R41
 * Aggregated data is accessible to a third party if an accepted aggregated data that request that data exists
 */
pred groupDataAccessibleIfAcceptedRequestExist {
    all tp : ThirdParty, d : AggregatedData | d in tp.accessibleAggregatedData implies {
        some r : GroupRequest | r in tp.requests and r.status = RequestAccepted and r.aggregatedData in d
    }
}


/* 
 * Goals 
 */

/*
 * G3
 * Once the health parameters of a user have been observed 
 * below the threshold for the first time after one hour, an ambulance is sent to the user location.  
 */ 
assert ambulanceIsProvidedAfterASOSCall {
    parametersHasBeenBelowThenAnSOSCallIsRequested  and AllSOSCallArePerformedByUserWithDataBelowTheThreshold implies{
        all hd: HealthData | some u: User | hd in u.userData and hd.belowThreshold = True implies{
            some a: Ambulance | a in u.ambulancesProvided
        }
    }
}

/* 
 * G12: 
 * Allow a third party to access data specified in a request if the user accepts the request or if he accepted one or more requests 
 * from the same third party that provided access to the same data 
 */
assert correctAccessToIndividualData {
    individualDataAccessibleIfAnAcceptedRequestExist  and individualRequestAcceptedIfDataAccessible implies {	
        all r : IndividualRequest, tp : ThirdParty | r in tp.requests implies {
            r.status = RequestAccepted implies {r.requestedData in tp.accessibleData }
            and
            (r.status != RequestAccepted and  no r2 : IndividualRequest | r2 in tp.requests and r2.status = RequestAccepted and  some (r2.requestedData & r.requestedData))  implies {
                (r.requestedData not in tp.accessibleData)
            } 
        }
    }
}

/*
 * G13: 
 * Allow a third party to access statistical and anonymized data if and only if the number of individual involved is greater than 1000. 
 * This is satisfied as soon as the request is approved  
 */
assert correctAccessToGroupData {
    groupRequestAcceptedIfAggregatedDataAccessible and groupRequestNumberOfPeopleInvolved and groupDataAccessibleIfAcceptedRequestExist implies {
        all r : GroupRequest, tp : ThirdParty | r in tp.requests implies {
            #(getPeopleInvolved[r.aggregatedData.regardingData]) >5 iff r.aggregatedData in tp.accessibleAggregatedData 			
        }	
    } 
}

/* Show World Predicate */
pred showData4HelpWorld{
    individualRequestAcceptedIfDataAccessible and individualDataAccessibleIfAnAcceptedRequestExist and groupRequestNumberOfPeopleInvolved
    and groupRequestAcceptedIfAggregatedDataAccessible and groupDataAccessibleIfAcceptedRequestExist 
}

pred showAutomatedSOSWorld{
    parametersHasBeenBelowThenAnSOSCallIsRequested and AllSOSCallArePerformedByUserWithDataBelowTheThreshold
}

check correctAccessToGroupData for 10
check correctAccessToIndividualData for 10
check ambulanceIsProvidedAfterASOSCall  for 10
run showAutomatedSOSWorld for 5 but 0 Request
run showData4HelpWorld for 5 but 1 EmergencyRoom, 1 Ambulance, 0 SOSCall

