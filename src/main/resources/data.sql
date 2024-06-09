INSERT INTO user_details(id,birth_date,name)
VALUES(10001,current_date(),'Mateusz');

INSERT INTO user_details(id,birth_date,name)
VALUES(10002,current_date(),'Mati');

INSERT INTO user_details(id,birth_date,name)
VALUES(10003,current_date(),'Mefju');

INSERT INTO post(id,description,user_id)
VALUES(20001,'Great Stuff Man',10001);

INSERT INTO post(id,description,user_id)
VALUES(20002,'So much fun!',10002);

INSERT INTO post(id,description,user_id)
VALUES(20003,'I hate java',10003);

INSERT INTO post(id,description,user_id)
VALUES(20004,'JK I love java',10003);
