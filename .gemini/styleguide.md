# 스타일 가이드: Java & Spring Boot 프로젝트 (Gemini 코드 리뷰용)

**버전:** 1.0
  **최종 수정일:** 2025-05-11

> 이 스타일 가이드는 Java와 Spring Boot를 사용하는 프로젝트의 코드 품질을 향상시키기 위해 작성되었습니다.
> 제공되는 코드를 리뷰할 때 다음 가이드라인을 참고하여 개선이 필요한 부분을 지적해주시고, 가능하다면 더 나은 코드 예시도 함께 제안해주세요.
> **모든 코드 리뷰 피드백과 설명은 한국어로 작성해주시기 바랍니다.**
> 특히 다음 사항들을 중점적으로 검토해주시면 감사하겠습니다:
> 1.  일반 코딩 규칙 (포맷팅, 명명 규칙, 주석, 가독성)
> 2.  Java 모범 사례 (특히, 예외 처리 전략과 try-catch의 신중한 사용)
> 3.  Spring Framework 및 Spring Boot 관용구 (DI, 어노테이션, 설정, 유효성 검사 등)
> 4.  Lombok 사용 규칙 (적절한 어노테이션 사용)
> 5.  테스트 코드 작성 (단위/통합 테스트, 명명, 단언)
> 6.  기본적인 보안 고려 사항
> 7.  레이어드 아키텍처 원칙 준수 여부 (각 레이어의 책임, 의존성 방향)

---

## 1. 일반 코딩 규칙 (General Coding Conventions)
  
  ### 1.1. 포맷팅 (Formatting)
  * **indentation:** 일관된 들여쓰기를 사용합니다 (예: 스페이스 4개). 탭과 스페이스를 혼용하지 않습니다.
  * **line_length:** 한 줄의 최대 길이는 120자를 권장합니다. 가독성을 해치지 않는 선에서 유연하게 적용합니다.
  * **braces:** K&R 스타일(여는 중괄호는 같은 줄, 닫는 중괄호는 다음 줄)을 사용합니다.
  * **blank_lines:** 논리적 코드 블록 사이, 메소드 정의 사이에 적절한 공백 줄을 사용하여 가독성을 높입니다.
  
  ### 1.2. 명명 규칙 (Naming Conventions)
  * **packages:** 소문자, 점(.)으로 구분하며, 레이어를 나타내는 패키지명을 권장합니다 (예: `com.example.project.controller`, `com.example.project.service`, `com.example.project.domain`, `com.example.project.repository`, `com.example.project.config`, `com.example.project.global`).
  * **classes_interfaces_enums_annotations:** 파스칼 케이스(PascalCase)를 사용합니다 (예: `MyClass`, `UserService`, `OrderStatus`, `@CustomAnnotation`).
  * **methods:** 카멜 케이스(camelCase)를 사용합니다 (예: `getUserById`, `calculateTotalPrice`).
  * **variables_fields:** 카멜 케이스(camelCase)를 사용합니다 (예: `userName`, `orderCount`).
  * **constants:** 대문자 스네이크 케이스(UPPER_SNAKE_CASE)를 사용합니다 (예: `MAX_USERS`, `DEFAULT_TIMEOUT`).
  * **boolean_getters:** Boolean 타입의 getter는 `isProperty` 또는 `hasProperty` 형태를 권장합니다 (예: `isActive()`, `hasPermission()`).
  
  ### 1.3. 주석 (Comments)
  * **javadoc:** Public API (클래스, 메소드) 및 복잡한 로직에는 Javadoc 주석을 작성합니다.
  * **implementation_comments:** 코드만으로 의도가 명확하지 않은 경우에만 간결한 구현 주석을 사용합니다. '무엇을' 하는지보다 '왜' 그렇게 했는지 설명합니다.
  * **todo_fixme:** `// TODO: [설명]`, `// FIXME: [설명]` 형식으로 사용하며, 가능한 경우 관련 이슈 티켓 번호를 포함합니다.
  
  ### 1.4. 가독성 (Readability)
  * **magic_numbers_strings:** 의미를 알 수 없는 숫자나 문자열(매직 넘버/스트링) 대신 명명된 상수를 사용합니다.
  * **method_length:** 메소드는 가능한 한 짧게 유지하여 단일 책임을 갖도록 합니다. (예: 30줄 이내 권장)
  * **nesting_depth:** 중첩 제어문(if, for, while 등)의 깊이는 가독성을 위해 2-3단계를 넘지 않도록 노력합니다.

