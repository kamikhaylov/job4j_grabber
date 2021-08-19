create table if not exists posts (
    id serial primary key,
    title text,
    link text,
    description text,
    created_date timestamp
);