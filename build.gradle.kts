import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.7.0"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  kotlin("jvm") version "1.6.21"
  kotlin("plugin.spring") version "1.6.21"
}

group = "ovh.snet.grzybek"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.liquibase:liquibase-core:4.6.2")
  implementation("org.springframework.boot:spring-boot-starter-jdbc:2.7.0")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.neo4j:neo4j-jdbc:4.0.5")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
  implementation("com.squareup.okhttp3:okhttp:4.10.0")
  implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
  implementation("org.neo4j:neo4j-ogm-bolt-driver:3.2.35")
  implementation("org.springframework.boot:spring-boot-starter-data-neo4j")
  implementation("org.springframework.boot:spring-boot-starter-data-rest")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  testImplementation("org.springframework.boot:spring-boot-starter-test")

  runtimeOnly("org.postgresql:postgresql:42.3.6")
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