---

## 2. Java 모범 사례 (Java Best Practices)
  
  ### 2.1. 일반 (General)
  * **final_modifier:** `final` 키워드를 적절히 사용하여 불변성을 높입니다 (필드, 지역 변수, 파라미터).
  * **equals_hashcode:** `equals()`를 오버라이드할 경우 반드시 `hashCode()`도 함께 오버라이드합니다. Lombok 사용 시 `@EqualsAndHashCode`를 고려하며, 특히 JPA 엔티티의 경우 ID 또는 비즈니스 키 기반으로 신중하게 필드를 선택합니다. (`onlyExplicitlyIncluded = true` 와 `@EqualsAndHashCode.Include` 조합 권장)
  * **tostring:** 디버깅 및 로깅을 위해 주요 객체에는 `toString()` 메소드를 구현합니다. Lombok 사용 시 `@ToString`을 고려하며, JPA 엔티티의 경우 양방향 관계에서의 순환 참조 및 지연 로딩 문제를 주의하여 필요한 필드만 포함합니다. (`@ToString.Exclude` 또는 `onlyExplicitlyIncluded = true` 활용)
  * **visibility_modifiers:** 필드와 메소드에는 가능한 가장 제한적인 접근 제어자(private > package-private > protected > public)를 사용합니다.
  
  ### 2.2. 컬렉션 (Collections)
  * **return_empty_not_null:** 컬렉션을 반환하는 메소드는 `null` 대신 빈 컬렉션(예: `Collections.emptyList()`)을 반환합니다.
  * **interface_type:** 변수 선언 시 구체적인 구현 클래스보다는 인터페이스 타입을 사용합니다 (예: `List<String> list = new ArrayList<>();`).
  
  ### 2.3. 스트림 및 람다 (Streams & Lambda)
  * **readability:** Stream API와 람다 표현식은 가독성을 향상시킬 때 사용합니다. 지나치게 복잡하거나 긴 체이닝은 피합니다.
  * **side_effects:** Stream의 중간 연산(map, filter 등)에서는 사이드 이펙트를 발생시키지 않습니다.
  
  ### 2.4. Null 안전성 (Null Safety)
  * **optional_usage:** `null`이 반환될 수 있는 메소드의 경우 `java.util.Optional`을 사용하여 명시적으로 처리하는 것을 적극 권장합니다. (단, 성능에 민감한 내부 로직이나 컬렉션 반환 시에는 상황에 맞게 판단)
  * **null_checks:** 외부 입력이나 다른 계층에서 전달된 객체 사용 전 `null` 체크를 고려합니다. Lombok의 `@NonNull` 또는 Spring의 `@Nullable`, `@NonNullApi` 어노테이션을 활용하여 null 안전성을 설계 수준에서 관리합니다.
  
  ### 2.5. 예외 처리 (Exceptions)
  * **specific_exceptions:** 일반적인 `Exception`이나 `Throwable` 대신, 상황에 맞는 구체적인 예외 타입을 사용하거나 정의하여 던집니다. `RuntimeException`을 상속하는 것을 기본으로 고려합니다.
  * **checked_vs_unchecked_guideline:**
  * **Checked Exception의 신중한 사용:** API 호출자에게 예외 처리를 명시적으로 강제해야 하는 복구 가능한 상황에 제한적으로 사용합니다. 남용 시 코드 가독성을 저해하고 상위 레이어까지 불필요한 `throws` 선언을 유발할 수 있습니다.
  * **Unchecked Exception 적극 활용:** 대부분의 애플리케이션 오류 (잘못된 입력, 상태 불일치, 시스템 오류 등)는 Unchecked Exception (주로 `RuntimeException`의 하위 클래스)으로 처리하여, 유연하고 간결한 예외 처리 흐름을 만듭니다. Spring 환경에서는 이러한 예외들이 중앙 집중식 예외 핸들러(`@ControllerAdvice`)에서 처리되는 것을 권장합니다.
  * **thoughtful_exception_handling_over_try_catch:**
  **`try-catch` 블록의 직접적인 사용은 최소화하고, 다른 예외 처리 전략을 우선적으로 고려합니다.**
  * **값의 부재는 `Optional` 활용:** 메소드가 특정 조건에서 값을 반환할 수 없는 경우, `null`을 반환하고 호출자가 `try-catch`로 `NullPointerException`을 처리하기보다는, `Optional<T>`을 반환하여 명시적으로 값의 존재 유무를 처리하도록 유도합니다.
  * **Result 패턴 또는 커스텀 응답 객체 고려:** 여러 결과 상태(성공, 다양한 유형의 실패)를 명확하게 전달해야 할 경우, `try-catch`에 의존하기보다 Result 패턴 객체나 상태 정보를 포함하는 커스텀 응답 객체를 사용하는 것을 고려합니다.
  * **Spring의 선언적 예외 처리 활용:** Controller 단에서는 `@ControllerAdvice`와 `@ExceptionHandler`를 통해 예외를 중앙에서 처리하고, Service 단에서 발생하는 대부분의 `RuntimeException`은 자연스럽게 전파되도록 합니다.
  * **avoid_problematic_try_catch_patterns:**
                           다음과 같은 `try-catch` 사용 패턴을 지양합니다:
  * **광범위한 예외 잡기:** `catch (Exception e)` 또는 `catch (Throwable t)`와 같이 매우 일반적인 예외를 잡는 것은 구체적인 오류 상황을 파악하기 어렵게 만듭니다. 반드시 필요한 경우가 아니라면 피해야 합니다.
  * **비어있는 `catch` 블록 또는 단순 로깅 후 무시:** 예외를 잡은 후 아무런 조치도 취하지 않거나(`//TODO` 주석만 남기는 등), 단순히 로그만 기록하고 정상 흐름처럼 코드를 계속 진행하는 것은 심각한 문제를 은폐할 수 있습니다. 예외는 적절히 처리되거나, 의미 있는 정보를 추가하여 다시 던져지거나, 또는 전파되어야 합니다.
  * **흐름 제어 목적 사용 금지:** `try-catch` 구문을 정상적인 프로그램 실행 흐름을 제어하는 용도로 사용해서는 안 됩니다. 예외 처리는 예외적인 상황을 다루기 위한 것입니다.
  * **과도한 방어적 `try-catch`:** 모든 외부 호출이나 로직을 불필요하게 `try-catch`로 감싸는 것은 코드를 장황하게 만들고 가독성을 떨어뜨립니다. 예외 발생 가능성을 합리적으로 예측하고 필요한 곳에만 사용합니다.
  
  ### 2.6. 자원 관리 (Resource Management)
  * **try_with_resources:** 자원 해제가 필요한 경우(예: `InputStream`, `OutputStream`, `Connection` 등)에는 반드시 `try-with-resources` 구문을 사용하여 자원이 안전하게 해제되도록 보장합니다.

