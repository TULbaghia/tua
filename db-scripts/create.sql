create sequence seq_account_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence seq_booking_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence seq_booking_line_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence seq_box_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence seq_city_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence seq_hotel_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence seq_pending_code_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence seq_rating_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence seq_role_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create table account
(
    id                               bigint       not null,
    creation_date                    timestamp    not null,
    modification_date                timestamp,
    version                          bigint       not null,
    confirmed                        boolean      not null,
    contact_number                   varchar(15),
    enabled                          boolean      not null,
    failed_login_attempts_counter    integer default 0,
    firstname                        varchar(31)  not null,
    language                         varchar(2),
    last_failed_login_date           timestamp,
    last_failed_login_ip_address     bytea,
    last_successful_login_date       timestamp,
    last_successful_login_ip_address bytea,
    lastname                         varchar(31)  not null,
    login                            varchar(127) not null,
    password                         varchar(64)  not null,
    created_by                       bigint       not null,
    modified_by                      bigint,
    constraint pk_account_id
        primary key (id),
    constraint uk_account_contact_number
        unique (contact_number),
    constraint uk_account_login
        unique (login),
    constraint fk_account_account_created_by
        foreign key (created_by) references account,
    constraint fk_account_account_modified_by
        foreign key (modified_by) references account,
    constraint account_failed_login_attempts_counter_check
        check (failed_login_attempts_counter >= 0)
);

create index ix_account_created_by
    on account (created_by);

create index ix_account_modified_by
    on account (modified_by);

create table booking
(
    id                bigint        not null,
    creation_date     timestamp     not null,
    modification_date timestamp,
    version           bigint        not null,
    date_from         timestamp     not null,
    date_to           timestamp     not null,
    price             numeric(8, 2) not null,
    status            integer       not null,
    created_by        bigint        not null,
    modified_by       bigint,
    account_id        bigint        not null,
    constraint pk_booking_id
        primary key (id),
    constraint fk_booking_account_created_by
        foreign key (created_by) references account,
    constraint fk_booking_account_modified_by
        foreign key (modified_by) references account,
    constraint fk_booking_account_account_id
        foreign key (account_id) references account,
    constraint booking_price_check
        check (price >= (0)::numeric),
    constraint booking_dates_check
        check (date_from < date_to)
);

create index ix_booking_account_id
    on booking (account_id);

create index ix_booking_created_by
    on booking (created_by);

create index ix_booking_modified_by
    on booking (modified_by);

create table city
(
    id                bigint       not null,
    creation_date     timestamp    not null,
    modification_date timestamp,
    version           bigint       not null,
    description       varchar(255) not null,
    name              varchar(31)  not null,
    created_by        bigint       not null,
    modified_by       bigint,
    constraint pk_city_id
        primary key (id),
    constraint uk_city_name
        unique (name),
    constraint fk_city_account_created_by
        foreign key (created_by) references account,
    constraint fk_city_account_modified_by
        foreign key (modified_by) references account
);

create index ix_city_created_by
    on city (created_by);

create index ix_city_modified_by
    on city (modified_by);

create table hotel
(
    id                bigint        not null,
    creation_date     timestamp     not null,
    modification_date timestamp,
    version           bigint        not null,
    address           varchar(63)   not null,
    name              varchar(63)   not null,
    rating            numeric(2, 1) not null,
    created_by        bigint        not null,
    modified_by       bigint,
    city_id           bigint        not null,
    constraint pk_hotel_id
        primary key (id),
    constraint uk_hotel_name
        unique (name),
    constraint fk_hotel_account_created_by
        foreign key (created_by) references account,
    constraint fk_hotel_account_modified_by
        foreign key (modified_by) references account,
    constraint fk_hotel_city_city_id
        foreign key (city_id) references city,
    constraint hotel_rating_check
        check ((rating <= (5)::numeric) AND (rating >= (1)::numeric))
);

create index ix_hotel_city_id
    on hotel (city_id);

create index ix_hotel_created_by
    on hotel (created_by);

create index ix_hotel_modified_by
    on hotel (modified_by);

create table box
(
    id                bigint        not null,
    creation_date     timestamp     not null,
    modification_date timestamp,
    version           bigint        not null,
    animal_type       integer       not null,
    price_per_day     numeric(8, 2) not null,
    created_by        bigint        not null,
    modified_by       bigint,
    hotel_id          bigint        not null,
    constraint pk_box_id
        primary key (id),
    constraint fk_box_account_created_by
        foreign key (created_by) references account,
    constraint fk_box_account_modified_by
        foreign key (modified_by) references account,
    constraint fk_box_hotel_hotel_id
        foreign key (hotel_id) references hotel,
    constraint box_price_per_day_check
        check (price_per_day >= (0)::numeric)
);

