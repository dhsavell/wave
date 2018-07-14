import com.beust.kobalt.api.Project
import com.beust.kobalt.buildScript
import com.beust.kobalt.plugin.application.application
import com.beust.kobalt.plugin.apt.apt
import com.beust.kobalt.plugin.packaging.assemble
import com.beust.kobalt.project

const val VERSION = "0.0.0"

val buildscript = buildScript {
    repos("https://jitpack.io")
}

fun Project.waveModule(moduleName: String) {
    name = "wave-$moduleName"
    group = "com.github.dhsavell.wave"
    artifactId = name
    version = VERSION
    directory = "./wave-$moduleName"

    sourceDirectories {
        path("src", "res")
    }

    sourceDirectoriesTest {
        path("test")
    }

    dependencies {
        compile("org.jetbrains.kotlin:kotlin-runtime:1.2.10",
                "org.jetbrains.kotlin:kotlin-stdlib:1.2.10")
    }
}

val waveCore = project {
    waveModule("core")

    dependencies {
        compile("net.java.sezpoz:sezpoz:1.13")
        compile("com.discord4j:Discord4J:jar:2.10.1")
        compile("com.sxtanna.database:Kedis:1.1")
    }

    dependenciesTest {
        compile("io.mockk:mockk-common:1.8.5")
        compile("org.jetbrains.spek:spek-api:1.1.5")
        runtime("org.jetbrains.spek:spek-junit-platform-engine:1.1.5")
    }

    apt {

    }
}

val waveApp = project(waveCore) {
    waveModule("app")

    dependencies {

    }

    assemble {
        jar {
            fatJar = true
        }
    }

    application {
        mainClass = "com.github.dhsavell.wave.app.LauncherKt"
    }
}