---

## 3. Spring Framework 및 Spring Boot 규칙
  
  ### 3.1. 의존성 주입 (Dependency Injection)
  * **constructor_injection:** 의존성 주입 시 생성자 주입(Constructor Injection)을 최우선으로 사용합니다. Lombok의 `@RequiredArgsConstructor`와 `final` 필드를 함께 사용하면 편리합니다.
  * **field_injection_discouraged:** 필드 주입(`@Autowired`를 필드에 직접 사용)은 테스트의 어려움과 순환 참조 가능성 등으로 인해 권장되지 않습니다.
  
  ### 3.2. 어노테이션 (Annotations)
  * **stereotype_consistency:** 적절한 스테레오타입 어노테이션(`@Service`, `@Repository`, `@Component`, `@Controller`, `@RestController`, `@Configuration`)을 일관되게 사용합니다.
  * **transactional_scope:** `@Transactional` 어노테이션은 일반적으로 public Service 메소드에 적용하며, 필요한 최소 범위에 적용합니다. 읽기 전용 메소드에는 `readOnly = true` 옵션을 고려합니다. 클래스 레벨보다는 메소드 레벨 적용을 권장합니다.
  
  ### 3.3. 설정 (Configuration)
  * **java_based_config:** XML 설정보다는 Java 기반 설정(`@Configuration` 어노테이션 사용)을 선호합니다.
  * **properties_management:** `@Value` 어노테이션이나 `@ConfigurationProperties`를 사용하여 외부 설정 값을 주입받습니다. `@ConfigurationProperties` 사용 시 레코드(record)나 전용 클래스를 활용하여 타입 안전성을 높입니다.
  * **profile_specific_config:** 개발, 테스트, 운영 등 환경별 설정을 위해 Spring Profile (`@Profile`)을 활용합니다.
  
  ### 3.4. 유효성 검사 (Validation)
  * **bean_validation:** DTO 필드 유효성 검사를 위해 Jakarta Bean Validation 어노테이션 (예: `@NotNull`, `@NotBlank`, `@Size`, `@Pattern`)을 사용하고 Controller 메소드 파라미터에 `@Valid`를 적용합니다. 유효성 검증 실패 시 발생하는 `MethodArgumentNotValidException`은 `@ControllerAdvice`에서 일관되게 처리합니다.

