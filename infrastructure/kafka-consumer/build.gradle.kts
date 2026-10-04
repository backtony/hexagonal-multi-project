import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("sample-springboot")
}

dependencies {
    runtimeOnly("com.sample.hexagonal.common:yaml-config")
    implementation("com.sample.hexagonal.common:json")

    api("org.springframework.kafka:spring-kafka")
    implementation("io.micrometer:micrometer-core")
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.jar {
    enabled = true
}
