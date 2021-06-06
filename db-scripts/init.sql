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
       (-6, now(), null, 1, true, '356897453', true, 0, 'Zbycholud', 'pl', null, null, 'DARK', null, null, 'Pakleza', 'byniolus', 'byniolus@edu.pl',
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
       ('MANAGER', -7, now(), null, 1, true, -1, null, -6);


-- Inicjalizacja miast
INSERT INTO city (id, creation_date, modification_date, version, description, name, created_by, modified_by)
VALUES (-1, now(), null, 1, 'Super perspektywy', 'Łódź', -1, null),
       (-2, now(), null, 1, 'Stolica', 'Warszawa', -1, null);

-- Inicjalizacja hoteli
INSERT INTO hotel (id, creation_date, modification_date, version, address, name, rating, created_by, modified_by, city_id)
VALUES (-1, now(), null, 1, 'Papieska 21/37', 'Klatkowo', 0, -1, null, -1),
       (-2, now(), null, 1, 'Zbrodniarzy 3', 'Zwierzogród', 0, -1, null, -1),
       (-3, now(), null, 1, 'Kredytowa 69', 'Pet-Land', 0, -1, null, -2),
       (-4, now(), null, 1, 'Mickiewicza 1', 'Zwierzex', 0, -1, null, -2);

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
INSERT INTO manager_data (id, hotel_id)
VALUES (-7, -4);
INSERT INTO client_data (id)
VALUES (-5);

-- Inicjalizacja boxów
INSERT INTO box (id, creation_date, modification_date, version, animal_type, price_per_day, created_by, modified_by, hotel_id)
VALUES (-1, now(), null, 1, 1, 55.55, -2, null, -1);