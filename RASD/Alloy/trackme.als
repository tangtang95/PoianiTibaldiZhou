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

/* Actors */
abstract sig Actor{}

sig User extends Actor{
  userData: set Data
}

sig ThirdParty extends Actor{
  accessible: set Data,
  requests: set Request
}

sig Organizer extends ThirdParty{

}

// assumption: there is one unique institution called when 118 is called
one sig EmergencyInstitution extends ThirdParty{
  
}

/* Objects */
abstract sig Data{
}

sig LocationData extends Data{

}

sig HealthData extends Data{
  belowThreshold: one Bool
}

abstract sig Request{
  status: one RequestStatus
}

sig GroupRequest extends Request{
  requestedData: some Data
}

sig IndividualRequest extends Request{
  user: one User
}

sig SOSCall{
  destinatedTo: one EmergencyInstitution
}

/* Domain Assumptions */
fact DataBelongOnlyToOneUser{
  all disjoint u1, u2: User | all  d: Data | d in u1.userData implies d not in u2.userData
}

/* Help Function */
fun getPeopleInvolved [d: set Data] : set User{
  userData.d
}

/* Requirements */
pred requestAcceptedImpliesDataAccessible {
  all r : GroupRequest | r.status = RequestAccepted implies { 
    all tp : ThirdParty | r in tp.requests implies r.requestedData in tp.accessible 
  } //TODO for IndividualRequest
}

pred requestNotAcceptedImpliesDataInaccessible{
  all r : GroupRequest | r.status != RequestAccepted implies { 
    all tp : ThirdParty | r in tp.requests implies r.requestedData not in tp.accessible 
  } // TODO for IndividualRequest
}

pred groupRequestRequirement [gr: set GroupRequest] {
  // number of people involved in the request must be greater than 1000 to be accepted by 
  //  the system (here we decrease 1000 for simplicity)
  
  #(getPeopleInvolved[gr.requestedData]) > 5 implies gr.status = RequestAccepted
  else gr.status = RequestRefused
}

/* Goals */
assert correctAccessToGroupData {
  requestNotAcceptedImpliesDataInaccessible and requestAcceptedImpliesDataAccessible and 
  groupRequestRequirement[GroupRequest] implies {
    all tp : ThirdParty, r : tp.requests & GroupRequest | r.status = RequestAccepted 
    iff r.requestedData in tp.accessible
  }
}

check correctAccessToGroupData for 10
run groupRequestRequirement for 5
