import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("sample-springboot")
}

dependencies {
    api("tools.jackson.module:jackson-module-kotlin")
    api("tools.jackson.core:jackson-databind")
    api("tools.jackson.dataformat:jackson-dataformat-yaml")

    implementation("org.springframework:spring-context")
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.jar {
    enabled = true
}
