# 카테고리 관리 설계 (생성 + 트리 조회)

- 날짜: 2026-07-01
- 상태: 승인됨 (구현 계획 대기)
- 관련: 상품 등록 기능의 선행 조건. 상품 등록은 별도 spec으로 이 기능 완료 후 재개.

## 배경 / 목적

판매자 상품 등록 시 카테고리를 지정하려면 카테고리가 먼저 존재해야 한다. 이 spec은
관리자가 카테고리(고정 3단계 트리)를 **생성**하고, 사용자가 **트리로 조회**하는 최소 기능을
정의한다. 프로젝트 컨벤션은 헥사고날(ports & adapters): `domain` / `application`(provided·required) / `adapter`.

## 범위

**포함**
- 카테고리 생성 (ADMIN)
- 카테고리 트리 조회 (인증 사용자)

**제외 (YAGNI)**
- 수정 / 삭제 / 비활성화 토글
- 부모 이동(재부모)
- 카테고리별 상품 목록

## 확정 결정

| 항목 | 결정 |
|------|------|
| 동작 범위 | 생성 + 트리 조회 |
| 트리 깊이 | 고정 3단계 (`level` 1=대, 2=중, 3=소) |
| slug 유니크 | **전역 유니크** (soft-delete 인지) |
| name 유니크 | 형제 내 유니크 |
| 루트 표현 | `parent_id` NULL (루트 = NULL) |
| 권한 | 생성 = ADMIN, 조회 = 인증 사용자 |
| 마이그레이션 | **V4 직접 수정** (`flyway clean` 후 재적용, 빈 테이블 전제) |
| slug 입력 | 관리자 직접 입력 (영문+하이픈; 한글명에서 자동생성 불가) |

## 스키마 변경 — V4 직접 수정

기존 V4는 세 가지 문제가 있어 수정한다:
1. `level` 컬럼 없음 → 추가
2. `slug`가 형제 내 유니크(`UNIQUE(parent_id, slug, active_flag)`)였음 → **전역 유니크로 변경**
3. 루트가 `parent_id NOT NULL DEFAULT 0` + 자기참조 FK → id=0 행이 없어 루트 INSERT 시 FK 위반(잠재 버그) → **`parent_id` NULL 허용으로 변경**

```sql
CREATE TABLE categories
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    parent_id   BIGINT       NULL,                 -- 루트 = NULL
    slug        VARCHAR(64)  NOT NULL,
    level       INT          NOT NULL,             -- 1=대, 2=중, 3=소
    is_active   BOOLEAN      NOT NULL DEFAULT TRUE,
    active_flag TINYINT(1) as (IF(deleted_at IS NULL, 1, NULL)) VIRTUAL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP    NULL     DEFAULT NULL,
    CONSTRAINT fk_parent_id FOREIGN KEY (parent_id) REFERENCES categories (id),
    CONSTRAINT uk_slug UNIQUE (slug, active_flag),            -- 전역 유니크 (soft-delete aware)
    CONSTRAINT uk_name UNIQUE (parent_id, name, active_flag), -- 형제 내 유니크
    CONSTRAINT chk_level CHECK (level BETWEEN 1 AND 3)
);
```

> `active_flag`는 `deleted_at IS NULL`일 때 1, 삭제되면 NULL이 되는 가상 컬럼. 유니크 제약에
> 포함하면 소프트 삭제된 행은 유니크 대상에서 제외된다(NULL은 서로 다르게 취급).

## 엔티티 변경 — `Category`

```java
@Column(nullable = false)
int level;                        // 신규

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "parent_id")   // nullable(기본) → 루트 = null
Category parent;

public static Category create(String name, Slug slug, Category parent) {
    // level = (parent == null) ? 1 : parent.level + 1;
    // isActive = true;
}
```

기존 필드(`name`, `slug`, `isActive`, `parent`)와 소프트 삭제/`BaseEntity`는 유지.

## 컴포넌트 (헥사고날)

| 레이어 | 타입 | 역할 |
|--------|------|------|
| inbound port | `application/category/provided/CategoryManager` | `Category register(CategoryRegisterRequest req)` + `List<Category> findAllActive()` (도메인 반환) |
| service | `application/category/CategoryService` | `implements CategoryManager`. 생성 `@Transactional`, 조회 `@Transactional(readOnly = true)` |
| outbound port | `application/category/required/CategoryRepository` | `extends JpaRepository<Category, Long>`; `existsBySlug(Slug)`, `existsByParentAndName(Category, String)`, `existsByParentIsNullAndName(String)`, `findAllByIsActiveTrue()` |
| controller | `adapter/webApi/category/CategoryApi` | `POST /api/categories`(ADMIN), `GET /api/categories`(인증) |
| mapper | `adapter/webApi/category/mapper/CategoryMapper` | `Category → CategoryResponse`, `List<Category> → List<CategoryTreeResponse>`(트리 조립) |
| response DTO | `adapter/webApi/category/dto/` | `CategoryResponse`, `CategoryTreeResponse` |

