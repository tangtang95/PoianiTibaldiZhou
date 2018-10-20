/* 
 title: TrackMe's Alloy
 description: It defines the world and shared phenomena of the system
 author: Riccardo Poiani, Mattia Tibaldi, Tang-Tang Zhou
*/

// define useful struct data
abstract sig Bool{}
one sig True extends Bool{}
one sig False extends Bool{}

abstract sig RequestStatus{}
sig RequestInProgress extends RequestStatus{}
sig RequestAccepted

// from this point, it defines signature about the project's environment
abstract sig Data{
}

sig LocationData extends Data{

}

sig HealthData extends Data{
  belowThreshold: one Bool
}

sig User{
  data: set Data
}

sig ThirdParty{
  requests: set Request
}

sig Organizer extends ThirdParty{

}

// assumption: there is one unique institution called when 118 is called
one sig EmergencyInstitution extends ThirdParty{
  
}

abstract sig Request{
  
}

sig GroupRequest extends Request{
  data: set Data
}

sig IndividualRequest extends Request{
  user: one User
}

fact DataBelongOnlyToOneUser{
  all disjoint u1, u2: User | all  d: Data | d in u1.data implies d not in u2.data
}
