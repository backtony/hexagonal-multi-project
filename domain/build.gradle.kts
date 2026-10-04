import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("sample-kotlin-jvm")
    kotlin("plugin.jpa") version "2.2.21"
    kotlin("plugin.allopen") version "2.2.21"
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    // Spring Boot 플러그인 없이 Boot BOM 으로 jakarta 버전만 맞춘다. testFixtures 와 소비 모듈도 같은 버전을 쓰도록 api 로 노출한다.
    api(platform(SpringBootPlugin.BOM_COORDINATES))

    // 엔티티 매핑 애노테이션(@Entity, @OneToMany 등)
    api("jakarta.persistence:jakarta.persistence-api")
}