> 레이어링: `provided` 포트는 도메인 `Category`를 반환하고(=`UserRegister`가 `User` 반환하는 컨벤션),
> 응답 DTO 변환·트리 조립은 어댑터의 `CategoryMapper`가 담당(=`UserMapper` 패턴). 애플리케이션 레이어가
> webApi DTO에 의존하지 않게 한다.

## DTO

`CategoryRegisterRequest`는 현재 빈 스텁 → 아래로 채운다.

```java
public record CategoryRegisterRequest(
    @NotBlank String name,
    @NotBlank String slug,   // 영문+하이픈
    Long parentId            // null = 대분류(level 1)
) {}

public record CategoryResponse(Long id, String name, String slug, Long parentId, int level) {}

public record CategoryTreeResponse(
    Long id, String name, String slug, int level,
    List<CategoryTreeResponse> children
) {}
```

`CategoryResponse` / `CategoryTreeResponse`는 `adapter/webApi/category/dto`에 둔다(응답 전용).
`register`는 도메인 `Category`를 반환하고 컨트롤러가 `CategoryMapper.toResponse`로 변환한다.

## 생성 흐름 & 검증

1. slug 형식 검증(`Slug` VO) + **전역 유니크** 검증 (`existsBySlug`)
2. `parentId`가 있으면:
   - 부모 조회, 없으면 예외
   - `parent.level < 3` 확인 (3이면 소분류 → 하위 생성 거부)
   - `level = parent.level + 1`
   - 없으면 `level = 1`
3. **name 형제 내 유니크**:
   - 자식: `existsByParentAndName(parent, name)`
   - 루트: `existsByParentIsNullAndName(name)`
   - ⚠️ `uk_name`은 `parent_id`가 NULL이면 MySQL 유니크가 중복을 잡지 못하므로(NULL 취급),
     **루트 name 유니크는 앱 레벨 검증이 실질 방어선**이다.
4. `Category.create(name, slug, parent)` → `categoryRepository.save(...)`

## 트리 조회

- `CategoryService.findAllActive()`(readOnly tx)가 `findAllByIsActiveTrue()`로 활성 카테고리를
  **flat 도메인 리스트**로 반환.
- `CategoryMapper`(어댑터)가 flat 리스트를 `parentId` 기준으로 **중첩 트리(`List<CategoryTreeResponse>`)로
  조립**(루트=level 1부터).
  - 부모 식별은 `category.getParent()`의 `getId()`로 수행. LAZY `@ManyToOne` 프록시라도 FK 기반
    `getId()`는 프록시 초기화 없이 안전(`LazyInitializationException` 미발생).
- 카테고리 수가 많지 않다는 전제. 재귀 CTE는 도입하지 않음.
- 추후 상품 등록에서 판매자가 이 트리를 보고 `categoryId`를 선택한다.

## 권한

- 생성: `Role.ADMIN`만
- 조회: 인증 사용자(판매자 포함) — 상품 등록 시 카테고리 선택에 필요하므로 admin 전용 아님

## 에러 처리

- 중복 slug / 부모 없음 / depth 초과 / 형제 name 중복 → 도메인 예외
  (`DuplicatedEmailException` 패턴 참고, 카테고리 전용 예외 신설)
- `ApiControllerAdvice`에 매핑하여 `ApiResponse.fail(...)` 형태로 응답

## 테스트 (TDD)

- 도메인 단위: `Category.create` 성공 + 각 검증 실패(중복 slug, depth 초과, 부모 없음, name 중복)
- 서비스 슬라이스: 생성 → 저장 검증, 트리 조회 조립 검증
- 컨트롤러 통합: `POST /api/categories`(ADMIN 권한 포함), `GET /api/categories`

## 참고 (상품 등록 연계)

- `product_categories`에 `category_id → categories(id)` FK 존재 → 카테고리는 **같은 DB**.
  상품 등록 시 `CategoryRepository`로 존재 검증 가능.
- `product_categories`는 `primary_flag` 가상 컬럼 + `UNIQUE(primary_flag)`로
  **상품당 대표 카테고리 1개**를 DB 레벨에서 이미 강제 → 상품 등록 설계에서 활용.
