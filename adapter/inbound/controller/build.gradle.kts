import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("sample-springboot")
}

dependencies {
    implementation(project(":application"))

    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("tools.jackson.module:jackson-module-kotlin")

    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation(testFixtures(project(":application")))
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.jar {
    enabled = true
}
