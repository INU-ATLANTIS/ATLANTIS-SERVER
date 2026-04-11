-- Pagination perf seed
-- 12 buildings / 40 users / 15,000 posts / post_list refresh

SET SESSION cte_max_recursion_depth = 20000;

-- ensure post_list
CREATE TABLE IF NOT EXISTS post_list (
  post_id INT NOT NULL,
  user_id INT NOT NULL,
  title VARCHAR(255),
  content TEXT,
  like_count INT,
  comment_count INT,
  write_datetime DATETIME,
  writer_nickname VARCHAR(255),
  writer_profile_image VARCHAR(255),
  post_title_image VARCHAR(255),
  building_id INT,
  PRIMARY KEY (post_id)
);

-- reset data
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM image;
DELETE FROM favorite;
DELETE FROM comment;
DELETE FROM marker;
DELETE FROM notification;
DELETE FROM post_list;
DELETE FROM post;
DELETE FROM report;
DELETE FROM floorpic;
DELETE FROM building;
DELETE FROM user;

SET FOREIGN_KEY_CHECKS = 1;

-- reset auto increment
ALTER TABLE user AUTO_INCREMENT = 1;
ALTER TABLE post AUTO_INCREMENT = 1;
ALTER TABLE image AUTO_INCREMENT = 1;
ALTER TABLE marker AUTO_INCREMENT = 1;
ALTER TABLE notification AUTO_INCREMENT = 1;
ALTER TABLE comment AUTO_INCREMENT = 1;

-- buildings: 12
INSERT INTO building (building_id, name, x, y, office, phone, url, departments, code) VALUES
  (1, '도서관', 126.634100, 37.375100, '학술정보팀', '032-000-0001', 'https://example.com/buildings/1', '문헌정보,열람실', 'LIB'),
  (2, '학생회관', 126.634200, 37.375200, '학생지원팀', '032-000-0002', 'https://example.com/buildings/2', '학생복지,동아리', 'STD'),
  (3, '공학관', 126.634300, 37.375300, '공과대학 행정실', '032-000-0003', 'https://example.com/buildings/3', '컴퓨터,전자,기계', 'ENG'),
  (4, '인문관', 126.634400, 37.375400, '인문대 행정실', '032-000-0004', 'https://example.com/buildings/4', '국문,영문,사학', 'HUM'),
  (5, '사회과학관', 126.634500, 37.375500, '사회과학대 행정실', '032-000-0005', 'https://example.com/buildings/5', '행정,정치외교,경제', 'SOC'),
  (6, '자연과학관', 126.634600, 37.375600, '자연과학대 행정실', '032-000-0006', 'https://example.com/buildings/6', '수학,물리,화학', 'SCI'),
  (7, '기숙사', 126.634700, 37.375700, '생활관 행정실', '032-000-0007', 'https://example.com/buildings/7', '생활관,편의시설', 'DOM'),
  (8, '체육관', 126.634800, 37.375800, '체육지원팀', '032-000-0008', 'https://example.com/buildings/8', '체육시설,헬스장', 'GYM'),
  (9, '본부관', 126.634900, 37.375900, '총무팀', '032-000-0009', 'https://example.com/buildings/9', '행정,총무', 'ADM'),
  (10, '복지회관', 126.635000, 37.376000, '복지지원팀', '032-000-0010', 'https://example.com/buildings/10', '식당,편의점,카페', 'WEL'),
  (11, '미래관', 126.635100, 37.376100, '미래융합대 행정실', '032-000-0011', 'https://example.com/buildings/11', 'AI,데이터사이언스', 'FUT'),
  (12, '예술체육관', 126.635200, 37.376200, '예체능대 행정실', '032-000-0012', 'https://example.com/buildings/12', '디자인,공연예술', 'ART');

-- floor images
INSERT INTO floorpic (floorid, floor, src, building_id) VALUES
  (101, 1, 'https://example.com/floor/1-1.png', 1),
  (102, 2, 'https://example.com/floor/1-2.png', 1),
  (201, 1, 'https://example.com/floor/2-1.png', 2),
  (301, 1, 'https://example.com/floor/3-1.png', 3),
  (701, 1, 'https://example.com/floor/7-1.png', 7);

