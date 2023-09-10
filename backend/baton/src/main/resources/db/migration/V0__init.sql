create table member
(
    id         bigint auto_increment
        primary key,
    name       varchar(255) not null,
    social_id  varchar(255) not null,
    oauth_id   varchar(255) not null,
    github_url varchar(255) not null,
    image_url  varchar(255) not null,
    company    varchar(255) not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    deleted_at datetime(6) null
);

create table runner
(
    id           bigint auto_increment
        primary key,
    introduction varchar(255) not null,
    created_at   datetime(6) not null,
    updated_at   datetime(6) not null,
    deleted_at   datetime(6) null,
    member_id    bigint       not null,
    constraint UK_lk1nbff3heosfroj0h6joq0a7
        unique (member_id),
    constraint fk_runner_to_member
        foreign key (member_id) references member (id)
);

create table supporter
(
    id           bigint auto_increment
        primary key,
    review_count int default 0 null,
    introduction varchar(255) not null,
    created_at   datetime(6) not null,
    updated_at   datetime(6) not null,
    deleted_at   datetime(6) null,
    member_id    bigint       not null,
    constraint UK_113bhk4mjpi996rm5uyj7eva9
        unique (member_id),
    constraint fk_supporter_to_member
        foreign key (member_id) references member (id)
);

create table runner_post
(
    id               bigint auto_increment
        primary key,
    title            varchar(255)  not null,
    contents         text          not null,
    pull_request_url varchar(2083) not null,
    deadline         datetime(6) not null,
    watch_count      int default 0 not null,
    review_status    enum ('DONE', 'IN_PROGRESS', 'NOT_STARTED', 'OVERDUE') not null,
    created_at       datetime(6) not null,
    updated_at       datetime(6) not null,
    deleted_at       datetime(6) null,
    runner_id        bigint        not null,
    supporter_id     bigint null,
    constraint fk_runner_post_to_runner
        foreign key (runner_id) references runner (id),
    constraint fk_runner_post_to_supporter
        foreign key (supporter_id) references supporter (id)
);

create table supporter_feedback
(
    id             bigint auto_increment
        primary key,
    review_type    enum ('BAD', 'GOOD', 'GREAT') not null,
    description    text null,
    created_at     datetime(6) not null,
    updated_at     datetime(6) not null,
    deleted_at     datetime(6) null,
    supporter_id   bigint not null,
    runner_id      bigint not null,
    runner_post_id bigint not null,
    constraint UK_h2msmse508dwfs8uevwhq2xsn
        unique (runner_post_id),
    constraint fk_supporter_feed_back_to_runner
        foreign key (runner_id) references runner (id),
    constraint fk_supporter_feed_back_to_runner_post
        foreign key (runner_post_id) references runner_post (id),
    constraint fk_supporter_feed_back_to_supporter
        foreign key (supporter_id) references supporter (id)
);

create table supporter_runner_post
(
    id             bigint auto_increment
        primary key,
    message        varchar(255) not null,
    created_at     datetime(6) not null,
    updated_at     datetime(6) not null,
    deleted_at     datetime(6) null,
    supporter_id   bigint       not null,
    runner_post_id bigint       not null,
    constraint fk_support_runner_post_to_runner_post
        foreign key (runner_post_id) references runner_post (id),
    constraint fk_support_runner_post_to_supporter
        foreign key (supporter_id) references supporter (id)
);

create table tag
(
    id           bigint auto_increment
        primary key,
    name         varchar(255) not null,
    reduced_name varchar(255) not null,
    constraint UK_1wdpsed5kna2y38hnbgrnhi5b
        unique (name)
);

create table runner_post_tag
(
    id             bigint auto_increment
        primary key,
    runner_post_id bigint not null,
    tag_id         bigint not null,
    constraint fk_runner_post_tag_to_runner_post
        foreign key (runner_post_id) references runner_post (id),
    constraint fk_runner_post_tag_to_tag
        foreign key (tag_id) references tag (id)
);

create table technical_tag
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null,
    constraint UK_cocro1c4s00vt166wkg6assr
        unique (name)
);

create table runner_technical_tag
(
    id               bigint auto_increment
        primary key,
    runner_id        bigint not null,
    technical_tag_id bigint not null,
    constraint fk_runner_technical_tag_to_runner
        foreign key (runner_id) references runner (id),
    constraint fk_runner_technical_tag_to_technical_tag
        foreign key (technical_tag_id) references technical_tag (id)
);

create table supporter_technical_tag
(
    id               bigint auto_increment
        primary key,
    supporter_id     bigint not null,
    technical_tag_id bigint not null,
    constraint fk_supporter_technical_tag_to_supporter
        foreign key (supporter_id) references supporter (id),
    constraint fk_supporter_technical_tag_to_technical_tag
        foreign key (technical_tag_id) references technical_tag (id)
);
