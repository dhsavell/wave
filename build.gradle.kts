import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version "1.2.41"

    id("jacoco")
    id("com.github.kt3k.coveralls") version "2.6.3"
    id("org.jmailen.kotlinter") version "1.17.0"
}

allprojects {
    group = "com.github.dhsavell.wave"
    version = "0.0.0"

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.withType<JacocoReport> {
        reports {
            html.isEnabled = true
            xml.isEnabled = true
        }
    }

    repositories {
        jcenter()
        mavenCentral()
    }
}