-- users: 40
INSERT INTO user (email, password, create_date, update_date, nickname, role, profile_image, reported_count)
WITH RECURSIVE seq AS (
  SELECT 1 AS n
  UNION ALL
  SELECT n + 1 FROM seq WHERE n < 40
)
SELECT
  CONCAT('seed-user', LPAD(n, 2, '0'), '@inu.ac.kr'),
  '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi4u6M7V6czS4QhIwTZPIYOvToNiMQa',
  TIMESTAMP('2026-03-01 09:00:00') + INTERVAL n DAY,
  TIMESTAMP('2026-03-01 09:00:00') + INTERVAL n DAY,
  CONCAT('테스터', LPAD(n, 2, '0')),
  'ROLE_USER',
  CONCAT('https://picsum.photos/seed/user', n, '/200/200'),
  0
FROM seq;

-- posts: 15,000
INSERT INTO post (title, content, like_count, comment_count, create_date, update_date, user_id, building_id)
WITH RECURSIVE seq AS (
  SELECT 1 AS n
  UNION ALL
  SELECT n + 1 FROM seq WHERE n < 15000
)
SELECT
  CONCAT(
    CASE
      WHEN MOD(n, 5) = 0 THEN '학식'
      WHEN MOD(n, 5) = 1 THEN '도서관'
      WHEN MOD(n, 5) = 2 THEN '기숙사'
      WHEN MOD(n, 5) = 3 THEN '시험'
      ELSE '셔틀'
    END,
    ' 정보 공유 #', n
  ),
  CONCAT(
    '교내 커뮤니티 성능 테스트용 더미 게시글 ',
    n,
    '. ',
    CASE
      WHEN MOD(n, 5) = 0 THEN '학식 메뉴 후기와 가격 정보를 정리했습니다.'
      WHEN MOD(n, 5) = 1 THEN '도서관 좌석, 열람실 분위기, 시험기간 팁을 공유합니다.'
      WHEN MOD(n, 5) = 2 THEN '기숙사 생활 팁과 택배, 세탁실, 통금 관련 정보를 모았습니다.'
      WHEN MOD(n, 5) = 3 THEN '중간고사, 기말고사, 전공 시험 대비 팁을 정리했습니다.'
      ELSE '셔틀버스 시간, 배차, 탑승 위치 관련 경험을 남깁니다.'
    END
  ),
  MOD(n * 7, 91),
  MOD(n * 11, 37),
  TIMESTAMP('2026-01-01 08:00:00') + INTERVAL n MINUTE,
  CASE
    WHEN MOD(n, 4) = 0 THEN TIMESTAMP('2026-01-01 08:00:00') + INTERVAL (n + 30) MINUTE
    ELSE NULL
  END,
  ((n - 1) MOD 40) + 1,
  CASE
    WHEN MOD(n, 10) IN (0, 1, 2) THEN 1
    WHEN MOD(n, 10) IN (3, 4, 5) THEN 3
    WHEN MOD(n, 10) IN (6, 7) THEN 7
    WHEN MOD(n, 10) = 8 THEN 10
    ELSE 11
  END
FROM seq;

-- images: about 1/3 of posts
INSERT INTO image (post_id, image)
SELECT
  p.post_id,
  CONCAT('https://picsum.photos/seed/post', p.post_id, '/640/360')
FROM post p
WHERE MOD(p.post_id, 3) = 0;

-- post_list refresh
INSERT INTO post_list (
  post_id,
  user_id,
  title,
  content,
  like_count,
  comment_count,
  write_datetime,
  writer_nickname,
  writer_profile_image,
  post_title_image,
  building_id
)
SELECT
  p.post_id,
  p.user_id,
  p.title,
  p.content,
  p.like_count,
  p.comment_count,
  COALESCE(p.update_date, p.create_date),
  u.nickname,
  u.profile_image,
  i.image,
  p.building_id
FROM post p
JOIN user u ON u.user_id = p.user_id
LEFT JOIN image i ON i.post_id = p.post_id
WHERE i.image_id IS NULL
   OR i.image_id = (
     SELECT MIN(i2.image_id)
     FROM image i2
     WHERE i2.post_id = p.post_id
   );
