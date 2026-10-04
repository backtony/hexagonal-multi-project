import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("sample-springboot")
}

dependencies {
    implementation(project(":application"))
    implementation(project(":infrastructure:kafka"))
    implementation(platform("io.namastack:namastack-outbox-bom:1.7.3"))
    implementation("io.namastack:namastack-outbox-starter-jdbc")
    implementation("io.namastack:namastack-outbox-kafka")
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.jar {
    enabled = true
}
