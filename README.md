# ATLANTIS-SERVER
![Frame 31](https://github.com/DO-SOPT-ANDROID/chaeeun-park/assets/107169027/fcab55b8-272f-4a06-86a9-6163561e612f)

위치 기반 **맞춤형 알림**을 제공해요! 캠퍼스 내의 다양한 장소와 이벤트에 대한 **정보를 손쉽게 공유**해요

<br><br>

"commINUty"는 팀 아틀란티스의 졸업작품으로, 현대적인 캠퍼스 생활을 위한 혁신적인 솔루션을 제공하는 앱입니다. 이 앱은 학생들과 교직원들이 캠퍼스 내의 다양한 위치와 이벤트에 쉽게 접근하고 상호작용할 수 있도록 설계되었습니다. 위치 기반 기술을 활용하여 사용자에게 맞춤형 알림을 제공하고, 캠퍼스 내의 다양한 장소와 이벤트에 대한 정보를 손쉽게 공유할 수 있게 해줍니다.

"commINUty"는 학생들의 캠퍼스 경험을 개선하고, 교육 공동체 간의 소통과 협력을 증진시키는 데 중점을 두고 있습니다. 학생들이 자신의 일정과 위치에 맞춰 필요한 정보를 즉시 받을 수 있게 함으로써, 더욱 효율적이고 연결된 캠퍼스 생활을 지원합니다. 이 앱은 캠퍼스 내의 소셜 네트워킹, 정보 공유, 개인화된 경험을 통해 대학 생활을 한층 더 풍부하고 편리하게 만들어줄 것입니다.

<br><br>

## 위치 기반 알림(지오펜싱)
설정한 위치에 근접할 시 알람
위치 뿐만 아니라 특정 날짜와 시간도 설정
권한을 가진 사용자는 지정한 위치에 근접한 불특정 유저들에게 알림 전송 가능
## 게시글
위치를 설정하지 않아도 게시글 작성 가능
## 추천수와 스크랩 기능 ( 추천수 기반으로 위치 정보가 설정된 게시글을 마커 노출 결정)
## 게시글 검색
## 사용자 마커 생성
특정한 위치를 설정하고 게시글을 작성하면 마커 생성 가능
권한을 가진 사용자는 모든 유저의 캠퍼스 맵에 생성한 마커 강제 노출
## 건물 정보 및 평면도
강의실 검색을 통한 해당 층 평면도 확인
사용자가 설정한 시간표 기반으로 강의실 즐겨찾기

<br><br>

- TEAM ATLANTIS 졸업작품 (2023.09 ~ )

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
<img width="709" alt="스크린샷 2024-03-13 오후 9 11 43" src="https://github.com/DO-SOPT-ANDROID/chaeeun-park/assets/107169027/deb200f2-7d6b-4997-8c7f-8808c2e18a3c">

<br><br>

## 🧬 Architecture
<img width="722" alt="스크린샷 2024-03-13 오후 9 13 39" src="https://github.com/DO-SOPT-ANDROID/chaeeun-park/assets/107169027/fe685959-e656-4597-a376-de518f6e4aaa">

<br><br>

## 📂 폴더 구조도
```
├── 📂.github
├── 📂 main
	├── 📂 java
		├── 📂 com.app.toaster
			├── 📂 controller(컨트롤러 파일)
				├── 🗂️ dto
					 ├──🗂️ request
					 ├──🗂️ response
					 ├──🗂️ valid(valid custom어노테이션 관리 폴더)

			├── 📂 domain(엔티티 파일)
			
			├── 📂 infrastructure(레포지토리 폴더)
			
			├── 📂 service(서비스 파일)
				├── 🗂️ auth
				├── 🗂️ parse
				├── 🗂️ search
				├── 🗂️ toast
				├── 🗂️ timer
			
			├── 📂 exception(Exception enum, Exception class 파일)
				├── 🗂️ model
			
			├── 📂 external(서비스 파일)
				├── 🗂️ slack
				├── 🗂️ client.aws
					├── 🗂️ AWSConfig
					├── 🗂️ S3Service
			
			├── 📂 common(공용 클래스 관리)
				├──🗂️ advice
				├──🗂️ dto	
			
			├── 📂 config(공용 클래스 설정 관리)
				├──🗂️ user
				├──🗂️ jwt			

		├── 🗂️ resources
			├── 📕 application.yml
```
<br><br>

## 🤝 Code Convention
### ✓ File Naming
- 파일 이름 및 클래스, 인터페이스 이름: **파스칼 케이스(Pascal Case)**
- Entity에서 사용되는 속성값들은 ? **카멜 케이스(camel Case)**
- 내부에서 사용되는 함수 및 기타 사용: **카멜 케이스(camelCase)**

### ✓ 인터페이스 이름에 명사/형용사 사용 [interface-noun-adj]
인터페이스(interface)의 이름은 명사/명사절로 혹은 형용사/형용사절로 짓는다.

### ✓ 클래스 이름에 명사 사용 [class-noun]

클래스 이름은 명사나 명사절로 짓는다.

### ✓ 메서드 이름은 동사/전치사로 시작 [method-verb-preposition]

메서드명은 기본적으로 동사로 시작한다.

다른 타입으로 전환하는 메서드나 빌더 패턴을 구현한 클래스의 메서드에서는 전치사를 쓸 수 있다.

### ✓ 상수는 대문자와 언더스코어로 구성[constant_uppercase]

"static final"로 선언되어 있는 필드일 때 상수로 간주한다.

상수 이름은 대문자로 작성하며, 복합어는 언더스코어'_'를 사용하여 단어를 구분한다.

### ✓ 변수에 소문자 카멜표기법 적용 [var-lower-camelcase]

상수가 아닌 클래스의 멤버변수/지역변수/메서드 파라미터에는 소문자 카멜표기법(Lower camel case)을 사용한다.

### ✓ 임시 변수 외에는 1 글자 이름 사용 금지 [avoid-1-char-var]

메서드 블럭 범위 이상의 생명 주기를 가지는 변수에는 1글자로 된 이름을 쓰지 않는다.

**반복문의 인덱스나 람다 표현식의 파라미터 등 짧은 범위의 임시 변수**에는 관례적으로 1글자 변수명을 사용할 수 있다.
<br><br>

## 🤝 Git Convention
### Issue

모든 작업의 단위는 github에 생성된 Issue를 기준으로 합니다.

Issue의 볼륨은 최소 하나의 기능으로 합니다.

하나의 이슈를 마무리하기 전에는 특별한 상황이 아닌 이상 다른 작업에 대한 이슈를 생성하지 않습니다.

### PR (Pull Request)

Issue ≤ PR

하나의 이슈에 대해서 반드시 하나의 PR이 열려야하는 건 아닙니다.

원활한 코드리뷰와 리뷰에 대한 내용을 반영하기 위해서 PR은 3개의 commit을 넘어가지 않아야합니다.

하나의 PR에 3개 이상의 File Change는 지양합니다.

## Branch

Branch 전략은 Git-flow를 준수합니다.

[우린 Git-flow를 사용하고 있어요 | 우아한형제들 기술블로그](https://techblog.woowahan.com/2553/)

branch 이름: 관련브랜치 분류/#[Issue tracker]
 ex) feature/#1
 

### Commit
| 커밋 구분 | 설명 |
| --- | --- |
| Feature | (Feature) 개선 또는 기능 추가 |
| Bug | (Bug Fix) 버그 수정 |
| Doc | (Documentation) 문서 작업 |
| Test | (Test) 테스트 추가/수정 |
| Build | (Build) 빌드 프로세스 관련 수정(yml) |
| Performance | (Performance) 속도 개선 |
| Refactor | (Cleanup) 코드 정리/리팩토링 |

- 이슈번호와 함께 커밋 내용을 적는다.
- 예시 : [#1] feataure : ~

