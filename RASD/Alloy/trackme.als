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
		all tp : ThirdParty | no r2 : GroupRequest | r2.status = RequestAccepted and r2.aggregatedData = r.aggregatedData implies {
			r.aggregatedData not in tp.accessibleAggregatedData
		}
	}
   
    all r: IndividualRequest | r.status = RequestAccepted implies {
	    all tp : ThirdParty | r in tp.requests iff r.requestedData in tp.accessibleData
    } 
}

pred groupRequestRequirement {
    // number of people involved in the request must be greater than 1000 to be accepted by 
    //  the system (here we decrease 1000 for simplicity)
    all gr : GroupRequest | #(getPeopleInvolved[gr.aggregatedData.regardingData]) > 5 implies {gr.status = RequestAccepted}
	
    all gr : GroupRequest | #(getPeopleInvolved[gr.aggregatedData.regardingData]) < 6 implies {gr.status = RequestRefused}
}

/* Show World Predicate */
pred showData4HelpWorld{
    requestAcceptedIffDataAccessible and groupRequestRequirement 
}

/* Goals */
assert correctAccessToGroupData {
    requestAcceptedIffDataAccessible and groupRequestRequirement implies {
         all r : GroupRequest, tp : ThirdParty | r in tp.requests implies {
			#(getPeopleInvolved[r.aggregatedData.regardingData]) >5 implies {
				r.status = RequestAccepted  and r.aggregatedData in tp.accessibleAggregatedData 			
			} 	else {
         		r.aggregatedData not in tp.accessibleAggregatedData iff 	
				(no r2 : GroupRequest| r2 in tp.requests and r2.status = RequestAccepted and  (r2.aggregatedData = r.aggregatedData) )
         	}
		}	
    } 
}

assert correctAccessToIndividualData {
	requestAcceptedIffDataAccessible implies {	
		all r : IndividualRequest, tp : ThirdParty | r in tp.requests implies {
			r.status = RequestAccepted implies {r.requestedData in tp.accessibleData }
			and
			r.status != RequestAccepted and  (r.requestedData not in tp.accessibleData) implies {
				no r2 : IndividualRequest | r2 in tp.requests and r2.status = RequestAccepted and  no (r2.requestedData & r.requestedData) 
			}
    		}
	}
}

check correctAccessToGroupData for 30
check correctAccessToIndividualData for 30
run showData4HelpWorld for 7
