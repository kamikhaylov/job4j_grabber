create table if not exists posts (
    id serial primary key,
    name text,
    link text,
    description text,
    created_date timestamp
);