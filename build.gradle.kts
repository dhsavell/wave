plugins {
    base
    kotlin("jvm") version "1.2.41"
}

allprojects {
    group = "com.github.dhsavell.wave"
    version = "0.0.0"

    repositories {
        jcenter()
        mavenCentral()
    }
}