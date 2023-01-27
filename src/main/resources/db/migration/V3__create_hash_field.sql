alter table user_diary
    add column content_hash CHAR(64) not null;
