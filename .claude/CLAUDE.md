# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 빌드

- 변경 후 `./gradlew ktlintFormat` → `./gradlew clean build` 로 검증한다. 루트의 `ktlintFormat`, `lint` 는 하위 모듈 태스크를 묶은 커스텀 태스크다.
- `build-plugin`, `common` 은 `includeBuild` 로 포함된 별도 빌드다. `common` 모듈은 루트에서 `./gradlew :common:actuator:test` 처럼 실행한다. `-p common` 은 동작하지 않는다.
- 서버 실행에는 Kafka 가 필요하다 (README 의 docker 명령 참고).
- Java 21, Kotlin, Spring Boot 4.x (Jackson 3 = `tools.jackson.*`), Hibernate 7.

## 아키텍처

헥사고날(Ports and Adapters) 멀티 모듈. 모듈 구성과 역할은 [README.md](../README.md) 참고.

### 의존 방향

`server → adapter → application → domain`, adapter 는 필요 시 `infrastructure` 에도 의존한다.

- `domain` 은 `jakarta.persistence-api` 만 의존한다. JPA 엔티티가 곧 도메인 모델이다(별도 영속 모델 없음).
- `application` 은 Spring 컨텍스트/트랜잭션만 의존하고 어댑터를 모른다.
- `server:*` 는 어댑터를 `runtimeOnly` 로만 조합한다. 서버 코드에서 어댑터 클래스를 참조할 수 없다.
- `architecture-test` 의 `HexagonalArchitectureTest` 가 계층 방향, 애그리거트 간 순환, 허용된 애그리거트 의존을 검증한다. 새 애그리거트 의존을 추가하면 이 테스트가 깨질 수 있다.

### application 패키지 규칙

```
application/service/{aggregate}/
  {Aggregate}ModifyService, {Aggregate}QueryService   # inbound 포트 구현
  port/inbound    # 어댑터가 호출하는 유스케이스 인터페이스 (MemberRegister, CourseFinder ...)
  port/outbound   # 서비스가 필요로 하는 인터페이스 (MemberRepository, EmailSender ...)
  spi             # 다른 애그리거트가 구현하는 인터페이스
application/support/  # DomainEventPublisher, 공통 예외, 토픽
```

- 다른 애그리거트 조회는 해당 애그리거트의 inbound `Finder` 포트를 통한다.
- 애그리거트 간 호출이 순환을 만들면 호출하는 쪽이 `spi` 인터페이스를 정의하고 상대가 구현한다. (예: `course.spi.CurriculumValidator` ← `CurriculumValidationService`)
- outbound repository 포트는 adapter 의 Spring Data `Repository` 인터페이스가 직접 상속해 구현한다.

### 도메인 이벤트

- `DomainEvent`(`key`, `aggregateRoot`, `registeredDate`)는 서비스에서 저장 후 생성해 `DomainEventPublisher` 로 발행한다.
  - `publishInternalEvent`: Spring `ApplicationEventPublisher` 로 같은 트랜잭션 안에서 처리 → `adapter:inbound:event` 리스너가 받아 다른 애그리거트의 inbound 포트 호출 (예: 강의 생성 → 커리큘럼 생성)
  - `publishExternalEvent`: Namastack outbox 에 같은 트랜잭션으로 저장 → 커밋 후 Kafka 로 발행 → `server:consumer` 의 `adapter:inbound:listener` 가 수신. 최소 한 번 전달이므로 consumer 는 멱등해야 한다.
- 토픽 라우팅은 `adapter:outbound:producer` 의 `NamastackConfig`(`InternalTopic.from(event)`), 폴링·재시도 설정은 `config/outbox.yml`.

### 설정 로딩

각 모듈의 `src/main/resources/config/*.yml` 은 `common:yaml-config` 의 `EnvironmentPostProcessor` 가 기동 시 자동 로딩한다. 모듈별 설정(DB, Kafka, outbox)은 server 가 아니라 해당 모듈에 둔다.

## 테스트

- Kotest(`StringSpec`) + mockk/springmockk. Spring 테스트 모듈은 `src/test/kotlin/io/kotest/provided/ProjectConfig.kt` 로 `SpringExtension` 을 등록한다.
- 테스트 진입 애노테이션:
  - `application`: `@ApplicationServiceTest` (`@SpringBootTest` + `@Transactional`, 이벤트는 `RecordingDomainEventPublisher` 로 기록)
  - `adapter:outbound:repository`: `@RepositoryTest`
  - `adapter:inbound:controller`: `@WebMvcTest` + `@MockkBean`
- 테스트 데이터는 `domain` 의 testFixtures(`MemberFixture`, `CourseFixture` ...)를 `testImplementation(testFixtures(project(":domain")))` 로 사용한다.

## 코드 스타일

- ktlint `intellij_idea` 스타일, 최대 140자. 생성자 파라미터가 2개 이상이면 한 줄에 하나씩(`.editorconfig`).
- import 순서 규칙은 비활성화되어 있어 ktlintFormat 이 정렬해 주지 않는다.

## 문서

- `docs/개발가이드.md` 는 원본(Java/Lombok, `provided`/`required` 패키지) 기준으로 작성되어 현재 코드와 다르다. 패키지·네이밍 규칙은 이 파일과 README 를 우선한다.
- `docs/도메인모델.md`, `docs/용어사전.md` 는 도메인 규칙 참고용.
