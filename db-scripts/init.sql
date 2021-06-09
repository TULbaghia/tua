-- Utworzenie kont użytkowników
INSERT INTO account (id, creation_date, modification_date, version, confirmed, contact_number, enabled,
                     failed_login_attempts_counter, firstname, language, last_failed_login_date,
                     last_failed_login_ip_address, theme_color, last_successful_login_date, last_successful_login_ip_address,
                     lastname, login, email, new_email, password, created_by, modified_by)
VALUES (-1, now(), null, 1, true, '999888777', true, 0, 'Admin', 'pl', null, null, 'LIGHT', null, null, 'Adminski', 'admin1', 'admin@ssbd06.pl',
        null, 'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', -1, null),
       (-2, now(), null, 1, true, '256987539', true, 0, 'Mateusz', 'pl', null, null, 'LIGHT', null, null, 'Chrzanowski', 'mchrzan','mchrzan@edu.pl',
        null, 'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', -1, null),
       (-3, now(), null, 1, true, '154729754', true, 0, 'Michał', 'pl', null, null, 'LIGHT', null, null, 'Masłowski', 'mima', 'mima@edu.pl',
        null, 'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', -1, null),
       (-4, now(), null, 1, true, '234978427', true, 0, 'Marcin', 'pl', null, null, 'LIGHT', null, null, 'Borowski', 'marbor', 'marbor@edu.pl',
        null, 'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', -1, null),
       (-5, now(), null, 1, true, '543095437', true, 0, 'Dominik', 'pl', null, null, 'DARK', null, null, 'Dąbrowski', 'domino', 'domino@edu.pl',
        null, 'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', -1, null),
       (-6, now(), null, 1, true, '356897453', true, 0, 'Zbigniew', 'pl', null, null, 'DARK', null, null, 'Adamczyk', 'zbadam', 'zbadam@edu.pl',
        null, 'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', -1, null),
       (-7, now(), null, 1, true, '356892223', true, 0, 'Filp', 'pl', null, null, 'LIGHT', null, null, 'Bąk', 'filipino', 'filipino@edu.pl',
        null, 'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', -1, null),
       (-8, now(), null, 1, true, '375003421', true, 0, 'Jakub', 'pl', null, null, 'LIGHT', null, null, 'Trapczak', 'jakub', 'jakub@edu.pl',
        null, 'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', -1, null);

-- Przypisanie ról użytkownikom
INSERT INTO role (access_level, id, creation_date, modification_date, version, enabled, created_by, modified_by,
                  account_id)
VALUES ('ADMIN', -1, now(), null, 1, true, -1, null, -1),
       ('ADMIN', -2, now(), null, 1, true, -1, null, -2),
       ('MANAGER', -3, now(), null, 1, true, -1, null, -2),
       ('MANAGER', -4, now(), null, 1, true, -1, null, -3),
       ('CLIENT', -5, now(), null, 1, true, -1, null, -4),
       ('MANAGER', -6, now(), null, 1, true, -1, null, -5),
       ('MANAGER', -7, now(), null, 1, true, -1, null, -6),
       ('CLIENT', -8, now(), null, 1, true, -1, null, -7),
       ('MANAGER', -9, now(), null, 1, true, -1, null, -8);


-- Inicjalizacja miast
INSERT INTO city (id, creation_date, modification_date, version, description, name, created_by, modified_by)
VALUES (-1, now(), null, 1, 'Super perspektywy', 'Łódź', -1, null),
       (-2, now(), null, 1, 'Stolica Polski', 'Warszawa', -1, null);

-- Inicjalizacja hoteli
INSERT INTO hotel (id, creation_date, modification_date, version, address, name, rating, created_by, modified_by, city_id)
VALUES (-1, now(), null, 1, 'Krakowska 21', 'Pet-Land', 4.0, -1, null, -1),
       (-2, now(), null, 1, 'Paderewskiego 3', 'Zwierzogród', null, -1, null, -1),
       (-3, now(), null, 1, 'Kredytowa 69', 'Psy i My', null, -1, null, -2),
       (-4, now(), null, 1, 'Mickiewicza 1', 'Jet Pet', null, -1, null, -2);

