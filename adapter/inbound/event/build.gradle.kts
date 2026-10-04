import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("sample-springboot")
}

dependencies {
    implementation(project(":application"))
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.jar {
    enabled = true
}
