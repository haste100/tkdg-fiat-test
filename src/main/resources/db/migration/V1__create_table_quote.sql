create sequence IF NOT EXISTS quote_id_seq
START WITH 1
INCREMENT BY 50
;

create table quote(
    id number not null primary key,
    isin varchar(12),
    bid number,
    ask number
);