---

## 4. Lombok 사용 규칙 (프로젝트에서 Lombok을 사용하는 경우)
  
  ### 4.1. 일반 (General)
  * **specific_annotations_preferred:** `@Data` 어노테이션은 편리하지만, 모든 필드를 `equals/hashCode/toString`에 포함하고, `final`이 아닌 필드에 `setter`를 생성하는 등의 동작으로 인해 예기치 않은 문제를 유발할 수 있습니다. 특히 JPA 엔티티에는 사용을 지양합니다. 대신 `@Getter`, `@Setter`(필요한 경우 최소한으로), `@ToString`(필드 명시 또는 제외), `@EqualsAndHashCode`(필드 명시 또는 `onlyExplicitlyIncluded=true` 사용), `@NoArgsConstructor`, `@AllArgsConstructor`, `@RequiredArgsConstructor` 등을 개별적으로 조합하여 사용하는 것을 권장합니다.
  * **builder_pattern:** 객체 생성이 복잡하거나 파라미터가 많을 경우, 또는 명확한 의미를 부여하며 객체를 생성하고 싶을 때 `@Builder` 사용을 고려합니다.
  * **value_object:** 불변 값 객체에는 `@Value` (Lombok 제공, 모든 필드 private final 및 getter, equals/hashCode, toString, AllArgsConstructor 생성) 또는 직접 `final` 필드와 `@Getter`, `@EqualsAndHashCode` 등을 조합하여 사용합니다.
  * **log_annotations:** 로깅 구현을 위해 `@Slf4j`, `@Log4j2` 등의 어노테이션 사용을 고려합니다. (SLF4J 인터페이스 사용 권장)
  * **nonnull_usage:** Lombok의 `@NonNull`은 생성자나 메소드 파라미터에 사용하여 `NullPointerException`을 자동으로 던지도록 할 수 있지만, Spring의 `@Validated`나 Bean Validation과 함께 사용할 때의 동작을 이해하고 사용합니다. 일반적으로는 Bean Validation을 우선적으로 고려합니다.

---

## 5. 테스트 (Testing)
  
  ### 5.1. 일반 (General)
  * **unit_tests_scope:** 단위 테스트는 특정 클래스나 메소드의 기능에 집중하며, 외부 의존성은 Mock 객체(예: Mockito 사용)로 대체합니다. Service 레이어 로직, 도메인 객체 행위 등이 주 대상입니다.
  * **integration_tests_scope:** 통합 테스트는 여러 컴포넌트 또는 계층 간의 상호작용을 검증합니다. `@SpringBootTest`를 활용하며, 필요한 경우 테스트 슬라이스(예: `@WebMvcTest`, `@DataJpaTest`)를 사용하여 테스트 범위를 제한하고 속도를 높입니다.
  * **test_coverage:** 중요한 비즈니스 로직과 예외 경로에 대한 테스트 커버리지를 확보하려고 노력합니다. 단순 CRUD나 getter/setter에 대한 테스트는 지양합니다.
  
  ### 5.2. 명명 (Naming)
  * **test_method_names:** 테스트 대상 메소드명, 테스트하는 조건(상태), 기대하는 결과를 명시하는 명확한 테스트 메소드 이름을 사용합니다 (예: `[MethodName]_Should[ExpectedBehavior]_When[Condition]`). BDD 스타일(given-when-then) 주석이나 구조도 좋습니다.
  
  ### 5.3. 단언 (Assertions)
  * **assertj_usage:** AssertJ와 같은 Fluent Assertion 라이브러리를 사용하여 가독성 높고 표현력 있는 단언문을 작성합니다.
  
  ### 5.4. 테스트 데이터 (Test Data)
  * **setup_teardown:** 테스트 데이터는 각 테스트 실행 전에 `@BeforeEach` 등에서 설정하고, 필요시 `@AfterEach`에서 정리합니다. 테스트 간 격리를 유지합니다.

