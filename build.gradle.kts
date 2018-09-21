import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version "1.2.41"
    id("org.jmailen.kotlinter") version "1.17.0"
}

allprojects {
    group = "com.github.dhsavell.wave"
    version = "0.0.0"

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    repositories {
        jcenter()
        mavenCentral()
    }
}