plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(kotlin("stdlib"))

    api(project(":rent-domain"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}
