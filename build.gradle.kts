plugins {
    base
    kotlin("jvm") version "1.2.41"
    id("org.jmailen.kotlinter") version "1.17.0"
}

allprojects {
    group = "com.github.dhsavell.wave"
    version = "0.0.0"

    repositories {
        jcenter()
        mavenCentral()
    }
}