---

## 6. 보안 (Security - 기본적인 사항)
  
  * **hardcoded_secrets:** 소스 코드에 비밀번호, API 키 등의 민감 정보를 하드코딩하지 않습니다. 외부 설정 파일, 환경 변수, 또는 Vault/Secrets Manager와 같은 전문 도구를 사용합니다.
  * **input_validation:** 외부로부터 오는 모든 입력값(Request Parameter, Path Variable, Request Body 등)에 대해 철저한 유효성 검사를 수행하여 SQL Injection, XSS 등의 취약점을 방지합니다. (Bean Validation 활용)
  * **dependency_vulnerabilities:** 프로젝트 의존성의 알려진 보안 취약점을 정기적으로 확인하고 업데이트합니다 (예: OWASP Dependency-Check, Snyk, GitHub Dependabot).
  * **sensitive_data_logging:** 로그에 개인정보, 비밀번호 등 민감한 데이터가 기록되지 않도록 주의합니다. 필요한 경우 마스킹 처리합니다.

---

## 7. 아키텍처 (Architecture)
  
  ### 7.1. 레이어드 아키텍처 (Layered Architecture)
  
  #### 7.1.1. 일반 원칙 (General Principles)
  * **dependency_direction:** 상위 레이어(예: Presentation)는 하위 레이어(예: Application/Service)에 의존할 수 있지만, 그 반대는 허용되지 않습니다. 예를 들어, Service 레이어가 Controller 레이어의 메소드를 호출하거나 특정 클래스에 의존해서는 안 됩니다.
  * **strict_layering_adherence:** 레이어를 건너뛰는 직접 호출(예: Controller가 Repository를 직접 호출)을 지양합니다. 호출은 일반적으로 인접한 하위 레이어를 통해 이루어져야 합니다 (예: Presentation -> Application -> Infrastructure/Domain).
  * **separation_of_concerns:** 각 레이어는 명확히 구분된 책임을 가져야 합니다. 관심사를 혼합하지 마십시오 (예: Service 레이어에 프레젠테이션 로직, Repository에 비즈니스 로직 포함 금지).
                              
                              #### 7.1.2. 프레젠테이션 레이어 (Presentation Layer)
                              (예: `@RestController`, `@Controller`가 위치하는 패키지 - `com.example.project.controller`)
  * **role:** HTTP 요청 수신 및 처리, 요청 유효성 검사 (Bean Validation 활용), Application(Service) 레이어로 작업 위임, 결과(DTO)를 HTTP 응답으로 변환하는 책임을 가집니다. 이 레이어는 '얇게(thin)' 유지되어야 합니다.
  * **no_direct_repository_access:** Presentation 레이어(Controller)는 Repository/DAO 레이어에 직접 접근하거나 주입받아서는 안 됩니다. 모든 데이터 접근은 Service 레이어를 통해 이루어져야 합니다.
  * **no_core_business_logic:** 핵심 비즈니스 로직, 복잡한 계산, 다수 작업의 조합 등은 Service 또는 Domain 레이어에 위치해야 하며, Controller에 포함되어서는 안 됩니다.
  * **dto_usage_strict:** 요청(Request)과 응답(Response)에는 반드시 DTO(Data Transfer Object)를 사용합니다. JPA 엔티티나 내부 도메인 객체를 Controller의 파라미터나 반환 타입으로 직접 사용하지 않습니다.
  * **dependency:** 주로 Application(Service) 레이어의 인터페이스 또는 클래스에 의존합니다.
  * **global_exception_handling:** 발생하는 예외는 `@ControllerAdvice`와 `@ExceptionHandler`를 통해 전역적으로 처리하여 일관된 오류 응답을 제공합니다.
  
  #### 7.1.3. 애플리케이션 레이어 (Application Layer)
