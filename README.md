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


## 문서

- [개발가이드](docs/개발가이드.md): 모듈별 역할, 패키지·포트 규칙, 애그리거트 설계 기준, 코드 작성 관례
- [도메인모델](docs/도메인모델.md): 애그리거트별 속성·행위·규칙
- [용어사전](docs/용어사전.md)
