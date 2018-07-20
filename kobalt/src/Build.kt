import com.beust.kobalt.api.Project
import com.beust.kobalt.plugin.application.application
import com.beust.kobalt.plugin.apt.apt
import com.beust.kobalt.plugin.packaging.assemble
import com.beust.kobalt.project

object Versions {
    const val WAVE = "0.0.0"
    const val KOTLIN = "1.2.50"

    const val DISCORD4J = "2.10.1"
    const val MAPDB = "3.0.7"

    const val MOCKITO_KOTLIN = "2.0.0-RC1"
    const val KOTLINTEST = "3.1.7"

    const val SEZPOZ = "1.13"
    const val KODEIN = "5.2.0"
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
        compile("org.jetbrains.kotlin:kotlin-runtime:${Versions.KOTLIN}",
                "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}",
                "com.discord4j:Discord4J:jar:${Versions.DISCORD4J}",
                "org.mapdb:mapdb:${Versions.MAPDB}")
    }
}

val waveCore = project {
    waveModule("core")

    sourceDirectoriesTest {
        path("test")
    }

    dependenciesTest {
        compile("com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.MOCKITO_KOTLIN}",
                "io.kotlintest:kotlintest-runner-junit5:${Versions.KOTLINTEST}")
    }
}

val waveApp = project(waveCore) {
    waveModule("app")

    dependencies {
        apt("net.java.sezpoz:sezpoz:${Versions.SEZPOZ}")
        compile("net.java.sezpoz:sezpoz:${Versions.SEZPOZ}")
        compile("org.kodein.di:kodein-di-generic-jvm:${Versions.KODEIN}")
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
