create table if not exists posts (
    id serial primary key,
    title text,
    link unique text,
    description text,
    created_date timestamp
);