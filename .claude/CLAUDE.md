# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 빌드

- 변경 후 `./gradlew ktlintFormat` → `./gradlew clean build` 로 검증한다. 루트의 `ktlintFormat`, `lint` 는 하위 모듈 태스크를 묶은 커스텀 태스크다.
- `build-plugin`, `common` 은 `includeBuild` 로 포함된 별도 빌드다. `common` 모듈은 루트에서 `./gradlew :common:actuator:test` 처럼 실행한다. `-p common` 은 동작하지 않는다.
- 서버 실행에는 Kafka 가 필요하다 (README 의 docker 명령 참고).
- Java 21, Kotlin, Spring Boot 4.x (Jackson 3 = `tools.jackson.*`), Hibernate 7.

## 코드 스타일

- ktlint `intellij_idea` 스타일, 최대 140자. 생성자 파라미터가 2개 이상이면 한 줄에 하나씩(`.editorconfig`).
- import 순서 규칙은 비활성화되어 있어 ktlintFormat 이 정렬해 주지 않는다.

## 문서

모듈 역할, 패키지·포트 규칙, 애그리거트 설계 기준, 코드 관례는 아래 개발가이드를 따른다.

@../docs/개발가이드.md

- 모듈 트리·실행 방법: [README](../README.md)
- 애그리거트별 속성·행위·규칙: [도메인모델](../docs/도메인모델.md)
- 용어: [용어사전](../docs/용어사전.md)
