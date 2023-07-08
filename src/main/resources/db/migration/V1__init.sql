CREATE TABLE user(
    user_id CHAR(26) NOT NULL COMMENT '유저 아이디',
    email VARCHAR(255) NOT NULL COMMENT '이메일',
    name VARCHAR(128) NOT NULL COMMENT '이름',
    tag CHAR(4) NOT NULL COMMENT '태그',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_At DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY user_pk(user_id)
) DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci COMMENT='유저';
CREATE UNIQUE INDEX user_uk1 ON user(email);
CREATE UNIQUE INDEX user_uk2 ON user(name, tag);

CREATE TABLE user_social(
    provider VARCHAR(64) NOT NULL COMMENT '제공자',
    identifier VARCHAR(128) NOT NULL COMMENT 'id',
    user_id CHAR(26) NOT NULL COMMENT '유저 아이디',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_At DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY user_social_pk(provider, identifier)
) DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci COMMENT='유저';
CREATE UNIQUE INDEX user_social_idx1 ON user_social(user_id);

CREATE TABLE user_token(
    user_id CHAR(26) NOT NULL COMMENT '유저 아이디',
    refresh_token VARCHAR(256) NOT NULL COMMENT '리프레시 토큰',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_At DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY user_token_pk(user_id, refresh_token)
) DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci COMMENT='유저 토큰';

CREATE TABLE user_device(
    user_id CHAR(26) NOT NULL COMMENT '유저 아이디',
    device_token VARCHAR(256) NOT NULL COMMENT '디바이스 토큰',
    device_info VARCHAR(256) NOT NULL COMMENT '디바이스 정보',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_At DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY user_device_pk(user_id, device_token)
) DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci COMMENT='유저 기기';

CREATE TABLE diary(
    diary_id CHAR(26) NOT NULL COMMENT '일기 아이디',
    user_id CHAR(26) NOT NULL COMMENT '유저 아이디',
    ymd DATE NOT NULL COMMENT '날짜',
    emotion VARCHAR(64) NOT NULL COMMENT '감정',
    content TEXT NOT NULL COMMENT '내용',
    content_hash CHAR(255) NOT NULL COMMENT '해시',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_At DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY diary_pk(diary_id)
) DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci COMMENT='일기';
CREATE UNIQUE INDEX diary_uk1 ON diary(user_id, ymd);
