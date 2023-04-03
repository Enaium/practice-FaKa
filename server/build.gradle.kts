import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.spring") version "1.8.10"
    kotlin("kapt") version "1.8.10"
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}

allprojects {
    group = "cn.enaium"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "com.google.devtools.ksp")


    java.sourceCompatibility = JavaVersion.VERSION_17

    project.extra.apply {
        val jimmer = "0.7.19"
        val doc = "2.0.3"
        val cloud = "4.0.1"
        val nacos = "2022.0.0.0-RC1"
        val saToken = "1.34.0"
        val therapi = "0.15.0"
        val alipay = "4.35.87.ALL"

        this["web"] = "org.springframework.boot:spring-boot-starter-web"
        this["webflux"] = "org.springframework.boot:spring-boot-starter-webflux"
        this["doc"] = "org.springdoc:springdoc-openapi-starter-webmvc-ui:$doc"
        this["doc-webflux"] = "org.springdoc:springdoc-openapi-starter-webflux-ui:$doc"
        this["jimmer"] = "org.babyfish.jimmer:jimmer-spring-boot-starter:${jimmer}"
        this["jimmer-ksp"] = "org.babyfish.jimmer:jimmer-ksp:${jimmer}"
        this["gateway"] = "org.springframework.cloud:spring-cloud-starter-gateway:$cloud"
        this["loadbalancer"] = "org.springframework.cloud:spring-cloud-starter-loadbalancer:$cloud"
        this["nacos"] = "com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:$nacos"
        this["sa"] = "cn.dev33:sa-token-spring-boot3-starter:$saToken"
        this["sa-reactor"] = "cn.dev33:sa-token-reactor-spring-boot3-starter:$saToken"
        this["sa-redis"] = "cn.dev33:sa-token-dao-redis-jackson:$saToken"
        this["pool"] = "org.apache.commons:commons-pool2"
        this["therapi"] = "com.github.therapi:therapi-runtime-javadoc:$therapi"
        this["therapi-ap"] = "com.github.therapi:therapi-runtime-javadoc-scribe:$therapi"
        this["config-ap"] = "org.springframework.boot:spring-boot-configuration-processor"
        this["alipay"] = "com.alipay.sdk:alipay-sdk-java:${alipay}"
        this["amqp"] = "org.springframework.boot:spring-boot-starter-amqp"
        this["cloud"] = "org.springframework.cloud:spring-cloud-dependencies:2022.0.1"
        this["cloud-alibaba"] = "com.alibaba.cloud:spring-cloud-alibaba-dependencies:2022.0.0.0-RC1"
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        kapt(project.extra["therapi-ap"]!!)
        ksp(project.extra["jimmer-ksp"]!!)

        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        developmentOnly("org.springframework.boot:spring-boot-devtools")
        runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
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
}