plugins {
    id("sample-springboot")
}

dependencies {
    runtimeOnly(project(":adapter:inbound:listener"))
    runtimeOnly(project(":adapter:outbound:repository"))
    runtimeOnly(project(":adapter:outbound:security"))
    runtimeOnly(project(":adapter:outbound:email"))
    runtimeOnly(project(":adapter:outbound:producer"))
    runtimeOnly(project(":infrastructure:h2"))
    runtimeOnly("com.sample.hexagonal.common:actuator")

    implementation("org.springframework.boot:spring-boot-starter-webmvc")
}
