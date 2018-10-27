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
one sig CallRequested extends CallStatus{}
one sig CallAccepted extends CallStatus{}
one sig CallRefused extends CallStatus{}
one sig CallCancelled extends CallStatus{}

/* Actors */
abstract sig Actor{}

sig User extends Actor{
  userData: set Data
}

sig ThirdParty extends Actor{
  accessibleData: set Data,
  accessibleAggregatedData: set AggregatedData,
  requests: set Request
}

sig EmergencyRoom{
  
}

/* Objects */
abstract sig Data{
}

sig LocationData extends Data{

}

sig HealthData extends Data{
  belowThreshold: one Bool
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
  destinatedTo: one EmergencyRoom
}

/* Domain Assumptions */
fact DataBelongOnlyToOneUser{
  all d: Data | some u : User | d in u.userData and (no u1 : User | u != u1 and d in u1.userData)
}

fact RequestBelongOnlyToOneThirdParty{
  all r : Request | some tp : ThirdParty | r in tp.requests and (no tp1: ThirdParty | tp != tp1 and r in tp1.requests)
}

fact AggregatedDataBelongAlwaysToSomeGroupRequest{
  all ad: AggregatedData | some gr : GroupRequest | gr.aggregatedData = ad
}

fact SOSCallBelongOnlyToOneUser{
  // TODO
}

/* Help Function and Predicate */
fun getPeopleInvolved [d: set Data] : set User{
  userData.d
}

/* Requirements */
pred requestAcceptedIffDataAccessible {
  all r : GroupRequest | r.status = RequestAccepted implies { 
    all tp : ThirdParty | r in tp.requests iff r.aggregatedData in tp.accessibleAggregatedData 
  } else {
    all tp : ThirdParty | r.aggregatedData not in tp.accessibleAggregatedData
  }
   
  //TODO for IndividualRequest
}

pred groupRequestRequirement {
  // number of people involved in the request must be greater than 1000 to be accepted by 
  //  the system (here we decrease 1000 for simplicity)
  
  all gr : GroupRequest | #(getPeopleInvolved[gr.aggregatedData.regardingData]) > 5 implies gr.status = RequestAccepted
  else gr.status = RequestRefused
}

/* Show World Predicate */
pred showData4HelpWorld{
  requestAcceptedIffDataAccessible and groupRequestRequirement and some tp : ThirdParty | #(tp.accessibleAggregatedData) >0 
}

/* Goals */
assert correctAccessToGroupData {
  requestAcceptedIffDataAccessible and groupRequestRequirement implies {
     all r : GroupRequest | #(getPeopleInvolved[r.aggregatedData.regardingData]) >5 implies { 
      all tp : ThirdParty | r in tp.requests iff r.aggregatedData in tp.accessibleAggregatedData 
     } else {
      all tp : ThirdParty | r.aggregatedData not in tp.accessibleAggregatedData
     }
  } 
}

check correctAccessToGroupData for 10
run showData4HelpWorld for 6
