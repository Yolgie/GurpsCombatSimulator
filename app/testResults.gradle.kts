import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import groovy.time.TimeCategory
import java.util.Date

/**
 * source gradle_tests_report.gradle.kts by ethanmdavidson:
 * https://gist.github.com/ethanmdavidson/a73147ce5bdcde4a87554c7303bae8f4
 * based on the groovy code by lwasyl:
 * https://gist.github.com/lwasyl/f5b2b4ebe9e348ebbd8ee4cb995f8362
 */
var testResults by extra(mutableListOf<TestOutcome>()) // Container for tests summaries

tasks.withType<Test>().configureEach {
    val testTask = this

    testLogging {
        events = setOf(
            TestLogEvent.FAILED,
            TestLogEvent.SKIPPED,
            TestLogEvent.STANDARD_OUT,
            TestLogEvent.STANDARD_ERROR
        )

        exceptionFormat = TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }

    ignoreFailures = true // Always try to run all tests for all modules

    //addTestListener is a workaround https://github.com/gradle/kotlin-dsl-samples/issues/836
    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {}
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}
        override fun afterSuite(desc: TestDescriptor, result: TestResult) {
            if (desc.parent != null) return // Only summarize results for whole modules

            val summary = TestOutcome().apply {
                add( "${testTask.project.name}:${testTask.name} results: ${result.resultType} " +
                        "(" +
                        "${result.testCount} tests, " +
                        "${result.successfulTestCount} successes, " +
                        "${result.failedTestCount} failures, " +
                        "${result.skippedTestCount} skipped" +
                        ") " +
                        "in ${TimeCategory.minus(Date(result.endTime), Date(result.startTime))}")
                add("Report file: ${testTask.reports.html.entryPoint}")
            }

            // Add reports in `testsResults`, keep failed suites at the end
            if (result.resultType == TestResult.ResultType.SUCCESS) {
                testResults.add(0, summary)
            } else {
                testResults.add(summary)
            }
        }
    })
}

gradle.buildFinished {
    if (testResults.isNotEmpty()) {
        printResults(testResults)
    }
}

fun printResults(allResults:List<TestOutcome>) {
    val maxLength = allResults.map{ it.maxWidth() }
        .max() ?: 0

    println("┌${"─".repeat(maxLength)}┐")

    println(allResults.joinToString("├${"─".repeat(maxLength)}┤\n") { testOutcome ->
        testOutcome.lines.joinToString("│\n│", "│", "│") {
            it + " ".repeat(maxLength - it.length)
        }
    })

    println("└${"─".repeat(maxLength)}┘")
}

data class TestOutcome(val lines:MutableList<String> = mutableListOf()) {
    fun add(line:String) {
        lines.add(line)
    }

    fun maxWidth(): Int {
        return lines.maxBy { it.length }?.length ?: 0
    }
}
