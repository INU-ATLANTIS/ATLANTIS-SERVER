-- =========================================================
-- ATLANTIS-SERVER 페이징 성능 측정용 더미 데이터 시드
-- =========================================================
-- 이 파일은 dev 환경에서 스프링부트를 실행할 때 자동으로 실행됩니다.
--
-- 목적:
-- 1) /api/v1/post/latest-list
-- 2) /api/v1/post/search-list/{searchword}
-- 3) /api/v1/post/building/{buildingid}
-- 위 3개 API의 페이징 적용 전/후 성능을 JMeter로 비교하기 위한 테스트 데이터를 생성합니다.
--
-- 이 파일을 실행하면 어떻게 되나?
-- - 기존 로컬 데이터가 삭제됩니다.
-- - 같은 규칙의 더미 데이터가 다시 생성됩니다.
-- - 즉, "항상 비슷한 조건으로 Before / After 비교"를 하기 위한 파일입니다.
--
-- 생성되는 데이터 양:
-- - 건물 12개
-- - 층 이미지 5개
-- - 사용자 40명
-- - 게시글 15,000개
-- - 게시글 대표 이미지 약 1/3 (대략 5,000개)
-- - postList 목록 테이블(또는 뷰 대체용 테이블) 15,000개
--
-- 데이터 분포 특징:
-- - 검색 키워드: 학식 / 도서관 / 기숙사 / 시험 / 셔틀
-- - 특정 건물(1, 3, 7번)에 게시글이 많이 몰리도록 구성
-- - building API와 search API에서 페이징 효과가 보이도록 의도한 분포
--
-- 참고:
-- - SQL을 잘 몰라도 "어떤 데이터를 몇 개 만드는지" 이해할 수 있도록
--   각 구간마다 설명 주석을 최대한 자세히 넣었습니다.
-- =========================================================

-- MySQL 재귀 CTE 기본 제한(1000)을 늘려
-- 15,000건 더미 데이터를 한 번에 생성할 수 있게 설정합니다.
SET SESSION cte_max_recursion_depth = 20000;

-- ---------------------------------------------------------
-- -1. post_list 테이블 보장
-- ---------------------------------------------------------
-- 로컬 DB에 post_list가 아직 없으면 앱 시작 전에 시드가 실패할 수 있습니다.
-- 그래서 목록 API에서 사용하는 최소 구조를 먼저 보장합니다.
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

-- ---------------------------------------------------------
-- 0. 기존 로컬 데이터 삭제
-- ---------------------------------------------------------
-- 성능 측정은 같은 조건에서 여러 번 비교하는 것이 중요합니다.
-- 그래서 실행할 때마다 관련 데이터를 먼저 비우고 다시 채웁니다.
-- 주의: dev DB의 기존 테스트 데이터는 이 시드 실행 시 사라집니다.
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM image;
DELETE FROM favorite;
DELETE FROM comment;
DELETE FROM marker;
DELETE FROM notification;
DELETE FROM post_list;
DELETE FROM post;
DELETE FROM certification;
DELETE FROM report;
DELETE FROM floorpic;
DELETE FROM building;
DELETE FROM user;

SET FOREIGN_KEY_CHECKS = 1;

-- AUTO_INCREMENT를 초기화해서
-- user_id 1~40, post_id 1~15000처럼 예측 가능한 ID가 다시 생성되게 맞춥니다.
ALTER TABLE user AUTO_INCREMENT = 1;
ALTER TABLE post AUTO_INCREMENT = 1;
ALTER TABLE image AUTO_INCREMENT = 1;
ALTER TABLE marker AUTO_INCREMENT = 1;
ALTER TABLE notification AUTO_INCREMENT = 1;
ALTER TABLE comment AUTO_INCREMENT = 1;

-- ---------------------------------------------------------
-- 1. 건물 데이터 생성
-- ---------------------------------------------------------
-- 총 12개 건물을 넣습니다.
-- building/{buildingid} API 측정 시 게시글이 어느 건물에 속하는지 판단하는 기준 데이터입니다.
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

-- ---------------------------------------------------------
-- 2. 층 이미지 데이터 생성
-- ---------------------------------------------------------
-- building 쪽 API가 빈 데이터가 되지 않도록 최소한의 floorpic 데이터를 함께 넣습니다.
INSERT INTO floorpic (floorid, floor, src, building_id) VALUES
  (101, 1, 'https://example.com/floor/1-1.png', 1),
  (102, 2, 'https://example.com/floor/1-2.png', 1),
  (201, 1, 'https://example.com/floor/2-1.png', 2),
  (301, 1, 'https://example.com/floor/3-1.png', 3),
  (701, 1, 'https://example.com/floor/7-1.png', 7);

-- ---------------------------------------------------------
-- 3. 사용자 40명 생성
-- ---------------------------------------------------------
-- 게시글 작성자 데이터를 만들기 위한 더미 사용자입니다.
-- user1, user2 같은 단순 패턴이 아니라 실제 이메일/닉네임처럼 보이게 생성합니다.
-- 비밀번호는 성능 측정용이라 실제 로그인 목적보다는 "형식 유지"에 초점이 있습니다.
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

-- ---------------------------------------------------------
-- 4. 게시글 15,000개 생성
-- ---------------------------------------------------------
-- 이 시드 파일의 핵심 데이터입니다.
--
-- 왜 15,000개인가?
-- - 로컬 MySQL에서도 무리하지 않으면서 성능 비교 효과를 보기 좋은 절충안입니다.
-- - latest-list, search-list, building API 모두에서 조회 부담이 더 분명해집니다.
--
-- 제목/본문 키워드 분포:
-- - 학식, 도서관, 기숙사, 시험, 셔틀
-- - 5개 키워드가 반복되므로 검색 API 테스트가 쉬워집니다.
--
-- 건물 분포:
-- - 1번, 3번, 7번 건물에 글이 많이 몰리도록 설계
-- - building API에서 페이징 차이가 보이게 하기 위한 의도적 편중입니다.
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

-- ---------------------------------------------------------
-- 5. 게시글 대표 이미지 생성
-- ---------------------------------------------------------
-- 모든 글에 이미지를 넣지는 않고, 3개 중 1개 정도의 게시글에만 이미지를 붙입니다.
-- 실제 서비스처럼 "이미지 있는 글 / 없는 글"이 섞이도록 하기 위한 설정입니다.
INSERT INTO image (post_id, image)
SELECT
  p.post_id,
  CONCAT('https://picsum.photos/seed/post', p.post_id, '/640/360')
FROM post p
WHERE MOD(p.post_id, 3) = 0;

-- ---------------------------------------------------------
-- 6. post_list 목록용 데이터 생성
-- ---------------------------------------------------------
-- 현재 목록 API는 post_list 테이블(또는 뷰 성격의 구조)을 기준으로 조회합니다.
-- 그래서 post만 넣는 것이 아니라, 목록 조회에서 바로 사용되는 post_list도 함께 채웁니다.
--
-- 여기에는 다음 정보가 들어갑니다:
-- - 게시글 제목/본문
-- - 좋아요 수, 댓글 수
-- - 최신 작성 시각(writeDatetime)
-- - 작성자 닉네임/프로필 이미지
-- - 대표 이미지 1장
-- - 건물 ID
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
