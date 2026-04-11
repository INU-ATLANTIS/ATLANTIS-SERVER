# 파일 처리 보안 강화 및 수정 일관성 보완 - 2026-04-11

---

## 1. 문제 (Problem)

**무엇을 하려 했는가?**
> 파일 업로드/조회 API를 더 안전하게 만들고, 게시글 수정/알림 수정 로직에서 남아 있던 일관성 문제를 함께 정리하려 했다.

**어떤 에러/막힘이 있었는가?**
> 코드 리뷰 과정에서 세 가지 문제가 확인됐다.
> - `patchPost()`에서 기존 이미지를 `deleteById(postId)`로 삭제하고 있어, 게시글 기준 이미지 교체가 올바르게 동작하지 않을 수 있었다.
> - 파일 업로드/조회가 파일명과 경로를 충분히 검증하지 않아 예외 가능성과 path traversal 위험이 남아 있었다.
> - 알림 생성 시에는 `markerId` 존재 여부를 검사하지만, 알림 수정 시에는 같은 검사가 빠져 있어 생성/수정 규칙이 일관되지 않았다.

---

## 2. 가설 (Hypothesis)

**왜 이 문제가 발생했다고 생각했는가?**
> 초기 구현은 기능 완성을 우선하다 보니 "동작만 되면 된다"는 관점으로 작성된 부분이 남아 있었다.
> 하지만 운영 관점에서는 파일 처리와 수정 API가 가장 작은 틈으로도 장애나 보안 리스크로 이어질 수 있다.
> 따라서 이번 단계에서는 기능 추가보다, 기존 API의 안정성과 규칙 일관성을 먼저 보완하는 것이 맞다고 판단했다.

---

## 3. 시도 (Attempt)

**시도 1: 게시글 수정 로직 최소 수정**
> `patchPost()`는 본문 수정과 이미지 교체를 함께 수행하는데 트랜잭션이 없어 중간 실패 시 일부만 반영될 수 있었다.
> 또한 기존 이미지를 이미지 PK 기준으로 삭제하고 있어 게시글 기준 교체라는 의도와 맞지 않았다.
> 그래서 `@Transactional`을 추가하고, 이미지 삭제를 `deleteByPostId(postId)`로 바꿨다.

**시도 2: 파일 처리 하드닝**
> 업로드 시에는 빈 파일 여부만 보던 구조를 보강해,
> - 파일명 존재 여부
> - 허용 content type (`image/jpeg`, `image/png`)
> - 확장자 추출 가능 여부
> - 저장 경로 정규화
> 를 확인하도록 변경했다.
>
> 조회 시에도 전달받은 파일명을 그대로 경로에 붙이지 않고,
> - 파일명 정규화
> - base path 이탈 여부
> - 파일 존재/가독성
> 를 검사하도록 바꿨다.

**시도 3: 알림 수정 시 markerId 검증 보강**
> `createNotification()`에는 있던 `markerId` 존재 검증이 `patchNotification()`에는 없었다.
> 이번에는 생성/수정 모두 같은 유효성 규칙을 따르도록 수정 시에도 `markerRepository.existsById()` 검사를 추가했다.

**시도 4: 더 큰 대안 검토**
> 아래 대안도 생각할 수 있었다.
>
> - **파일 저장소 분리 (S3 등 외부 스토리지)**
>   - 장점: 정적 파일 서빙, 접근 제어, 확장성 측면에서 유리
>   - 단점: 현재 단계에서는 인프라 변경 범위가 너무 큼
>
> - **파일 메타데이터 테이블 분리 및 MIME 검사 고도화**
>   - 장점: 업로드 정책과 감사 로그를 더 정교하게 관리 가능
>   - 단점: 지금 필요한 건 구조 확장보다 우선 현재 취약 지점 차단
>
> - **알림/게시글 수정 플로우 전면 재설계**
>   - 장점: API 일관성 관점에서는 더 깔끔
>   - 단점: 이번 PR 범위를 넘고, 수정해야 할 코드가 커짐
>
> 이번 단계에서는 "작은 수정으로 실제 위험을 줄이는 것"에 집중해, 기존 구조를 유지하면서 안전장치만 먼저 추가했다.

---

## 4. 해결 (Solution)

