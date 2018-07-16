import com.beust.kobalt.api.Project
import com.beust.kobalt.buildScript
import com.beust.kobalt.plugin.application.application
import com.beust.kobalt.plugin.apt.apt
import com.beust.kobalt.plugin.packaging.assemble
import com.beust.kobalt.project

object Versions {
    const val WAVE = "0.0.0"
    const val KOTLIN = "1.2.50"

    const val DISCORD4J = "2.10.1"
    const val KEDIS = "1.1"

    const val MOCKK = "1.8.5"
    const val KOTLINTEST = "3.1.7"

    const val DAGGER = "2.5"
    const val SEZPOZ = "1.13"
}


val buildscript = buildScript {
    repos("http://central.maven.org/maven2/")
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
        compile("com.discord4j:Discord4J:jar:${Versions.DISCORD4J}")
        compile("com.sxtanna.database:Kedis:${Versions.KEDIS}")
    }

    dependenciesTest {
        compile("io.mockk:mockk-common:${Versions.MOCKK}")
        compile("io.kotlintest:kotlintest-runner-junit5:${Versions.KOTLINTEST}")
    }
}

val waveApp = project(waveCore) {
    waveModule("app")

    dependencies {
        compile("com.google.dagger:dagger:${Versions.DAGGER}")
        apt("com.google.dagger:dagger-compiler:${Versions.DAGGER}")

        compile("net.java.sezpoz:sezpoz:${Versions.SEZPOZ}")
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
