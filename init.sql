INSERT INTO "Access_Level" ("Id", "Name") VALUES (1, 'admin');
INSERT INTO "Access_Level" ("Id", "Name") VALUES (2, 'manager');
INSERT INTO "Access_Level" ("Id", "Name") VALUES (3, 'client');

INSERT INTO "Account" ("Id", "Login", "Password", "Enabled", "Confirmed", "Version", "CreationDate", "ModificationDate")
VALUES (1, 'mszewc', 'zaq1234', true, true, 34, now()::timestamp, now()::timestamp),
(2, 'man', 'zaq1234', true, true, 34, now()::timestamp, now()::timestamp),
(3, 'notman', 'zaq1234', false, true, 34, now()::timestamp, now()::timestamp),
(4, 'kliencik', 'zaq1234', true, true, 34, now()::timestamp, now()::timestamp);

INSERT INTO "Role" ("Id", "AccessLevelId", "AccountId", "Enabled")
VALUES (1, 1, 1, true),
       (2, 2, 2, true),
       (3, 3, 3, true),
       (4, 3, 4, true)
