INSERT INTO account (id, creation_date, modification_date, version, confirmed, contact_number, enabled, failed_login_attempts_counter, firstname, language, last_failed_login_date, last_failed_login_ip_address, last_successful_login_date, last_successful_login_ip_address, lastname, login, password, created_by, modified_by)
VALUES (1, now(), null, 1, true, null, true, 0, 'admin', 'PL', null, null, null, null, 'adminski', 'admin', 'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', 1, null)

INSERT INTO role (access_level, id, creation_date, modification_date, version, enabled, created_by, modified_by, account_id)
VALUES ('ADMIN', 1, now(), null, 1, true, 1, null, 1)