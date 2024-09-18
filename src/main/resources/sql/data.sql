create table enroll
(
    id          bigint auto_increment
        primary key,
    user_id     bigint                             not null,
    number      varchar(128)                       not null,
    name        varchar(128)                       not null,
    major_class varchar(128)                       not null,
    telephone   varchar(128)                       not null,
    intention   varchar(16)                        not null comment '方向',
    first_time  int                                not null comment '一面时间',
    second_time int      default 0                 null comment '二面时间',
    status      int      default 0                 null,
    create_time datetime default CURRENT_TIMESTAMP null,
    update_time datetime default CURRENT_TIMESTAMP null
);

create table interview_time
(
    id             bigint auto_increment
        primary key,
    interview_type int                                not null,
    start_time     datetime                           not null,
    end_time       datetime                           not null,
    create_time    datetime default CURRENT_TIMESTAMP null,
    update_time    datetime default CURRENT_TIMESTAMP null,
    count          int      default 0                 null,
    version        int      default 0                 null
);

create table user
(
    id          bigint auto_increment
        primary key,
    email       varchar(128)                       not null,
    password    varchar(128)                       not null,
    create_time datetime default CURRENT_TIMESTAMP null,
    update_time datetime default CURRENT_TIMESTAMP null
);

