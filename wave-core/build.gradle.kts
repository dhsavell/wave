plugins {
    kotlin("jvm")
}

dependencies {
    compile(kotlin("stdlib-jdk8"))

    compile("com.discord4j:Discord4J:2.10.1")
    compile("org.mapdb:mapdb:3.0.7")
    compile("javax.inject:javax.inject:1")
    compile("com.xenomachina:kotlin-argparser:2.0.7")

    testImplementation("ch.qos.logback:logback-classic:1.2.3")
    testCompile("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC1")
    testCompile("io.kotlintest:kotlintest-runner-junit5:3.1.7")
}