ALTER TABLE diary
    ADD COLUMN like_count INT NOT NULL DEFAULT 0 COMMENT '좋아요 수' AFTER emotion;

CREATE TABLE diary_like(
    diary_id CHAR(26) NOT NULL COMMENT '일기 아이디',
    user_id CHAR(26) NOT NULL COMMENT '유저 아이디',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_At DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY diary_like_pk(diary_id, user_id)
) DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci COMMENT='일기 좋아요';
