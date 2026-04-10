-- Index tuning perf seed
-- Run after 01-pagination-perf-seed.sql

SET SESSION cte_max_recursion_depth = 20000;

-- reset relation data
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM favorite;
DELETE FROM comment;
DELETE FROM notification;
DELETE FROM marker;

SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE comment AUTO_INCREMENT = 1;
ALTER TABLE marker AUTO_INCREMENT = 1;
ALTER TABLE notification AUTO_INCREMENT = 1;

-- reset aggregates
UPDATE post
SET like_count = 0,
    comment_count = 0;

UPDATE post_list
SET like_count = 0,
    comment_count = 0;

-- favorite: 8,000
INSERT INTO favorite (user_id, post_id)
WITH RECURSIVE seq AS (
  SELECT 1 AS n
  UNION ALL
  SELECT n + 1 FROM seq WHERE n < 8000
)
SELECT
  CASE
    WHEN n <= 2000 THEN 1
    ELSE ((n - 2001) MOD 39) + 2
  END AS user_id,
  ((n - 1) MOD 15000) + 1 AS post_id
FROM seq;

-- comments: 8,001
INSERT INTO comment (user_id, post_id, content, like_count, create_date, update_date, parent_id)
VALUES (
  1,
  1,
  '인덱스 튜닝 성능 측정용 루트 댓글',
  0,
  TIMESTAMP('2026-04-10 09:00:00'),
  NULL,
  NULL
);

INSERT INTO comment (user_id, post_id, content, like_count, create_date, update_date, parent_id)
WITH RECURSIVE seq AS (
  SELECT 1 AS n
  UNION ALL
  SELECT n + 1 FROM seq WHERE n < 1000
)
SELECT
  ((n - 1) MOD 40) + 1,
  1,
  CONCAT('루트 댓글의 자식 댓글 #', n),
  0,
  TIMESTAMP('2026-04-10 09:01:00') + INTERVAL n SECOND,
  NULL,
  1
FROM seq;

INSERT INTO comment (user_id, post_id, content, like_count, create_date, update_date, parent_id)
WITH RECURSIVE seq AS (
  SELECT 1 AS n
  UNION ALL
  SELECT n + 1 FROM seq WHERE n < 3000
)
SELECT
  ((n - 1) MOD 40) + 1,
  1,
  CONCAT('post 1 성능 측정용 일반 댓글 #', n),
  0,
  TIMESTAMP('2026-04-10 09:20:00') + INTERVAL n SECOND,
  NULL,
  NULL
FROM seq;

INSERT INTO comment (user_id, post_id, content, like_count, create_date, update_date, parent_id)
WITH RECURSIVE seq AS (
  SELECT 1 AS n
  UNION ALL
  SELECT n + 1 FROM seq WHERE n < 4000
)
SELECT
  ((n - 1) MOD 40) + 1,
  ((n - 1) MOD 1000) + 2,
  CONCAT('분산 댓글 #', n),
  0,
  TIMESTAMP('2026-04-10 10:00:00') + INTERVAL n SECOND,
  NULL,
  NULL
FROM seq;

-- marker: 2,500
INSERT INTO marker (x, y, name, user_id, post_id, type)
WITH RECURSIVE seq AS (
  SELECT 1 AS n
  UNION ALL
  SELECT n + 1 FROM seq WHERE n < 2500
)
SELECT
  126.630000 + (n / 10000.0),
  37.370000 + (n / 10000.0),
  CONCAT('Index marker #', n),
  CASE
    WHEN n <= 600 THEN 1
    ELSE ((n - 1) MOD 40) + 1
  END AS user_id,
  CASE
    WHEN n <= 1000 THEN 2
    ELSE ((n - 1001) MOD 1500) + 3
  END AS post_id,
  CASE WHEN MOD(n, 2) = 0 THEN 'STORE' ELSE 'PLACE' END
FROM seq;

-- notification: 2,500
INSERT INTO notification (user_id, marker_id, type, message, create_date, update_date, date_time, radius)
SELECT
  m.user_id,
  m.marker_id,
  1,
  CONCAT('Index notification for marker #', m.marker_id),
  TIMESTAMP('2026-04-10 11:00:00') + INTERVAL m.marker_id SECOND,
  NULL,
  TIMESTAMP('2026-04-11 09:00:00') + INTERVAL m.marker_id MINUTE,
  100
FROM marker m;

-- sync aggregates
UPDATE post p
LEFT JOIN (
  SELECT post_id, COUNT(*) AS favorite_count
  FROM favorite
  GROUP BY post_id
) f ON f.post_id = p.post_id
LEFT JOIN (
  SELECT post_id, COUNT(*) AS comment_count
  FROM comment
  GROUP BY post_id
) c ON c.post_id = p.post_id
SET p.like_count = COALESCE(f.favorite_count, 0),
    p.comment_count = COALESCE(c.comment_count, 0);

UPDATE post_list pl
JOIN post p ON p.post_id = pl.post_id
SET pl.like_count = p.like_count,
    pl.comment_count = p.comment_count;
