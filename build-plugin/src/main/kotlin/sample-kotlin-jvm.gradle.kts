import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    // https://discuss.gradle.org/t/applying-a-plugin-version-inside-a-convention-plugin/42160
    // https://blog.sapzil.org/2022/03/04/gradle-convention-plugins/
    // 플러그인의 버전은 현재 프로젝트의 build.gradle.kts에 명시해야만 합니다.
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.kotlinx.kover")
    // for implementation, java, etc..
    id("java-library")
    `java-test-fixtures`

    kotlin("jvm")
    kotlin("kapt")
}

repositories {
    mavenCentral()
}

// Spring Boot BOM이 kotlin 의존성을 자체 버전으로 내리지 않도록 플러그인 버전으로 고정
extra["kotlin.version"] = getKotlinPluginVersion()

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("io.github.oshai:kotlin-logging-jvm:8.0.4")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:6.2.5")
    testImplementation("io.kotest:kotest-assertions-core-jvm:6.2.5")
    testImplementation("io.mockk:mockk:1.14.11")

    testFixturesImplementation("org.instancio:instancio-core:6.1.0")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// https://github.com/JLLeitschuh/ktlint-gradle?tab=readme-ov-file#configuration
ktlint {
    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.HTML)
    }
}

// https://github.com/Kotlin/kotlinx-kover/blob/main/kover-gradle-plugin/examples/jvm/defaults/build.gradle.kts
kover {
    reports {
        filters {
            includes {
                classes("com.sample.hexagonal.*")
            }
        }
    }
}
