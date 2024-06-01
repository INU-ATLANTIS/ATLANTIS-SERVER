# ATLANTIS-SERVER
![Frame 31](https://github.com/DO-SOPT-ANDROID/chaeeun-park/assets/107169027/fcab55b8-272f-4a06-86a9-6163561e612f)
## TEAM ATLANTIS 졸업작품 (2023.09 ~ 2024.05)

위치 기반 **맞춤형 알림**을 제공해요! 캠퍼스 내의 다양한 장소와 이벤트에 대한 **정보를 손쉽게 공유**해요
<br><br>

"commINUty"는 팀 아틀란티스의 졸업작품으로, 현대적인 캠퍼스 생활을 위한 혁신적인 솔루션을 제공하는 앱입니다. 이 앱은 학생들과 교직원들이 캠퍼스 내의 다양한 위치와 이벤트에 쉽게 접근하고 상호작용할 수 있도록 설계되었습니다. 위치 기반 기술을 활용하여 사용자에게 맞춤형 알림을 제공하고, 캠퍼스 내의 다양한 장소와 이벤트에 대한 정보를 손쉽게 공유할 수 있게 해줍니다.

"commINUty"는 학생들의 캠퍼스 경험을 개선하고, 교육 공동체 간의 소통과 협력을 증진시키는 데 중점을 두고 있습니다. 학생들이 자신의 일정과 위치에 맞춰 필요한 정보를 즉시 받을 수 있게 함으로써, 더욱 효율적이고 연결된 캠퍼스 생활을 지원합니다. 이 앱은 캠퍼스 내의 소셜 네트워킹, 정보 공유, 개인화된 경험을 통해 대학 생활을 한층 더 풍부하고 편리하게 만들어줄 것입니다.

<br><br>

## 위치 기반 알림(지오펜싱)
설정한 위치에 근접할 시 알람

## 게시글
위치를 설정하지 않아도 게시글 작성 가능

## 추천 기능 
추천수 기반으로 위치 정보가 설정된 게시글과 마커 노출 결정

## 사용자 마커 생성
특정한 위치를 설정하고 게시글을 작성하면 마커 생성 가능

## 건물 정보 및 평면도
강의실 검색을 통한 해당 층 평면도 확인


<br><br>


<br>

## 🍎 Server Developer

| [채은](https://github.com/CHANGEL1004) | [은진](https://github.com/PanicAthe) 
| :--: | :--: |
| <img width="600" alt="채은" src="https://github.com/DO-SOPT-ANDROID/chaeeun-park/assets/107169027/eb40b648-ee0e-47a5-8ecf-8ced80fe21c8"> | <img width="600" alt="은진" src="https://github.com/DO-SOPT-ANDROID/chaeeun-park/assets/107169027/dc67422e-a29c-41c4-bc22-b7dc7413b331"> | 

<br>

## 📚 Tech Stack
| Category | Used |
| --- | --- |
| Java version | Java 17 |
| Spring version | 3.2.2 |
| Cloud Computing |	AWS EC2 (Ubuntu 22.04 LTS) |
| Database | AWS RDS (MySQL 8.0.35) |
| File Upload | AWS S3 |
| MessageQueue | AWS SQS |
| Notification | Firebase Cloud Messaging |
| API Docs | Notion |

<br>

## 📦 ERD
<img width="709" alt="스크린샷 2024-03-13 오후 9 11 43" src="https://github.com/INU-ATLANTIS/ATLANTIS-SERVER/assets/137266460/103edf67-23d1-4cb7-96fc-6f8b6e771242">

## 프로젝트 폴더 구조

- **java/**  소스 코드 루트 디렉토리
    - **com/**
        - **atl/**
            - **map/**  주요 애플리케이션 패키지
                - **common/**  공통 유틸리티 클래스
                - **config/**  설정 클래스
                - **controller/**  컨트롤러 클래스
                - **dto/**  데이터 전송 객체
                    - **object/**  객체 DTO
                    - **request/**  요청 DTO
                        - **Noti/**
                        - **auth/**
                        - **marker**
                        - **post**
                        - **user**
                    - **response/**  응답 DTO
                        - **marker**
                        - **noti**
                        - **post**
                        - **user**
                - **entity/**  엔티티 클래스
                    - **primaryKey/**  복합 키 클래스
                - **filter/**  필터 클래스
                - **handler/**  예외 처리 핸들러
                - **provider/**  제공자 클래스 (예: 이메일, JWT)
                - **repository/**  리포지토리 인터페이스
                    - **resultSet/**  결과 셋 클래스
                - **service/**  서비스 클래스
                    - **implement/**  서비스 구현 클래스

<br><br>

