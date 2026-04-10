-- Index tuning candidates (manual apply)
-- Apply after checking existing indexes with SHOW INDEX.

-- comment
CREATE INDEX idx_comment_post_id ON comment (post_id);
CREATE INDEX idx_comment_parent_id ON comment (parent_id);

-- marker
CREATE INDEX idx_marker_post_id ON marker (post_id);
CREATE INDEX idx_marker_user_id ON marker (user_id);

-- notification
CREATE INDEX idx_notification_marker_id ON notification (marker_id);
CREATE INDEX idx_notification_user_id ON notification (user_id);

-- post
CREATE INDEX idx_post_building_id ON post (building_id);
CREATE INDEX idx_post_user_id ON post (user_id);
