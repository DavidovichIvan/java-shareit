/*
 --мои тестовые данные
INSERT INTO users (name, email) VALUES ('Tom', 'Thomas@mail.my');
INSERT INTO users (name, email) VALUES ('Ben', 'ben@mail.my');
INSERT INTO users (name, email) VALUES ('Bob', 'bob@mail.my');
INSERT INTO users (name, email) VALUES ('Bill', 'bill@mail.my');

INSERT INTO items (name, owner_id, description, available, request_id) VALUES ('Hammer', 2, 'Good boat', false, 1);
INSERT INTO items (name, owner_id, description, available, request_id) VALUES ('HYMM', 2, 'TTT', false, 1);
INSERT INTO items (name, owner_id, description, available, request_id) VALUES ('Saw', 1, 'notBoat', true, 3);
INSERT INTO items (name, owner_id, description, request_id) VALUES ('Boat', 1, 'Fast Boat', 5);
INSERT INTO items (name, owner_id, description, request_id) VALUES ('VVVV', 1, 'VVVV', 4);
INSERT INTO items (name, owner_id, description, request_id) VALUES ('WWW', 1, 'WWWW', 2);

INSERT INTO booking (item_id, booker_id, start_date, end_date, status) VALUES (1,1, '2023-05-14 01:02:03', '2026-05-14', 'CURRENT');
INSERT INTO booking (item_id, booker_id, start_date, end_date, status) VALUES (2,2, '2024-01-14 01:02:03', '2027-05-14', 'APPROVED');
INSERT INTO booking (item_id, booker_id, start_date, end_date, status) VALUES (2,2, '2015-09-14 01:02:03', '2016-05-14', 'rejected');
INSERT INTO booking (item_id, booker_id, start_date, end_date, status) VALUES (3,2, '2022-09-14 01:02:03', '2027-05-14', 'rejected');

INSERT INTO comments (item_id, user_id, text) VALUES (1,1,'1Good 1boat');
INSERT INTO comments (item_id, user_id, text) VALUES (1,2,'2Good 2boat');
INSERT INTO comments (item_id, user_id, text) VALUES (2,1,'3Good 3boat');
INSERT INTO comments (item_id, user_id, text) VALUES (2,2,'4Good 4boat');

INSERT INTO requests(requester_id, description) VALUES (1,'Хочу электрошашлычницу');
INSERT INTO requests(requester_id, description) VALUES (2,'Холодильник нужен на лето');
INSERT INTO requests(requester_id, description) VALUES (1,'Лопата');
INSERT INTO requests(requester_id, description) VALUES (2,'Молоток');
INSERT INTO requests(requester_id, description) VALUES (1,'Дрель');
*/