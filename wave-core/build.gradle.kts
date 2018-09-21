plugins {
    kotlin("jvm")
}

dependencies {
    compile(kotlin("stdlib-jdk8"))

    compile("com.discord4j:Discord4J:2.10.1")
    compile("org.dizitart:potassium-nitrite:3.1.0")
    compile("javax.inject:javax.inject:1")
    compile("com.xenomachina:kotlin-argparser:2.0.7")

    testImplementation("ch.qos.logback:logback-classic:1.2.3")
    testImplementation("io.mockk:mockk:1.8.7")
    testCompile("io.kotlintest:kotlintest-runner-junit5:3.1.7")
}