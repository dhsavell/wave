import com.beust.kobalt.api.Project
import com.beust.kobalt.buildScript
import com.beust.kobalt.plugin.application.application
import com.beust.kobalt.plugin.apt.apt
import com.beust.kobalt.plugin.packaging.assemble
import com.beust.kobalt.project

object Versions {
    const val WAVE = "0.0.0"

    const val KOTLIN = "1.2.50"
    const val SEZPOZ = "1.13"
    const val DISCORD4J = "2.10.1"
    const val KEDIS = "1.1"

    const val MOCKK = "1.8.5"
    const val JUNIT = "5.2.0"
}


val buildscript = buildScript {
    repos("https://jitpack.io")
}

fun Project.waveModule(moduleName: String) {
    name = "wave-$moduleName"
    group = "com.github.dhsavell.wave"
    artifactId = name
    version = Versions.WAVE
    directory = "./wave-$moduleName"

    sourceDirectories {
        path("src", "res")
    }

    dependencies {
        compile("org.jetbrains.kotlin:kotlin-runtime:${Versions.KOTLIN}")
        compile("org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}")
    }
}

val waveCore = project {
    waveModule("core")

    sourceDirectoriesTest {
        path("test")
    }

    dependencies {
        compile("net.java.sezpoz:sezpoz:${Versions.SEZPOZ}")
        compile("com.discord4j:Discord4J:jar:${Versions.DISCORD4J}")
        compile("com.sxtanna.database:Kedis:${Versions.KEDIS}")
    }

    dependenciesTest {
        compile("io.mockk:mockk-common:${Versions.MOCKK}")
        compile("org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT}")
        runtime("org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT}")
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
