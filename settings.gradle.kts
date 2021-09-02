pluginManagement {
    val mavenUser: String by settings
    val mavenPassword: String by settings

    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val gradleDependencyManagementVersion: String by settings
    val detektVersion: String by settings
    val gitPropertiesVersion: String by settings
    val vstContractDockerPluginVersion: String by settings
    val palantirGitVersion: String by settings
    val jGitVerVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.spring") version kotlinVersion apply false
        kotlin("plugin.jpa") version kotlinVersion apply false
        `maven-publish`
        id("org.springframework.boot") version springBootVersion apply false
        id("io.spring.dependency-management") version gradleDependencyManagementVersion apply false
        id("io.gitlab.arturbosch.detekt") version detektVersion apply false
        id("com.palantir.git-version") version palantirGitVersion apply false
        id("com.gorylenko.gradle-git-properties") version gitPropertiesVersion apply false
        id("jacoco")
        id("com.wavesplatform.vst.contract-docker") version vstContractDockerPluginVersion apply false
        id("fr.brouillard.oss.gradle.jgitver") version jGitVerVersion
    }

    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven {
            name = "maven-snapshots"
            url = uri("https://artifacts.wavesenterprise.com/repository/maven-snapshots/")
            mavenContent {
                snapshotsOnly()
            }
            credentials {
                username = mavenUser
                password = mavenPassword
            }
        }
        maven {
            name = "maven-releases"
            url = uri("https://artifacts.wavesenterprise.com/repository/maven-releases/")
            mavenContent {
                releasesOnly()
            }
            credentials {
                username = mavenUser
                password = mavenPassword
            }
        }
        mavenLocal()
    }
}

rootProject.name = "rent-app"

include(
    "rent-webapp:rent-webapp-api",
    "rent-webapp:rent-webapp-app",
    "rent-contract:rent-contract-api",
    "rent-contract:rent-contract-app",
    "rent-domain"
)