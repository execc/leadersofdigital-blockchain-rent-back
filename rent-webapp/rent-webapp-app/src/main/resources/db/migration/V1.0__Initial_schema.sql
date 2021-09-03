-- Empty migration to keep flyway from exiting
--
create table contract_events (contract_id varchar(255) not null, event_id varchar(255) not null, event_order int4 not null, primary key (contract_id, event_order));
create table payment_event (id varchar(255) not null, amount float8 not null, credit_part float8 not null, date timestamp, debt_part float8 not null, earning float8 not null, type varchar(255), primary key (id));
create table rent_contract_entity (id varchar(255) not null, bank_address varchar(255), bank_name varchar(255), bankuuid uuid, conditions_id varchar(255), date varchar(255), earning_credit_percent float8 not null, earning_percent float8 not null, end_date varchar(255), interest_rate float8 not null, "cond_limit" float8 not null, payment_amount float8 not null, credit_debt float8 not null, status varchar(255), tenant_address varchar(255), tenant_name varchar(255), tenantuuid uuid, total_debt float8 not null, total_earnings float8 not null, primary key (id));
alter table if exists contract_events add constraint UK_rworvogces5q416ohnoetq3n9 unique (event_id);
alter table if exists contract_events add constraint FK5wl1q4yk9d5t3w36ee1cihj9b foreign key (event_id) references payment_event;
alter table if exists contract_events add constraint FK77a09ky2c8kt9plxwocp7a5v6 foreign key (contract_id) references rent_contract_entity;