create index ix_box_hotel_id
    on box (hotel_id);

create index ix_box_created_by
    on box (created_by);

create index ix_box_modified_by
    on box (modified_by);

create table booking_line
(
    id                bigint        not null,
    creation_date     timestamp     not null,
    modification_date timestamp,
    version           bigint        not null,
    price_per_day     numeric(8, 2) not null,
    created_by        bigint        not null,
    modified_by       bigint,
    booking_id        bigint        not null,
    box_id            bigint        not null,
    constraint pk_booking_line_id
        primary key (id),
    constraint fk_booking_line_account_created_by
        foreign key (created_by) references account,
    constraint fk_booking_line_account_modified_by
        foreign key (modified_by) references account,
    constraint fk_booking_line_booking_booking_id
        foreign key (booking_id) references booking,
    constraint fk_booking_line_box_box_id
        foreign key (box_id) references box,
    constraint booking_line_price_per_day_check
        check (price_per_day >= (0)::numeric)
);

create index ix_booking_line_booking_id
    on booking_line (booking_id);

create index ix_booking_line_box_id
    on booking_line (box_id);

create index ix_booking_line_created_by
    on booking_line (created_by);

create index ix_booking_line_modified_by
    on booking_line (modified_by);

create table pending_code
(
    id                bigint       not null,
    creation_date     timestamp    not null,
    modification_date timestamp,
    version           bigint       not null,
    code              varchar(128) not null,
    code_type         integer      not null,
    used              boolean      not null,
    created_by        bigint       not null,
    modified_by       bigint,
    account_id        bigint       not null,
    constraint pk_pending_code_id
        primary key (id),
    constraint uk_pending_code_code
        unique (code),
    constraint fk_pending_code_account_created_by
        foreign key (created_by) references account,
    constraint fk_pending_code_account_modified_by
        foreign key (modified_by) references account,
    constraint fk_pending_code_account_account_id
        foreign key (account_id) references account
);

create index ix_pending_code_account_id
    on pending_code (account_id);

create index ix_pending_code_created_by
    on pending_code (created_by);

create index ix_pending_code_modified_by
    on pending_code (modified_by);

create table rating
(
    id                bigint    not null,
    creation_date     timestamp not null,
    modification_date timestamp,
    version           bigint    not null,
    comment           varchar(255),
    hidden            boolean   not null,
    rate              smallint  not null,
    created_by        bigint    not null,
    modified_by       bigint,
    booking_id        bigint    not null,
    constraint pk_rating_id
        primary key (id),
    constraint uk_rating_booking_id
        unique (booking_id),
    constraint fk_rating_account_created_by
        foreign key (created_by) references account,
    constraint fk_rating_account_modified_by
        foreign key (modified_by) references account,
    constraint fk_rating_booking_booking_id
        foreign key (booking_id) references booking,
    constraint rating_rate_check
        check ((rate <= 5) AND (rate >= 1))
);

create index ix_rating_booking_id
    on rating (booking_id);

create index ix_rating_created_by
    on rating (created_by);

create index ix_rating_modified_by
    on rating (modified_by);

create table role
(
    access_level      varchar(31) not null,
    id                bigint      not null,
    creation_date     timestamp   not null,
    modification_date timestamp,
    version           bigint      not null,
    enabled           boolean     not null,
    created_by        bigint      not null,
    modified_by       bigint,
    account_id        bigint      not null,
    constraint pk_role_id
        primary key (id),
    constraint uk_role_access_level_account_id
        unique (access_level, account_id),
    constraint fk_role_account_created_by
        foreign key (created_by) references account,
    constraint fk_role_account_modified_by
        foreign key (modified_by) references account,
    constraint fk_role_account_account_id
        foreign key (account_id) references account
);

create index ix_role_account_id
    on role (account_id);

create index ix_role_created_by
    on role (created_by);

create index ix_role_modified_by
    on role (modified_by);

create table admin_data
(
    id bigint not null,
    constraint pk_admin_data_id
        primary key (id),
    constraint fk_admin_data_role_id
        foreign key (id) references role
);

create table client_data
(
    id bigint not null,
    constraint pk_client_data_id
        primary key (id),
    constraint fk_client_data_role_id
        foreign key (id) references role
);

create table manager_data
(
    id       bigint not null,
    hotel_id bigint,
    constraint pk_manager_data_id
        primary key (id),
    constraint fk_manager_data_role_id
        foreign key (id) references role,
    constraint fk_manager_data_hotel_hotel_id
        foreign key (hotel_id) references hotel
);

create index ix_manager_data_hotel_id
    on manager_data (hotel_id);

create view auth_view (login, password, role) as
select a.login,
       a.password,
       r.access_level as role
from account a
         join role r on a.id = r.account_id
where a.confirmed = true
  and a.enabled = true