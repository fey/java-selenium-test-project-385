plugins {
    id("java")
}

group = "dev.selenium"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val seleniumVersion: String = System.getProperty("selenium.version", "4.40.0")

dependencies {
    testImplementation("org.seleniumhq.selenium:selenium-java:$seleniumVersion")
    testImplementation("org.seleniumhq.selenium:selenium-grid:$seleniumVersion")

    testImplementation(platform("org.junit:junit-bom:6.0.2"))
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("com.titusfortner:selenium-logger:2.4.0")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "failed", "skipped")
        showStandardStreams = false
        showExceptions = true
        showCauses = true
        showStackTraces = false
    }

    addTestListener(object : TestListener {

        override fun beforeSuite(suite: TestDescriptor) {}

        override fun afterSuite(suite: TestDescriptor, result: TestResult) {}

        override fun beforeTest(testDescriptor: TestDescriptor) {}

        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
            // игнорируем suite-узлы
            if (testDescriptor.parent != null) {
                val symbol = when (result.resultType) {
                    TestResult.ResultType.SUCCESS -> "✔"
                    TestResult.ResultType.FAILURE -> "✘"
                    TestResult.ResultType.SKIPPED -> "○"
                    else -> "?"
                }

                println("$symbol ${testDescriptor.className} > ${testDescriptor.name}")
            }
        }
    })
}
