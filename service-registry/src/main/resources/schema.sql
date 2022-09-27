create table service(
   id serial primary key,
   name varchar(100) not null,
   number integer not null default 1,
   host varchar(200) not null unique,
   registration_ts timestamp not null default CURRENT_TIMESTAMP,
   abandon_ts timestamp default null
);

create table service_uptime (
  service_id integer primary key references service(id),
  last_heartbeat_ts timestamp,
  lag integer not null default 0,
  uptime integer not null default 0
 );

