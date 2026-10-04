import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("sample-springboot")
}

dependencies {
    runtimeOnly("com.sample.hexagonal.common:yaml-config")

    runtimeOnly("com.h2database:h2")
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.jar {
    enabled = true
}
