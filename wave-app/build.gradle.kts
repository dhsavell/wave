plugins {
    application
    kotlin("jvm")
}

application {
    mainClassName = "com.github.dhsavell.wave.Launcher"
}

dependencies {
    compile(project(":wave-core"))
    compile(kotlin("stdlib"))
}