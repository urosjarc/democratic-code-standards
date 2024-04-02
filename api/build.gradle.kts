val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.9"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
//    id("buildSrc.jwks")
}

group = "com.urosjarc"
version = "0.0.1"

application {
    mainClass.set("MainKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

//configure<BuildSrc_jwks_gradle.JwksExtension> {
//    this.outputDir.set("src/main/resources/static/.well-known/jwks.json")
//}

dependencies {
    //Database
    implementation("com.urosjarc:db-messiah:0.0.2")
    implementation("com.urosjarc:db-messiah-extra:0.0.2")
    runtimeOnly("org.postgresql:postgresql:42.7.1")

    //Kotlinx
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    //Logging
    implementation("org.apache.logging.log4j:log4j-core:2.17.1")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")

    val ktor_version = "2.3.8"
    implementation("io.ktor:ktor-server-resources:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
}
