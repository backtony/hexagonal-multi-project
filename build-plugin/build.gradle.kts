plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // jvm
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.21")
    implementation("org.jetbrains.kotlin.kapt:org.jetbrains.kotlin.kapt.gradle.plugin:2.2.21")
    implementation("org.jetbrains.kotlinx.kover:org.jetbrains.kotlinx.kover.gradle.plugin:0.9.11")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:13.1.0")

    // spring
    implementation("org.jetbrains.kotlin:kotlin-allopen:2.2.21")
    implementation("org.jetbrains.kotlin:kotlin-noarg:2.2.21")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.5.13")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.7")
}
