import org.codehaus.groovy.ast.tools.GeneralUtils.args
import org.gradle.internal.Transformers.type

plugins {
    application
    kotlin("jvm")
}

application {
    mainClassName = "com.github.dhsavell.wave.app.LauncherKt"
}

dependencies {
    compile(project(":wave-core"))
    compile(kotlin("stdlib"))

    implementation("org.kodein.di:kodein-di-generic-jvm:5.2.0")
    compile("io.github.microutils:kotlin-logging:1.5.4")
    compile("net.java.sezpoz:sezpoz:1.13")
}