(예: `@Service`가 위치하는 패키지 - `com.example.project.service`)
  * **role:** 애플리케이션 유스케이스를 조정(orchestrate)합니다. 애플리케이션 특정 비즈니스 로직을 포함하고, Repository 및 다른 인프라 컴포넌트 호출을 조정하며, 트랜잭션을 관리합니다. Presentation과 Domain/Infrastructure 사이의 중재자 역할을 합니다.
  * **transaction_management:** 일반적으로 Service 메소드 레벨에서 트랜잭션 경계를 처리합니다. `@Transactional` 어노테이션을 사용합니다. (읽기 전용은 `readOnly=true` 고려)
  * **no_presentation_specifics:** Application 레이어는 Presentation 레이어의 관심사(예: `HttpServletRequest/Response` 직접 사용, HTTP 관련 세부 정보)로부터 독립적이어야 합니다.
  * **data_mapping:** Presentation DTO를 Domain 객체/Command로, Domain 객체/Entity를 Presentation DTO로 매핑하는 책임을 가질 수 있습니다 (또는 별도의 Mapper 클래스/라이브러리 사용 - 예: MapStruct).
  * **dependency:** Domain 레이어의 객체/서비스 및 Infrastructure 레이어의 인터페이스(예: Repository 인터페이스)에 의존할 수 있습니다.
                  
                  #### 7.1.4. 도메인 레이어 (Domain Layer)
                  (예: 핵심 도메인 객체, 엔티티(JPA 엔티티와 분리 시), 값 객체가 위치하는 패키지 - `com.example.project.domain`)
  * **role:** 재사용 가능한 핵심 비즈니스 로직, 비즈니스 규칙, 엔티티, 값 객체(Value Object)를 포함합니다. 이 레이어는 비즈니스 소프트웨어의 심장부여야 합니다.
  * **rich_model_preference:** 엔티티와 값 객체가 행위를 캡슐화하는 풍부한(rich) 도메인 모델을 지향합니다. 단순 데이터 홀더(getter/setter만 가진)인 빈약한(anemic) 도메인 모델은 지양합니다.
  * **framework_independence_ideal:** 이상적으로 Domain 레이어는 특정 프레임워크(예: Spring)로부터 독립적이어야 합니다. Spring 어노테이션 사용은 최소화하고, 사용 시 명확한 근거가 있어야 합니다 (예: JPA 엔티티가 도메인의 일부로 간주될 때 JPA 어노테이션 사용). 도메인 객체는 순수 Java(POJO)로 작성하는 것을 목표로 합니다.
  * **no_external_dependencies:** Domain 레이어는 Presentation, Application(Service), 또는 구체적인 Infrastructure 컴포넌트에 직접 의존해서는 안 됩니다. 다른 레이어의 서비스가 필요하다면, 도메인 내에 정의된 인터페이스에 의존하거나 인자로 전달받아야 합니다.
  
  #### 7.1.5. 인프라스트럭처 레이어 (Infrastructure Layer)
(예: `@Repository`, 외부 서비스 클라이언트, 메시지 큐 관련 코드가 위치하는 패키지 - `com.example.project.repository`, `com.example.project.client`, `com.example.project.adapter` 등)
  * **role:** 데이터 영속성, 외부 서비스 통신, 메시징, 파일 시스템 접근 등 모든 기술적인 관심사를 처리합니다. Application 또는 Domain 레이어에서 정의한 인터페이스를 구현하는 경우가 많습니다.
  * **no_business_logic:** Infrastructure 컴포넌트(예: Repository)는 비즈니스 로직을 포함해서는 안 됩니다. 비즈니스 로직은 Application 또는 Domain 레이어에 위치해야 합니다.
  * **data_access_responsibility:** Repository는 도메인/엔티티 객체와 데이터베이스 표현 간의 매핑 및 데이터 CRUD, 조회 로직에 집중합니다. Presentation 레이어 DTO로의 변환은 Repository의 책임이 아닙니다.

---