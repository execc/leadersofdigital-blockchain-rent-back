val mavenUser: String by project
val mavenPassword: String by project

plugins {
    `maven-publish`
}

dependencies {
    implementation(kotlin("stdlib"))
    api("com.wavesplatform.we:vst-contract-common")
    api(project(":rent-domain"))
}

publishing {
    repositories {
        maven {
            name = "maven"
            url = uri("https://artifacts.wavesenterprise.com/repository/${if (project.version.toString().endsWith("-SNAPSHOT")) "maven-snapshots" else "maven-releases"}")
            credentials {
                username = mavenUser
                password = mavenPassword
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
