alter table payment_event add concession_part float8 not null default 0.0;
alter table rent_contract_entity add concession_debt float8 not null default 0.0;
alter table rent_contract_entity add concession_earnings float8 not null default 0.0;
alter table rent_contract_entity add min_guaranteed_concession float8 not null default 0.0;
alter table rent_contract_entity add concession_percent float8 not null default 0.0;