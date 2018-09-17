plugins {
    kotlin("jvm")
    kotlin("kapt")
    application
    idea
}

application {
    mainClassName = "com.github.dhsavell.wave.app.MainKt"
}

dependencies {
    compile(project(":wave-core"))
    compile(kotlin("stdlib-jdk8"))

    compile("org.kodein.di:kodein-di-generic-jvm:5.2.0")
    compile("net.java.sezpoz:sezpoz:1.13")

    compile("io.github.microutils:kotlin-logging:1.5.4")
    implementation("ch.qos.logback:logback-classic:1.2.3")
}