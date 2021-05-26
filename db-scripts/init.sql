-- Utworzenie kont użytkowników
INSERT INTO account (id, creation_date, modification_date, version, confirmed, contact_number, enabled,
                     failed_login_attempts_counter, firstname, language, last_failed_login_date,
                     last_failed_login_ip_address, theme_color, last_successful_login_date, last_successful_login_ip_address,
                     lastname, login, email, new_email, password, created_by, modified_by)
VALUES (-1, now(), null, 1, true, null, true, 0, 'Admin', 'pl', null, null, 'LIGHT', null, null, 'Adminski', 'admin1', 'admin@ssbd06.pl',
        null, 'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', -1, null),
       (-2, now(), null, 1, true, null, true, 0, 'Mateusz', 'pl', null, null, 'LIGHT', null, null, 'Chrzanowski', 'mchrzan','mchrzan@edu.pl',
        null, 'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', -1, null),
       (-3, now(), null, 1, true, null, true, 0, 'Michał', 'pl', null, null, 'LIGHT', null, null, 'Masłowski', 'mima', 'mima@edu.pl',
        null, 'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', -1, null),
       (-4, now(), null, 1, true, null, true, 0, 'Marcin', 'pl', null, null, 'LIGHT', null, null, 'Borowski', 'marbor', 'marbor@edu.pl',
        null, 'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', -1, null);

-- Przypisanie ról użytkownikom
INSERT INTO role (access_level, id, creation_date, modification_date, version, enabled, created_by, modified_by,
                  account_id)
VALUES ('ADMIN', -1, now(), null, 1, true, -1, null, -1),
       ('ADMIN', -2, now(), null, 1, true, -1, null, -2),
       ('MANAGER', -3, now(), null, 1, true, -1, null, -2),
       ('MANAGER', -4, now(), null, 1, true, -1, null, -3),
       ('CLIENT', -5, now(), null, 1, true, -1, null, -4);

-- Inicjalizacja tabel szczegółowych rozszerzających role
INSERT INTO admin_data (id)
values (-1);
INSERT INTO admin_data (id)
values (-2);
INSERT INTO manager_data (id)
values (-3);
INSERT INTO manager_data (id)
values (-4);
INSERT INTO client_data (id)
values (-5);
