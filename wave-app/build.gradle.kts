import org.codehaus.groovy.ast.tools.GeneralUtils.args
import org.gradle.internal.Transformers.type

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

    compile("io.github.microutils:kotlin-logging:1.5.4")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    compile("com.google.dagger:dagger:2.16")
    kapt("com.google.dagger:dagger-compiler:2.16")

    compile("net.java.sezpoz:sezpoz:1.13")
}