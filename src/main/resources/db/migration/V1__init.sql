CREATE TABLE user
(
    user_id           CHAR(26)     NOT NULL COMMENT '유저 아이디',
    email             VARCHAR(255) NOT NULL COMMENT '이메일',
    name              VARCHAR(128) NOT NULL COMMENT '이름',
    tag               CHAR(4)      NOT NULL COMMENT '태그',
    profile_image_url VARCHAR(255) NOT NULL COMMENT '프사URL',
    created_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_At        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY user_pk(user_id)
) DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci COMMENT='유저';
CREATE UNIQUE INDEX user_uk1 ON user (email);
CREATE UNIQUE INDEX user_uk2 ON user (name, tag);

CREATE TABLE user_notification_config
(
    user_id        CHAR(26) NOT NULL COMMENT '유저 아이디',
    social_enabled BOOLEAN  NOT NULL COMMENT '소셜 알림 여부',
    app_enabled    BOOLEAN  NOT NULL COMMENT '앱 알림 여부',
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_At     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY user_notification_config_pk(user_id)
) DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci COMMENT='유저알림';

CREATE TABLE social_user
(
    provider   VARCHAR(64)  NOT NULL COMMENT '제공자',
    identifier VARCHAR(128) NOT NULL COMMENT 'id',
    user_id    CHAR(26)     NOT NULL COMMENT '유저 아이디',
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_At DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY social_user_pk(provider, identifier)
) DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci COMMENT='유저';
CREATE INDEX social_user_idx1 ON social_user (user_id);

CREATE TABLE user_token
(
    user_id       CHAR(26)     NOT NULL COMMENT '유저 아이디',
    refresh_token VARCHAR(256) NOT NULL COMMENT '리프레시 토큰',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_At    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY user_token_pk(user_id, refresh_token)
) DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci COMMENT='유저 토큰';
CREATE INDEX user_token_idx1 ON user_token (refresh_token);

CREATE TABLE user_device
(
    user_id      CHAR(26)     NOT NULL COMMENT '유저 아이디',
    device_token VARCHAR(256) NOT NULL COMMENT '디바이스 토큰',
    device_info  VARCHAR(256) NOT NULL COMMENT '디바이스 정보',
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_At   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY user_device_pk(user_id, device_token)
) DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci COMMENT='유저 기기';
CREATE INDEX user_device_idx1 ON user_device (device_token);

CREATE TABLE diary
(
    diary_id     CHAR(26)    NOT NULL COMMENT '일기 아이디',
    user_id      CHAR(26)    NOT NULL COMMENT '유저 아이디',
    ymd          DATE        NOT NULL COMMENT '날짜',
    emotion      VARCHAR(64) NOT NULL COMMENT '감정',
    content      TEXT        NOT NULL COMMENT '내용',
    content_hash CHAR(255)   NOT NULL COMMENT '해시',
    created_at   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_At   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY diary_pk(diary_id)
) DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci COMMENT='일기';
CREATE UNIQUE INDEX diary_uk1 ON diary (user_id, ymd);
CREATE INDEX diary_idx1 ON diary (ymd);
CREATE INDEX diary_idx2 ON diary (ymd, emotion);
CREATE INDEX diary_idx3 ON diary (emotion);

CREATE TABLE friend
(
    from_user_id     CHAR(26) NOT NULL COMMENT '친구A 아이디',
    to_user_id       CHAR(26) NOT NULL COMMENT '친구B 아이디',
    is_diary_allowed BOOLEAN  NOT NULL COMMENT '일기 공개 여부',
    created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_At       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY friend_pk(from_user_id, to_user_id)
) DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci COMMENT='친구';
CREATE INDEX friend_idx1 ON friend (to_user_id);
CREATE INDEX friend_idx2 ON friend (to_user_id, is_diary_allowed);

CREATE TABLE friend_request
(
    from_user_id CHAR(26) NOT NULL COMMENT '친구A 아이디',
    to_user_id   CHAR(26) NOT NULL COMMENT '친구B 아이디',
    created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_At   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY friend_request_pk(from_user_id, to_user_id)
) DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci COMMENT='친구요청';
CREATE INDEX friend_request_idx1 ON friend_request (to_user_id);

CREATE TABLE app_info
(
    app_key CHAR(36) NOT NULL COMMENT '앱키',
    app_name VARCHAR(64) NOT NULL COMMENT '앱이름',
    app_version VARCHAR(64) NOT NULL COMMENT '앱버전',
    in_service BOOLEAN NOT NULL COMMENT '서비스 여부',
    in_review BOOLEAN NOT NULL COMMENT '심사중 여부',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_At DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY app_info_pk(app_key)
) DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci COMMENT='앱정보';
CREATE UNIQUE INDEX app_info_uk1 ON app_info(app_version);

INSERT INTO app_info(app_key, app_name, app_version, in_service, in_review) VALUES('26d05985-803c-41ad-96ed-65d0f9c84922', 'Magcloud App', '0.0.1', true, false);
