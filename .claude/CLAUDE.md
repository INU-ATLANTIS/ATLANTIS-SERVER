# 프로젝트 개요

**commINUty** — 인천대학교 캠퍼스 위치 기반 알림 및 커뮤니티 앱 서버
- 팀: ATLANTIS (졸업작품, 2023.09 ~ 2024.05)
- 레포: https://github.com/INU-ATLANTIS/ATLANTIS-SERVER

## 기술 스택

- **Language**: Java 17
- **Framework**: Spring Boot 3.2.2
- **Security**: Spring Security + JWT (jjwt 0.11.2)
- **ORM**: Spring Data JPA (Hibernate, MySQL)
- **Cloud**: AWS EC2 / RDS (MySQL 8.0) / S3 / SQS
- **Push**: Firebase Cloud Messaging (FCM)
- **API Docs**: Swagger (SpringDoc OpenAPI 3 `2.3.0`)
- **Build**: Gradle

## 패키지 구조

```
com.atl.map
├── common/       # CertificationNumber, ResponseCode, ResponseMessage
├── config/       # WebSecurityConfig
├── controller/   # Auth, Marker, Post, User, Noti, File
├── dto/          # request / response / object
├── entity/       # JPA 엔티티 (primaryKey 포함)
├── filter/       # JwtAuthenticationFilter
├── handler/      # ValidationExceptionHandler
├── provider/     # JwtProvider, EmailProvider
├── repository/   # JPA 레포지토리 (resultSet 포함)
└── service/      # 인터페이스 + implement/
```

## API 엔드포인트

| 도메인 | Base URL |
|--------|----------|
| 인증 | `api/v1/auth` |
| 마커 | `api/v1/marker` |
| 게시글 | `api/v1/post` |
| 사용자 | `api/v1/user` |
| 알림 | `api/v1/noti` |
| 파일 | `api/v1/file` |

Swagger: `http://localhost:8080/swagger-ui/index.html`

## 환경변수

`src/main/resources/application.yml` (gitignore 처리됨)
→ `application.yml.example` 참고하여 생성

---

# Skills

| 명령어 | 설명 |
|--------|------|
| `/commit` | 변경사항 확인 후 사용자 승인 받고 커밋 |
| `/review` | 코드 리뷰 (Critical / Warning / Suggestion) |
| `/pr` | PR 생성 또는 기존 PR 분석 |
| `/debug` | 에러/버그 원인 분석 및 수정 |
| `/security` | 보안 취약점 점검 |
| `/test` | 테스트 코드 생성 |
| `/refactor` | 안전한 코드 리팩토링 |
| `/explain` | 코드/개념 설명 |
| `/perf` | 성능 이슈 탐지 및 개선 |
