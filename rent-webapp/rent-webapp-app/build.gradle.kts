import groovy.lang.Closure
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.springframework.boot.gradle.tasks.bundling.BootJar

val junitVersion: String by project
val mockitoVersion: String by project
val postgresVersion: String by project
val jacksonKotlinVersion: String by project
val vstCommonsVersion: String by project
val weBootStarterVersion: String by project

plugins {
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("com.palantir.git-version")
    id("com.palantir.docker")
    id("com.gorylenko.gradle-git-properties")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation(project(":rent-webapp:rent-webapp-api"))
    implementation(project(":rent-contract:rent-contract-app"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonKotlinVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")

    implementation("com.wavesplatform.we:vst-tx-observer-starter:$vstCommonsVersion")
    implementation("com.wavesplatform.we:we-platform-starter:$weBootStarterVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-inline:$mockitoVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")
    testImplementation("org.testcontainers:postgresql")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.3.1")
}

val gitVersion = (project.extensions.extraProperties.get("gitVersion") as? Closure<*>)?.call()

configure<com.gorylenko.GitPropertiesPluginExtension> {
    dateFormat = "yyyy-MM-dd'T'HH:mmZ"
    keys = listOf(
        "git.branch",
        "git.commit.id",
        "git.commit.id.abbrev",
        "git.commit.time",
        "git.tags",
        "git.closest.tag.name",
        "git.closest.tag.commit.count",
        "git.total.commit.count"
    )
}

fun getDate(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")

    return formatter.format(current)
}

val bootJar: BootJar by tasks

tasks {
    docker {
        name = "registry.weintegrator.com/rent/${project.name}"
        tags(if (version.toString().endsWith("-SNAPSHOT")) {
            "$version-${getDate()}-$gitVersion"
        } else {
            "$version-$gitVersion"
        })
        files(bootJar.get().archiveFile)
        noCache(true)

        dependsOn(bootJar.get())
    }
}