**최종 해결 방법:**
> 파일 처리 보안 강화와 수정 일관성 보완은 아래 세 가지를 중심으로 정리했다.
> - `patchPost()` 트랜잭션 추가 및 이미지 삭제 기준 수정
> - 파일 업로드/조회 경로 검증 및 이미지 타입 제한
> - 알림 수정 시 `markerId` 유효성 검사 추가

```java
// PostServiceImplement
@Transactional
public ResponseEntity<? super PatchPostResponseDto> patchPost(...) {
    ...
    if (dto.getImageList() != null) {
        imageRepository.deleteByPostId(postId);
        imageRepository.saveAll(imageEntities);
    }
}
```

```java
// FileServiceImplement - upload
if (!StringUtils.hasText(originalFileName)) return null;
if (!isAllowedContentType(file.getContentType())) return null;

Path basePath = Path.of(filePath).toAbsolutePath().normalize();
Path savePath = basePath.resolve(saveFileName).normalize();

if (!savePath.startsWith(basePath)) return null;
```

```java
// FileServiceImplement - getImage
Path requestedPath = basePath.resolve(filename).normalize();
String normalizedFileName = Path.of(filename).getFileName().toString();

if (!normalizedFileName.equals(filename) || !requestedPath.startsWith(basePath)) {
    return null;
}
```

```java
// NotiServiceImplement
if (dto.getMarkerId() != null && !markerRepository.existsById(dto.getMarkerId())) {
    throw new BusinessException(ErrorCode.MARKER_NOT_FOUND);
}
```

**적용 효과**
> - 게시글 수정 시 본문과 이미지 교체가 하나의 트랜잭션으로 묶임
> - 기존 이미지 삭제가 게시글 기준으로 정확히 동작함
> - 파일 업로드/조회에서 비정상 파일명과 경로 이탈 요청을 1차 차단
> - 알림 생성/수정의 validation 규칙이 일관되게 맞춰짐

---

## 5. 핵심 개념 (Key Insight)

**이 문제에서 배운 개념:**
> - 수정 API는 "저장"만 성공한다고 끝나는 것이 아니라, 관련 리소스가 함께 일관되게 반영되어야 한다.
> - 파일 처리 코드는 비즈니스 로직보다 먼저 경로 정규화와 입력 검증을 의심해야 한다.
> - 생성 API에 있던 검증이 수정 API에 빠지는 경우가 많아서, create/patch validation parity를 점검하는 습관이 중요하다.
> - 작은 버그 하나(`deleteById(postId)`)도 데이터 정합성을 깨뜨릴 수 있으므로, 메서드 이름과 엔티티 키 의미를 정확히 맞춰야 한다.

**다른 대안은 없었는가?**
> 있었다.
>
> - **S3 등 외부 스토리지로 파일 분리**
>   - 장기적으로는 더 적절할 수 있다.
>   - 하지만 지금은 파일 API 자체의 입력 검증과 경로 안정성 확보가 우선이었다.
>
> - **파일 API 응답 체계 전면 수정**
>   - 현재는 `null` 반환 기반이라, 장기적으로는 `ResponseEntity<ResponseDto>` 기반으로 맞추는 것이 더 좋다.
>   - 다만 이번 단계에서는 API 계약 전체 변경보다 보안/정합성 리스크 제거를 먼저 했다.
>
> - **알림/게시글 수정 흐름 전체 재설계**
>   - 더 큰 리팩토링으로 갈 수도 있었지만, 이번 범위에서는 최소 수정으로 실질적인 문제를 해결하는 방향을 택했다.

**언제 다시 써먹을 수 있는가?**
> 로컬 파일 업로드가 있는 Spring Boot 프로젝트, 그리고 생성/수정 API가 공존하는 서비스에서 "파일 보안 + 수정 일관성" 점검 항목으로 그대로 재사용할 수 있다.

---

## 6. 리팩토링/개선 (Optional)

**더 좋은 방법:**
> 현재 파일 API는 실패 시 `null`을 반환하므로, 추후에는 업로드/조회 실패도 표준 응답 DTO와 예외 체계로 통합하는 것이 좋다.
> 또한 장기적으로는 로컬 파일 저장 대신 S3 같은 외부 스토리지를 사용하고, MIME sniffing이나 파일 크기/확장자 정책까지 더 세밀하게 관리하면 보안 완성도가 올라간다.
