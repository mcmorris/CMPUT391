/*
 *  File name:  Project_populate.sql
 *  Function:   to populate the database structure for the Online Image Sharing System Project.
 *  Author:     Michael Morris
 */

/* users: user_name (24), password (24), date_registered */
INSERT INTO users values('admin', 'annual', SYSDATE);
INSERT INTO users values('mmorris', 'butts', SYSDATE);
INSERT INTO users values('jwang', 'couple', SYSDATE);
INSERT INTO users values('tmossueda', 'design', SYSDATE);
INSERT INTO users values('dmorris', 'clover', SYSDATE);

CREATE TABLE persons (
   user_name  varchar(24),
   first_name varchar(24),
   last_name  varchar(24),
   address    varchar(128),
   email      varchar(128),
   phone      char(10),
   PRIMARY KEY(user_name),
   UNIQUE (email),
   FOREIGN KEY (user_name) REFERENCES users
);

/* !Default schema constraint: Phone (10) not long enough to store area code or international numbers. */
/* persons: user_name (PK, 24), first_name (24), last_name (24), address (128), email (128), phone (10).  PK: User_name, FK: User_name (users), UNIQUE: Email */
INSERT INTO persons values('mmorris', 'Michael', 'Morris', '3488 Fallon Drive, Parkhill, ON', 'mcmorris@ualberta.ca', '294-3318');
INSERT INTO persons values('jwang', 'Julie', 'Wang', '3978 Albert Street, London, ON', 'jwang@ualberta.ca', '457-9069');
INSERT INTO persons values('tmossueda', 'Tonya' 'Mosqueda', '4695 Heritage Drive, Calgary, AB', 'tmossueda@ualberta.ca', '969-3995');
INSERT INTO persons values('dmorris', 'Derek', 'Morris', '3488 Fallon Drive, Parkhill, ON', 'dmorris@ualberta.ca', '578-3868');


CREATE TABLE groups (
   group_id   int,
   user_name  varchar(24),
   group_name varchar(24),
   date_created date,
   PRIMARY KEY (group_id),
   UNIQUE (user_name, group_name),
   FOREIGN KEY(user_name) REFERENCES users
);

/* groups: group_id, user_name (24), group_name (24), date_created, PK: group_id, UNIQUE: (username, groupname), FK: User_name (users) */
INSERT INTO groups values(1, null, 'public', sysdate);
INSERT INTO groups values(2, null, 'private', sysdate);
INSERT INTO groups values(3, 'admin', 'administration', sysdate);
INSERT INTO groups values(4, 'mmorris', 'family', sysdate);
INSERT INTO groups values(5, 'mmorris', 'friends', sysdate);
INSERT INTO groups values(6, 'jwang', 'besties', sysdate);
INSERT INTO groups values(7, 'jwang', 'friends', sysdate);
INSERT INTO groups values(8, 'tmossueda', 'friends', sysdate);

CREATE TABLE group_lists (
   group_id    int,
   friend_id   varchar(24),
   date_added  date,
   notice      varchar(1024),
   PRIMARY KEY(group_id, friend_id),
   FOREIGN KEY(group_id) REFERENCES groups,
   FOREIGN KEY(friend_id) REFERENCES users
);

/* group_lists: group_id, friend_id (24), date_added, notice (1024), PK: (group_id, friend_id), FK: group_id (groups), friend_id (users) */
INSERT INTO group_lists values(3, 'dmorris', SYSDATE, null);
INSERT INTO group_lists values(4, 'dmorris', SYSDATE, null);
INSERT INTO group_lists values(4, 'jwang', SYSDATE, null);
INSERT INTO group_lists values(5, 'mmorris', SYSDATE, null);

/* tmossueda is special, she has no friends in her friends group.  TEST-CASE. */

CREATE TABLE images (
   photo_id    int,
   owner_name  varchar(24),
   permitted   int,
   subject     varchar(128),
   place       varchar(128),
   timing      date,
   description varchar(2048),
   thumbnail   blob,
   photo       blob,
   PRIMARY KEY(photo_id),
   FOREIGN KEY(owner_name) REFERENCES users,
   FOREIGN KEY(permitted) REFERENCES groups
);

/* images: photo_id, owner_name (24), permitted, subject (128), place (128), timing, description (2048), thumbnail (BLOB), photo (BLOB), PK: photo_id, FK: Owner_name (users), permitted) */
/* How to insert-statement w blobs??? */

/* If in group 'administration' can view image.  No exceptions. */
/* May limit viewing group to: public, private, or any one other group.  Again, admin can always view. */
