ALTER TABLE diary
    ADD COLUMN image_url VARCHAR(255) NULL COMMENT '이미지 URL' AFTER content;
