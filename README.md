# DND 지원자 합격 조회 시스템

## 📋 프로젝트 개요

**DND(Designer & Developer) 지원자 합격 결과 조회 시스템**은 기존의 이메일 기반 합격 안내 방식을 웹사이트 직접 조회 기능으로 개선한 프로젝트입니다. 이를 통해 CS 문의를 대폭 감소시키고, 평가부터 발표까지의 프로세스를 자동화하는 것을 목표로 합니다.

---
## 🎯 주요 기능

* **이벤트 관리**: 각 기수별 지원 이벤트 생성 및 관리
* **지원자 상태 조회**: 이름과 이메일로 합격/불합격 결과 실시간 확인
* **강화된 보안**: 개인정보 보호를 위한 데이터 암호화 및 블라인드 인덱스 적용
* **조회 기간 제어**: 관리자가 설정한 특정 기간에만 결과 조회 가능

---

## 🛠 기술 스택
## 🛠 기술 스택

| 구분      | 기술                                                                                     |
| :-------- | :--------------------------------------------------------------------------------------- |
| **Backend** | Kotlin 1.9.25, Spring Boot 3.4.5, Spring Data JPA, Spring Security, Caffeine Cache, Gradle |
| **Database** | MySQL                                                                                    |
| **API Docs** | Spring REST Docs + OpenAPI 3.0 (AsciiDoc)                                                |
| **Testing** | Kotest, JUnit 5, Testcontainers, Mockito Kotlin                                          |

---
## 🏗 시스템 아키텍처

### 주요 기능
1. **이벤트 관리**: 지원 이벤트 생성 및 관리
2. **지원자 상태 조회**: 이름 + 이메일로 합격/불합격 결과 확인
3. **보안**: 개인정보 암호화 및 블라인드 인덱스 적용
4. **조회 기간 제한**: 결과 조회 가능 기간 설정


## 📁 프로젝트 구조

```
src/main/kotlin/ac/dnd/server/
├── admission/                          # 지원자 관리 도메인
│   ├── application/                    # 애플리케이션 서비스
│   │   ├── dto/                       # 커맨드 DTO
│   │   └── service/                   # 비즈니스 로직
│   ├── domain/                        # 도메인 모델
│   │   ├── enums/                     # 열거형 (상태, 타입 등)
│   │   ├── event/                     # 도메인 이벤트
│   │   └── model/                     # 도메인 모델
│   ├── exception/                     # 도메인 예외
│   └── infrastructure/                # 인프라스트럭처
│       ├── event/                     # 이벤트 리스너
│       ├── persistence/               # 데이터 영속성
│       │   ├── converter/             # JPA 컨버터 (암호화)
│       │   ├── crypto/                # 암호화 유틸리티
│       │   ├── dto/                   # 영속성 DTO
│       │   ├── entity/                # JPA 엔티티
│       │   ├── mapper/                # 매퍼
│       │   └── repository/            # JPA 리포지토리
│       └── web/                       # 웹 계층
│           ├── dto/                   # 웹 DTO
│           └── mapper/                # 웹 매퍼
├── auth/                              # 인증/인가
├── common/                            # 공통 모듈
└── shared/                            # 공유 모듈
```



---
## 💎 common 모듈과 shared 모듈을 분리한 이유

서비스의 핵심 로직을 특정 기술(프레임워크)로부터 분리하여, 변화에 유연하고 유지보수가 용이한 확장형 구조를 설계했습니다.
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
│                        ↓                                    │ ← 프레임워크에 의존적
│                   Shared Module                             │
└─────────────────────────────────────────────────────────────┘
```

### Common 모듈: 프레임워크 독립적 핵심 로직

-   **분리 이유**: **순수한 비즈니스 로직을 특정 기술(프레임워크)로부터 보호**프레임워크가 변경되거나 업그레이드되더라도 비즈니스 핵심 코드는 영향을 받지 않도록 하였습니다.
-   **역할**:
    -   도메인 중심의 예외 (`BusinessException`, `ErrorCode`) 정의
    -   어떤 기술에도 의존하지 않는 순수 유틸리티 (날짜 처리 등)
    -   **결과**: 프레임워크 없이도 **빠른 단위 테스트**가 가능하고, 다른 프로젝트에서도 **모듈 재사용**이 용이

### Shared 모듈: 프레임워크 의존적 공통 인프라

-   **분리 이유**: **특정 기술과 관련된 공통 코드를 한 곳에서 중앙 관리**하여 중복을 제거하고, 기술 스택 변경 시 수정 범위를 최소화하기 위함입니다.
-   **역할**:
    -   Spring, JPA 등 프레임워크 기반의 공통 기능 추상화 (`BaseEntity`, Custom Validator)
    -   캐시(`@EnableCaching`), 이벤트(`ApplicationEventPublisher`) 등 횡단 관심사 처리
    -   애플리케이션 전반의 설정 관리 (`CacheConfig`, `CorsProperties`)
    -   **결과**: 만약 Caffeine 캐시를 Redis로 변경해야 할 경우, **`shared` 모듈의 설정 코드만 수정**하면 되므로 도메인 로직에 전혀 영향을 주지 않습니다.

---
