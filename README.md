# splearn

헥사고날 아키텍처(Ports and Adapters)를 Gradle 멀티 모듈로 구성한 샘플 프로젝트

## 실행

```sh
docker run --name kafka -p 9092:9092 apache/kafka:3.9.0

./gradlew :server:api:bootRun
./gradlew :server:consumer:bootRun
```

## 모듈 구조

```
splearn
├── domain                     # 도메인 모델
├── application                # 유스케이스, 포트
├── adapter
│   ├── inbound
│   │   ├── controller         # HTTP
│   │   ├── listener           # Kafka
│   │   └── event              # Spring 애플리케이션 이벤트
│   └── outbound
│       ├── repository         # JPA
│       ├── security           # 비밀번호 암호화
│       ├── email              # 메일 발송
│       └── producer           # 도메인 이벤트 발행 (Namastack outbox → Kafka)
├── infrastructure
│   ├── h2                     # DB 설정
│   └── kafka                  # Kafka 공통 producer/consumer 설정
├── server
│   ├── api                    # HTTP API 서버
│   └── consumer               # Kafka consumer 서버
├── architecture-test          # ArchUnit 아키텍처 규칙
├── build-plugin               # (included build) Gradle convention plugin
└── common                     # (included build) 공통 라이브러리
```

## 의존 방향

```
server ──runtimeOnly──▶ adapter ──▶ application ──api──▶ domain
                           │
                           └──▶ infrastructure
```

- 모든 의존은 바깥에서 안쪽(`domain`)으로만 향한다.
- `domain`, `application` 은 어댑터와 인프라를 알지 못한다.
- `server` 는 어댑터를 `runtimeOnly` 로만 의존한다. 서버 코드에서 어댑터 클래스를 직접 참조할 수 없고, 어떤 어댑터를 조합해 띄울지만 결정한다.

## 모듈별 역할

### domain

- 엔티티, 값 객체, 도메인 이벤트, 도메인 예외
- 애그리거트별 패키지(`member`, `instructor`, `course`, `curriculum`, `enrollment`)로 나누고, 값 객체도 사용하는 애그리거트 패키지에 둔다. (ex. `member.Email`)
- 애그리거트 루트 엔티티에는 `@AggregateRoot` 를 붙인다.
- 외부 의존은 `jakarta.persistence-api` 뿐이다. JPA 객체 참조와 매핑 어노테이션은 도메인 모델에 그대로 둔다.
- 공통 기반 클래스 `AbstractEntity` 가 `id`, `createdAt`, `updatedAt`, `deleted`(soft delete) 를 가진다.

### application

애그리거트별 패키지 아래에 서비스와 포트를 둔다.

```
application
├── service
│   └── {aggregate}             # member, instructor, course, curriculum, enrollment
│       ├── {Aggregate}ModifyService, {Aggregate}QueryService
│       ├── port
│       │   ├── inbound         # 유스케이스 인터페이스 (ex. MemberRegister, CourseFinder)
│       │   └── outbound        # 외부 의존 인터페이스 (ex. MemberRepository, EmailSender)
│       └── spi                 # 다른 애그리거트가 구현하는 인터페이스 (ex. course.spi.CurriculumValidator)
└── support                     # 이벤트 발행 포트, 공통 예외
```

- 어댑터는 inbound 포트를 호출하고, outbound 포트를 구현한다.
- 다른 애그리거트를 조회할 때는 그 애그리거트의 inbound `Finder` 를 사용한다.
- 애그리거트 간 의존이 순환하게 되면 사용하는 쪽이 `spi` 인터페이스를 정의해 의존을 역전한다.

### adapter

| 모듈 | 방향 | 역할 | 주요 의존 |
|---|---|---|---|
| `inbound:controller` | inbound | REST API | spring-webmvc |
| `inbound:listener` | inbound | Kafka 메시지 수신 | `infrastructure:kafka` |
| `inbound:event` | inbound | 같은 트랜잭션 안의 내부 도메인 이벤트 수신 | - |
| `outbound:repository` | outbound | outbound repository 포트 구현 | spring-data-jpa |
| `outbound:security` | outbound | `PasswordEncoder` 구현 | spring-security-crypto |
| `outbound:email` | outbound | `EmailSender` 구현 | - |
| `outbound:producer` | outbound | `DomainEventPublisher` 구현 | Namastack outbox, `infrastructure:kafka` |

어댑터는 입력·출력 채널 단위로 나눈다. 같은 Kafka 를 쓰더라도 수신(`listener`)과 발행(`producer`)은 다른 모듈이다.

### infrastructure

어댑터가 사용하는 기술 설정만 둔다. 비즈니스 코드는 두지 않는다.

- `h2`: 데이터소스 설정, H2 드라이버
- `kafka`: 공통 `KafkaTemplate`, consumer container factory, 에러 핸들러

각 모듈의 설정 파일은 `src/main/resources/config/*.yml` 에 두며, `common:yaml-config` 가 기동 시 자동으로 로딩한다.

### server

실행 가능한 Spring Boot 애플리케이션. 필요한 어댑터와 인프라를 `runtimeOnly` 로 조합한다.

| 모듈 | inbound | outbound |
|---|---|---|
| `api` | controller, event | repository, security, email, producer |
| `consumer` | listener, event | repository, security, email, producer |

### architecture-test

ArchUnit 으로 아래 규칙을 검증한다.

- 헥사고날 계층 의존 방향 (`hexagonalArchitecture`)
- 애그리거트 간 순환 의존 금지 (`aggregateFreeOfCycles`, `applicationAggregateFreeOfCycles`)
- 애그리거트 간 허용된 의존 (`aggregateDependencies`)

### build-plugin, common (included build)

`settings.gradle.kts` 에서 `includeBuild` 로 포함한다.

- `build-plugin`: 모듈 공통 빌드 설정을 convention plugin 으로 제공한다.
  - `sample-kotlin-jvm`: Kotlin, ktlint, kover, 테스트(Kotest, mockk) 설정
  - `sample-springboot`: `sample-kotlin-jvm` + Spring Boot
- `common`: 프로젝트와 무관하게 재사용할 수 있는 라이브러리
  - `utils`, `json`, `yaml-config`, `actuator`
