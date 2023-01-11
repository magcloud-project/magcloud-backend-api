/** Create Tables **/
create table user (id bigint not null, created_at datetime(6), updated_at datetime(6), email varchar(255), name varchar(128), password varchar(255), provider varchar(32), user_identifier varchar(255), primary key (id)) engine=InnoDB;
create table user_device (fcm_token varchar(255) not null, user_id bigint not null, primary key (fcm_token, user_id)) engine=InnoDB;
create table user_diary (id bigint not null, created_at datetime(6), updated_at datetime(6), content mediumtext, date date, user_id bigint, primary key (id)) engine=InnoDB;
create table user_diary_emotion (emotion varchar(255) not null, value float(53) not null, diary_id bigint not null, primary key (diary_id, emotion)) engine=InnoDB;
create table user_tags (tag_id bigint not null, user_id bigint not null, primary key (tag_id, user_id)) engine=InnoDB;
create table user_token (id bigint not null, refresh_token varchar(255), primary key (id)) engine=InnoDB;
create table tags (id bigint not null, name varchar(255), primary key (id)) engine=InnoDB;
create table tags_seq (next_val bigint) engine=InnoDB;
insert into tags_seq values ( 1 );

/** Add Sequential ID Table **/
create table user_diary_seq (next_val bigint) engine=InnoDB;
insert into user_diary_seq values ( 1 );
create table user_seq (next_val bigint) engine=InnoDB;
insert into user_seq values ( 1 );

/** Create Indexes **/
alter table user add constraint idx_user_identity_provider unique (provider, user_identifier);
alter table user add constraint idx_user_email unique (email);
alter table user_diary add constraint idx_user_diary_date unique (date, user_id);

/** Create FK Constraints **/
alter table user_device add constraint FK_user_device_user_id foreign key (user_id) references user (id) ON DELETE CASCADE ON UPDATE CASCADE;
alter table user_diary add constraint FK_user_diary_user_id foreign key (user_id) references user (id) ON DELETE CASCADE ON UPDATE CASCADE;
alter table user_diary_emotion add constraint FK_diary_emotion_diary_id foreign key (diary_id) references user_diary (id) ON DELETE CASCADE ON UPDATE CASCADE;
alter table user_tags add constraint FK_user_tags_tag_id foreign key (tag_id) references tags (id) ON DELETE CASCADE ON UPDATE CASCADE;
alter table user_tags add constraint FK_user_tags_user_id foreign key (user_id) references user (id) ON DELETE CASCADE ON UPDATE CASCADE;
alter table user_token add constraint FK_user_token_id foreign key (id) references user (id) ON DELETE CASCADE ON UPDATE CASCADE;
