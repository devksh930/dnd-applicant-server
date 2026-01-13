# DND 지원자 합격 조회 시스템

[![CI](https://github.com/devksh930/dnd-applicant-server/actions/workflows/ci.yml/badge.svg)](https://github.com/devksh930/dnd-applicant-server/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/devksh930/dnd-applicant-server/graph/badge.svg)](https://codecov.io/gh/devksh930/dnd-applicant-server)

## 프로젝트 개요

**DND(Designer & Developer) 지원자 합격 결과 조회 시스템**은 기존의 이메일 기반 합격 안내 방식을 웹사이트 직접 조회 기능으로 개선한 프로젝트입니다. 이를 통해 CS 문의를 대폭 감소시키고, 평가부터 발표까지의 프로세스를 자동화하는 것을 목표로 합니다.

---

## 주요 기능

- **이벤트 관리**: 각 기수별 지원 이벤트 생성 및 관리
- **지원자 상태 조회**: 이름과 이메일로 합격/불합격 결과 실시간 확인
- **개인정보 보호**: AES-256 암호화 및 HMAC 블라인드 인덱싱 적용
- **조회 기간 제어**: 관리자가 설정한 특정 기간에만 결과 조회 가능
- **JWT 인증**: Access Token + Refresh Token Rotation 방식

---

## 기술 스택

| 구분 | 기술 |
|:-----|:-----|
| **Backend** | Kotlin 1.9.25, Spring Boot 3.4.5, Spring Data JPA, Spring Security |
| **Database** | MySQL, HikariCP |
| **Cache** | Caffeine |
| **Authentication** | JWT (JJWT 0.12.3), BCrypt |
| **API Docs** | Spring REST Docs + OpenAPI 3.0, Swagger UI |
| **Testing** | Kotest 5.8.0, JUnit 5, Testcontainers, Mockito Kotlin |
| **Build** | Gradle, JVM 21 |

---

## 프로젝트 구조

```
src/main/kotlin/ac/dnd/server/
├── admission/                          # 지원자 관리 도메인
│   ├── application/                    # 애플리케이션 서비스
│   │   ├── dto/                        # 커맨드 DTO
│   │   └── service/                    # 비즈니스 로직
│   ├── domain/                         # 도메인 모델
│   │   ├── enums/                      # ApplicantStatus, ApplicantType, EventStatus
│   │   ├── event/                      # 도메인 이벤트
│   │   └── model/                      # ApplicantData, EventData, ViewablePeriod
│   ├── exception/                      # 도메인 예외
│   └── infrastructure/                 # 인프라스트럭처
│       ├── persistence/                # JPA 엔티티, 리포지토리, 암호화 컨버터
│       └── web/                        # REST 컨트롤러, DTO
│
├── account/                            # 계정/인증 도메인
│   ├── domain/                         # Role, UserKey
│   └── infrastructure/
│       ├── persistence/                # Account, RefreshToken 엔티티
│       ├── security/                   # JWT, Spring Security 설정
│       │   ├── authentication/         # 인증 필터, 핸들러
│       │   ├── jwt/                    # JwtTokenProvider, JwtAuthenticationFilter
│       │   └── config/                 # SecurityConfig
│       └── web/                        # AuthController
│
├── common/                             # 프레임워크 독립적 공통 모듈
│   ├── exception/                      # ErrorCode, BusinessException
│   └── util/                           # 순수 유틸리티
│
├── shared/                             # 프레임워크 의존적 공유 모듈
│   ├── config/                         # Cache, CORS, 암호화 설정
│   ├── annotation/                     # @AuthUser
│   ├── dto/                            # LoginInfo
│   └── persistence/                    # BaseEntity (Auditing)
│
├── notification/                       # GitHub 웹훅 연동
└── health/                             # 헬스체크
```

---

## 아키텍처

### 계층 구조

```
┌─────────────────────────────────────┐
│     Web Layer (Controller)          │  ← REST API 진입점
└─────────────┬───────────────────────┘
              ↓
┌─────────────────────────────────────┐
│   Application Layer (Service)       │  ← 비즈니스 로직 조율
└─────────────┬───────────────────────┘
              ↓
┌─────────────────────────────────────┐
│   Domain Layer (Business Rules)     │  ← 순수 비즈니스 규칙
└─────────────┬───────────────────────┘
              ↓
┌─────────────────────────────────────┐
│   Infrastructure Layer              │  ← 기술 구현 (JPA, 암호화 등)
└─────────────────────────────────────┘
```

### Common vs Shared 모듈 분리

비즈니스 로직을 특정 프레임워크로부터 분리하여 유지보수가 용이한 구조를 설계했습니다.

```
┌─────────────────────────────────────────────────────────────┐
│                     Domain Layer                            │
│                  (Business Logic)                           │
│                        ↓                                    │
│                   Common Module                             │ ← 프레임워크 독립적
│              (Framework-Agnostic)                           │
├─────────────────────────────────────────────────────────────┤
│                Infrastructure Layer                         │
│               (Framework-Dependent)                         │
│                        ↓                                    │
│                   Shared Module                             │ ← 프레임워크 의존적
└─────────────────────────────────────────────────────────────┘
```

**Common 모듈**
- 순수한 비즈니스 로직을 프레임워크로부터 보호
- 도메인 중심의 예외 (`BusinessException`, `ErrorCode`) 정의
- 프레임워크 없이 빠른 단위 테스트 가능

**Shared 모듈**
- 프레임워크 기반 공통 기능 중앙 관리
- `BaseEntity`, Cache, CORS 등 횡단 관심사 처리
- 기술 스택 변경 시 수정 범위 최소화

---
## 인증 시스템

### JWT 토큰 전략

- **Access Token**: 인증 헤더(`Authorization: Bearer`)로 전달
- **Refresh Token**: HttpOnly 쿠키로 전달
- **알고리즘**: HMAC SHA-256

### Refresh Token Rotation

Access Token 갱신 시 Refresh Token도 함께 교체하여 토큰 탈취 위험을 최소화합니다.

```
1. Access Token 만료
2. Refresh Token으로 갱신 요청
3. 새로운 Access Token + 새로운 Refresh Token 발급
4. 기존 Refresh Token 폐기 (DB 업데이트)
```

---

## 개인정보 보호

### AES-256 암호화

지원자의 이름과 이메일은 DB에 암호화되어 저장됩니다.

```kotlin
@Convert(converter = StringCryptoConverter::class)
val name: String  // 저장 시 암호화, 조회 시 복호화
```

### HMAC 블라인드 인덱싱

암호화된 데이터도 검색 가능하도록 HMAC 기반 블라인드 인덱스를 사용합니다.

- 원본 데이터는 검색어로 저장되지 않음
- HMAC 해시값만으로는 원본 복원 불가능
- 동일 입력에 대해 항상 동일한 해시값 생성

---

## 환경 변수

| 환경 변수 | 설명 |
|:---------|:-----|
| `SPRING_DATASOURCE_URL` | MySQL JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | DB 사용자명 |
| `SPRING_DATASOURCE_PASSWORD` | DB 비밀번호 |
| `JWT_SECRET` | JWT 서명 키 (최소 256비트) |
| `JWT_COOKIE_SECURE` | HTTPS 전용 쿠키 여부 |
| `ENCRYPTION_TEXT_PASSWORD` | AES 마스터 키 |
| `ENCRYPTION_TEXT_SALT` | AES 솔트 |
| `HMAC_TEXT` | HMAC 비밀키 |

---

## 실행 방법

### 요구사항

- JDK 21
- MySQL 8.x
- Gradle
- Docker Desktop 4.34.x 이하 (Testcontainers 사용 시)

> **Note**: Docker Desktop 29.x (4.35+) 버전은 최소 API 버전이 1.44로 변경되어 Testcontainers와 호환되지 않습니다.
> 테스트 실행 시 Docker Desktop 4.34.x 이하 버전을 사용하거나, [Colima](https://github.com/abiosoft/colima)를 대안으로 사용하세요.

### 빌드 및 실행

```bash
# 빌드
./gradlew build

# 테스트
./gradlew test

# 실행
./gradlew bootRun

# REST Docs 생성
./gradlew restDocsTest
```

### Docker (MySQL)

```bash
docker run -d \
  --name dnd-mysql \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=dnd_db \
  -p 3306:3306 \
  mysql:8
```

---

## API 문서

- **Swagger UI**: `/swagger-ui.html`
- **OpenAPI Spec**: `/static/swagger/openapi.json`
- **REST Docs**: `/static/docs/api-docs.html`
