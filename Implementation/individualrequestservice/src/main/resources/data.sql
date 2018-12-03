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

INSERT INTO blocked_third_party(third_partyid, block_date, ssn) VALUES(4, '2000-01-01', 'user5')^;


