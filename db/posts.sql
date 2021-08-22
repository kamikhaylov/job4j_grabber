create table if not exists posts (
    id serial primary key,
    title text,
    link text unique,
    description text,
    created_date timestamp
);