-- Inicjalizacja tabel szczegółowych rozszerzających role
INSERT INTO admin_data (id)
VALUES (-1);
INSERT INTO admin_data (id)
VALUES (-2);
INSERT INTO manager_data (id, hotel_id)
VALUES (-3, -1);
INSERT INTO manager_data (id, hotel_id)
VALUES (-4, -2);
INSERT INTO manager_data (id, hotel_id)
VALUES (-6, -3);
INSERT INTO manager_data (id)
VALUES (-7);
INSERT INTO manager_data (id)
VALUES (-8);
INSERT INTO client_data (id)
VALUES (-5);
INSERT INTO client_data (id)
VALUES (-8);

-- Inicjalizacja boxów
INSERT INTO box (id, creation_date, modification_date, version, animal_type, price_per_day, created_by, modified_by, hotel_id)
VALUES (-1, now(), null, 1, 1, 55.55, -2, null, -1),
       (-2, now(), null, 1, 1, 55.55, -2, null, -1),
       (-3, now(), null, 1, 2, 39.99, -2, null, -1),
       (-4, now(), null, 1, 3, 43.90, -2, null, -1),
       (-5, now(), null, 1, 4, 29.99, -3, null, -2),
       (-6, now(), null, 1, 5, 35.22, -3, null, -2),
       (-7, now(), null, 1, 6, 49.90, -3, null, -2),
       (-8, now(), null, 1, 1, 39.90, -5, null, -3),
       (-9, now(), null, 1, 1, 29.99, -5, null, -3),
       (-10, now(), null, 1, 1, 34.90, -5, null, -3),
       (-11, now(), null, 1, 2, 43.44, -6, null, -4),
       (-12, now(), null, 1, 4, 32.14, -6, null, -4),
       (-13, now(), null, 1, 5, 48.49, -6, null, -4),
       (-14, now(), null, 1, 7, 29.44, -6, null, -4);

-- Inicjalizacja rezerwacji
INSERT INTO booking (id, creation_date, modification_date, version, date_from, date_to, price, status, created_by, modified_by, account_id)
VALUES (-1, now(), null ,1 , current_timestamp, current_timestamp + INTERVAL '1 week', 388.85, 3, -4, null, -4),
       (-2, now() - INTERVAL '1 week', null ,1 ,now() - INTERVAL '1 week', current_timestamp - INTERVAL '5 day', 191.08, 4, -4, null, -4),
       (-3, now(), null, 1, current_timestamp + INTERVAL '1 week', current_timestamp + INTERVAL '2 week', 209.93, 2, -7, null, -7),
       (-4, now() - INTERVAL '1 week', null, 1, now() - INTERVAL '1 week', current_timestamp - INTERVAL '3 day', 175.60, 4, -7, null, -7);

-- Inicjalizacja booking line
INSERT INTO booking_line(id, creation_date, modification_date, version, price_per_day, created_by, modified_by,
                                       booking_id, box_id)
VALUES (-1, now(), null, 1, 55.55, -4, null, -1, -1),
       (-2, now() - INTERVAL '1 week', null, 1, 55.55, -4, null, -2, -2),
       (-3, now() - INTERVAL '1 week', null, 1, 39.99, -4, null, -2, -3),
       (-4, now(), null, 1, 29.99, -7, null, -3, -5),
       (-5, now() - INTERVAL '1 week', null, 1, 43.90, -7, null, -4, -4);

-- Inicjalizacja ratingów
INSERT INTO  rating(id, creation_date, modification_date, version, comment, hidden, rate, created_by, modified_by, booking_id)
VALUES (-1, now(), null, 1, 'Bardzo fajny hotel, ale się nie wyspałem', false, 4, -4, null, -2);