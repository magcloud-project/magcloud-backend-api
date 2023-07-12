import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.8.0"
    id("org.springframework.boot") version "3.1.1"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
    kotlin("plugin.noarg") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    id("com.google.cloud.tools.jib") version "3.3.2"
}

group = "co.bearus"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

kapt.includeCompileClasspath = false

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}
noArg {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    runtimeOnly("com.mysql:mysql-connector-j")

    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-kotlin-codegen:5.0.0")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("com.nimbusds:nimbus-jose-jwt:9.30.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")


    implementation(platform("com.google.cloud:libraries-bom:26.10.0"))
    implementation("com.google.cloud:google-cloud-firestore")
    implementation("com.google.firebase:firebase-admin:9.1.1")

    implementation("org.flywaydb:flyway-core:9.16.0")
    implementation("org.flywaydb:flyway-mysql")

    implementation("com.github.f4b6a3:ulid-creator:5.2.0")

    implementation(platform("software.amazon.awssdk:bom:2.20.56"))
    implementation("software.amazon.awssdk:s3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


val activeProfile: String? = System.getProperty("spring.profiles.active")
val repoURL: String? = System.getProperty("imageName")
val imageTag: String? = System.getProperty("imageTag")

jib {
    from {
        image = "amazoncorretto:17-alpine3.17-jdk"
    }
    to {
        image = repoURL
        tags = setOf(imageTag)
    }
    container {
        jvmFlags = listOf(
            "-Dspring.profiles.active=${activeProfile}",
            "-Dserver.port=8080",
            "-Djava.security.egd=file:/dev/./urandom",
            "-Dfile.encoding=UTF-8",
            "-XX:+UnlockExperimentalVMOptions",
            "-XX:+UseContainerSupport",
            "-Xms1G", //min
            "-Xmx1G", //max
            "-XX:+DisableExplicitGC", //System.gc() 방어
            "-server",
        )
        ports = listOf("8080")
    }
}

