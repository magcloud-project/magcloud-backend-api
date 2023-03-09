import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.8.0"
    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    kotlin("plugin.jpa") version "1.7.22"
    kotlin("plugin.allopen") version "1.7.22"
    kotlin("plugin.noarg") version "1.7.22"
    id("com.google.cloud.tools.jib") version "3.3.2"
}

group = "co.bearus"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

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
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    implementation("org.springframework.boot:spring-boot-starter-security")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    implementation(platform("com.google.cloud:libraries-bom:26.2.0"))
    implementation("com.google.cloud:google-cloud-firestore")
    implementation("com.google.firebase:firebase-admin:9.1.1")

    implementation("org.flywaydb:flyway-core:9.11.0")
    implementation("org.flywaydb:flyway-mysql")

    implementation("com.nimbusds:nimbus-jose-jwt:9.28")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.1")

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

