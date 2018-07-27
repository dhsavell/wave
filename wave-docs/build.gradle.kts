import org.codehaus.groovy.ast.tools.GeneralUtils.args
import org.gradle.internal.Transformers.type

plugins {
    kotlin("jvm")
    application
    idea
}

application {
    mainClassName = "com.github.dhsavell.wave.docs.MainKt"
}

dependencies {
    compile(project(":wave-app"))
    compile(kotlin("stdlib-jdk8"))

    compile("io.github.microutils:kotlin-logging:1.5.4")
    implementation("ch.qos.logback:logback-classic:1.2.3")
}