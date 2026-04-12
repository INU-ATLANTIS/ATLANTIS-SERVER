# ATLANTIS-SERVER

![Frame 31](https://github.com/DO-SOPT-ANDROID/chaeeun-park/assets/107169027/fcab55b8-272f-4a06-86a9-6163561e612f)

## TEAM ATLANTIS 졸업작품 (2023.09 ~ 2024.05)

위치 기반 **맞춤형 알림**을 제공해요! 캠퍼스 내의 다양한 장소와 이벤트에 대한 **정보를 손쉽게 공유**해요

"commINUty"는 팀 아틀란티스의 졸업작품으로, 현대적인 캠퍼스 생활을 위한 혁신적인 솔루션을 제공하는 앱입니다. 위치 기반 기술을 활용하여 사용자에게 맞춤형 알림을 제공하고, 캠퍼스 내의 다양한 장소와 이벤트에 대한 정보를 손쉽게 공유할 수 있게 해줍니다.

<br>

## 🍎 Server Developer

| [채은](https://github.com/CHANGEL1004) | [은진](https://github.com/PanicAthe) |
| :--: | :--: |
| <img width="600" alt="채은" src="https://github.com/DO-SOPT-ANDROID/chaeeun-park/assets/107169027/eb40b648-ee0e-47a5-8ecf-8ced80fe21c8"> | <img width="600" alt="은진" src="https://github.com/DO-SOPT-ANDROID/chaeeun-park/assets/107169027/dc67422e-a29c-41c4-bc22-b7dc7413b331"> |

<br>

## 📚 Tech Stack

| Category | Used |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 3.2.2 |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA (Hibernate) |
| Database | AWS RDS (MySQL 8.0), Redis |
| Cloud | AWS EC2 (Ubuntu 22.04 LTS) |
| API Docs | Swagger (SpringDoc OpenAPI 3) |
| Build Tool | Gradle |

<br>

## ✨ 주요 기능

### 위치 기반 알림 (지오펜싱)
설정한 위치에 근접할 시 FCM 푸시 알림 발송

### 게시글
위치 설정 없이도 게시글 작성 가능, 댓글 및 대댓글 지원

### 추천 기능
추천수 기반으로 인기 게시글 및 마커 노출

### 사용자 마커 생성
특정 위치를 지정하고 게시글을 작성하면 지도 마커 생성

### 건물 정보 및 평면도
강의실 검색을 통한 해당 층 평면도 확인

<br>

## 🔌 API 구조

| 도메인 | Base URL | 주요 기능 |
| --- | --- | --- |
| 인증 | `api/v1/auth` | 이메일 중복 확인, 이메일 인증, 회원가입, 로그인, 회원탈퇴, 비밀번호 변경 |
| 마커 | `api/v1/marker` | 마커 생성/조회/수정/삭제, 건물 검색, 인기 마커, 내 마커 |
| 게시글 | `api/v1/post` | 게시글 CRUD, 댓글/대댓글, 좋아요, 검색 |
| 사용자 | `api/v1/user` | 프로필 조회/수정, 닉네임 변경, 신고 |
| 알림 | `api/v1/noti` | 알림 생성/조회/수정/삭제 |
| 파일 | `api/v1/file` | 이미지 업로드 |

<br>

## 📦 ERD

<img width="709" alt="ERD" src="https://github.com/INU-ATLANTIS/ATLANTIS-SERVER/assets/137266460/103edf67-23d1-4cb7-96fc-6f8b6e771242">

<br>

## 🗂️ 프로젝트 구조

```
src/main/java/com/atl/map/
├── common/          # 공통 유틸 (인증번호, 응답코드/메시지)
├── config/          # Spring Security 설정
├── controller/      # REST 컨트롤러 (Auth, Marker, Post, User, Noti, File)
├── dto/
│   ├── object/      # 공통 객체 DTO
│   ├── request/     # 요청 DTO (auth, marker, post, user, noti)
│   └── response/    # 응답 DTO (auth, marker, post, user, noti)
├── entity/          # JPA 엔티티
│   └── primaryKey/  # 복합 키
├── filter/          # JWT 인증 필터
├── handler/         # 전역 예외 처리
├── provider/        # JWT, Email 프로바이더
├── repository/      # JPA 레포지토리
│   └── resultSet/   # 커스텀 쿼리 결과 인터페이스
└── service/
    └── implement/   # 서비스 구현체
```

## ⚙️ 로컬 실행 방법

1. `src/main/resources/application.yml.example`을 복사해 `application.yml` 생성
2. DB, JWT, Gmail, 파일 경로 등 환경값을 채운다
3. Redis 기능 확인이 필요하다면 로컬 Redis를 실행한다
4. 애플리케이션 실행 후 `http://localhost:8080/swagger-ui/index.html` 에서 API 확인

## 🐳 Docker 실행 방법

`docker-compose.yml` 기준으로 앱과 Redis를 컨테이너로 실행하고, DB는 기존 RDS에 연결할 수 있습니다.

1. `docker-compose.yml`의 아래 환경값을 실제 값으로 수정
   - `DB_HOST`
   - `DB_USERNAME`
   - `DB_PASSWORD`
   - `SECRET_KEY`
   - `SPRING_MAIL_USERNAME`
   - `SPRING_MAIL_PASSWORD`
   - 필요 시 `CORS_ALLOWED_ORIGINS`
2. 아래 명령으로 빌드 및 실행

```bash
docker compose up --build
```

3. 실행 후 확인
   - API: `http://localhost:8080`
   - Swagger: `http://localhost:8080/swagger-ui/index.html`

즉, Docker에서는 애플리케이션과 Redis를 띄우고, MySQL은 기존 RDS를 그대로 사용하는 방식입니다.

중지:

```bash
docker compose down
```

### 성능 측정용 SQL

- 페이징용 기본 시드: `src/main/resources/db/seed/01-pagination-perf-seed.sql`
- 인덱스 튜닝용 추가 시드: `src/main/resources/db/seed/02-index-tuning-perf-seed.sql`
- 인덱스 후보 SQL: `src/main/resources/db/index/01-index-tuning-candidates.sql`
