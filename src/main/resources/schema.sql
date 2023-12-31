drop table IF EXISTS users, items, booking, comments, requests CASCADE;

create TABLE IF NOT EXISTS users (
  user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (user_id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email),
  CHECK name != '',
  CHECK email != ''
);

create TABLE IF NOT EXISTS items (
  item_id int GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  owner_id int REFERENCES users (user_id) ON delete CASCADE,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(512),
  available boolean DEFAULT true,
  request_id int,
  CONSTRAINT pk_item PRIMARY KEY (item_id)
  );

create unique index IF NOT EXISTS record_exists
ON items(item_id, owner_id);

create TABLE IF NOT EXISTS booking (
booking_id int GENERATED BY DEFAULT AS IDENTITY NOT NULL,
item_id int REFERENCES items (item_id) ON delete CASCADE,
booker_id int REFERENCES users (user_id) ON delete CASCADE,
start_date timestamp without time zone not null,
end_date timestamp without time zone not null,
status varchar(50) DEFAULT 'WAITING'
--,
--CHECK (start_date >= CURRENT_TIMESTAMP),
--CHECK (end_date > start_date)

);

create TABLE IF NOT EXISTS comments (
comment_id int GENERATED BY DEFAULT AS IDENTITY NOT NULL,
item_id int REFERENCES items (item_id) ON delete CASCADE,
user_id int REFERENCES users (user_id) ON delete CASCADE,
text varchar(200),
created timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);

create TABLE IF NOT EXISTS requests (
request_id int GENERATED BY DEFAULT AS IDENTITY NOT NULL,
requester_id int REFERENCES users (user_id) ON delete CASCADE,
description varchar(300) NOT NULL,
created timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
CHECK description != '',
CONSTRAINT request_id PRIMARY KEY (request_id)
);

create unique index IF NOT EXISTS request_exists
ON requests(requester_id, description);

--добавить в методе сохранения вещи заполнение этой таблицы в случае если у вещи заполнено поле request_id
--create TABLE IF NOT EXISTS request_response
--(
--    request_id integer REFERENCES requests(request_id) ON delete CASCADE,
--   item_id integer REFERENCES items(item_id) ON delete CASCADE
--);

--create unique index IF NOT EXISTS response_exists
--ON request_response(request_id, item_id);

