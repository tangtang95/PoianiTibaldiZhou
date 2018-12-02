INSERT INTO user(ssn) VALUES('user1')^;
INSERT INTO user(ssn) VALUES('user2')^;
INSERT INTO user(ssn) VALUES('user3')^;
INSERT INTO user(ssn) VALUES('user4')^;
INSERT INTO user(ssn) VALUES('user5')^;

INSERT INTO individual_request(id, end_date, start_date, status, third_partyid, timestamp, ssn) VALUES(1, '2000-01-01', '2000-01-01', 'PENDING', 1, '2000-01-01 00:00:00','user1')^;
INSERT INTO individual_request(id, end_date, start_date, status, third_partyid, timestamp, ssn) VALUES(2, '2000-01-01', '2000-01-01', 'PENDING', 1, '2000-01-01 00:00:00','user1')^;
INSERT INTO individual_request(id, end_date, start_date, status, third_partyid, timestamp, ssn) VALUES(3, '2000-01-01', '2000-01-01', 'PENDING', 1, '2000-01-01 00:00:00','user1')^;
INSERT INTO individual_request(id, end_date, start_date, status, third_partyid, timestamp, ssn) VALUES(4, '2000-01-01', '2000-01-01', 'PENDING', 2, '2000-01-01 00:00:00','user1')^;
INSERT INTO individual_request(id, end_date, start_date, status, third_partyid, timestamp, ssn) VALUES(5, '2000-01-01', '2000-01-01', 'PENDING', 2, '2000-01-01 00:00:00','user2')^;

CREATE TRIGGER RemovePendingRequestWhenBlocked
  AFTER INSERT ON blocked_third_party
  FOR EACH ROW
  BEGIN
    UPDATE individual_request
    set individual_request.status = 'REFUSED'
    WHERE individual_request.ssn = new.ssn AND
          individual_request.third_partyid = new.third_partyid AND
          individual_request.status = 'PENDING';
  END^;

CREATE TRIGGER ConsistentRequestStatus
  AFTER INSERT ON response
  FOR EACH ROW
  BEGIN
    IF (new.response = 'ACCEPT') THEN
      UPDATE individual_request SET individual_request.status = 'ACCEPTED_UNDER_ANALYSIS' WHERE new.requestid = individual_request.id;
    END IF;
    IF(new.response = 'REFUSE') THEN
      UPDATE individual_request SET individual_request.status ='REFUSE' WHERE individual_request.id = new.requestId;
    END IF;
  END^;


