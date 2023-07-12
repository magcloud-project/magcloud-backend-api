ALTER TABLE user_notification_config
    ADD COLUMN feed_enabled BOOLEAN NOT NULL DEFAULT TRUE COMMENT '피드 알림' AFTER app_enabled;
