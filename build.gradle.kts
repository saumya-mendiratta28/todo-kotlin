plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.kotlin.kapt") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    application
}


repositories {
    mavenCentral()
}

dependencies {
    // Ktor
    val ktor_version = "2.3.7"
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // Exposed + Postgres
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jodatime:0.41.1")
    implementation("org.postgresql:postgresql:42.6.0")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.11")

    // Dagger
    implementation("com.google.dagger:dagger:2.48")
    kapt("com.google.dagger:dagger-compiler:2.48")

    testImplementation(kotlin("test"))
}

application {
    mainClass.set("org.example.MainKt")
}

kotlin {
    jvmToolchain(11)
}
