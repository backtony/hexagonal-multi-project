plugins {
    id("sample-kotlin-jvm")
}

dependencies {
    testImplementation(project(":domain"))
    testImplementation(project(":application"))
    testImplementation(project(":adapter:inbound:controller"))
    testImplementation(project(":adapter:inbound:listener"))
    testImplementation(project(":adapter:inbound:event"))
    testImplementation(project(":adapter:outbound:repository"))
    testImplementation(project(":adapter:outbound:producer"))
    testImplementation("com.tngtech.archunit:archunit:1.4.1")
}
