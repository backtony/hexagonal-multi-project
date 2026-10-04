import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("sample-springboot")
}

dependencies {
    api(project(":domain"))

    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
    implementation("jakarta.validation:jakarta.validation-api")
    implementation("com.sample.hexagonal.common:utils")

    testFixturesApi(testFixtures(project(":domain")))

    testRuntimeOnly(project(":adapter:outbound:repository"))
    testRuntimeOnly(project(":infrastructure:h2"))
    testRuntimeOnly("org.springframework.boot:spring-boot-starter-validation")
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.jar {
    enabled = true
}
