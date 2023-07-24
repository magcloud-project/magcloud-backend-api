CREATE TABLE diary_comment
(
    diary_comment_id CHAR(26)    NOT NULL COMMENT '일기 댓글 아이디',
    diary_id         CHAR(26)    NOT NULL COMMENT '일기 아이디',
    user_id          CHAR(26)    NOT NULL COMMENT '유저 아이디',
    content          TEXT        NOT NULL COMMENT '내용',
    created_at       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_At       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY diary_comment_pk(diary_comment_id)
) DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci COMMENT='일기 댓글';
CREATE INDEX diary_comment_idx1 ON diary_comment (diary_id);
