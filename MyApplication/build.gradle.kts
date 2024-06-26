// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("org.sonarqube") version "3.5.0.2730"
    id("jacoco")
}


sonar {
    properties {
        property("sonar.projectKey", "DBL-App-Dev")
        property("sonar.host.url", "http://localhost:9000")
        property ("sonar.token", "sqp_e2b43b2e19ac1bf34567e454e4692762f088ecc2")
        property("sonar.tests", "src/test/java")
        property("sonar.test.inclusions", "**/*Test*/**")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.sources", "src/main/java")
        property("sonar.exclusions", listOf(
                "**/*Test*/**",
                "build/**",
                "*.json",
                "**/*test*/**",
                "**/.gradle/**",
                "**/R.class"
        ).joinToString(", "))
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.junit.reportPaths", "**/test-results/**/*.xml")
        property("sonar.coverage.jacoco.xmlReportPaths", "C:/Users/20222057/Documents/TU-eVania/appDev/MyApplication/build/reports/jacoco/jacocoFullReport/jacocoFullReport.xml")
    }
}

// List of modules that don't require Jacoco
val ignoredByJacoco = mutableListOf<String>()
val fileFilter = mutableListOf<String>()
val buildKotlinClasses = "/tmp/kotlin-classes/debug"
val buildJavaClasses = "/intermediates/javac/debug/classes"
val testExecutionFile = "/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"

// List of files that can be ignored for test coverage
val coverageExclusions = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/res.*"
)

// Apply additional build steps to sub-projects
subprojects.forEach() { project ->
    if (!ignoredByJacoco.contains(project.name)) {
        project.pluginManager.apply("jacoco")

        project.extensions.configure(org.gradle.testing.jacoco.plugins.JacocoPluginExtension::class.java) {
            toolVersion = "0.8.7"
        }

        project.tasks.register("jacocoReport", JacocoReport::class.java) {
            group = "Reporting"
            description = "Generate Jacoco coverage reports after running tests."
            dependsOn("testDebugUnitTest")

            val buildDebug = listOf("${project.buildDir}$buildKotlinClasses", "${project.buildDir}$buildJavaClasses")
            val classDirs = project.files(buildDebug.map { classPath ->
                project.fileTree(classPath) {
//                    include(fileFilter)
                    exclude(coverageExclusions as List<String>)
                }
            })

            val mainSrc = "${project.projectDir}/src/main/java"
            sourceDirectories.from(project.files(mainSrc))
            classDirectories.from(classDirs)
            executionData.from(project.files("${project.buildDir}$testExecutionFile"))

            reports {
                xml.required.set(true)
                html.required.set(true)
            }
        }
    }
}

// Root task that generates an aggregated Jacoco test coverage report for all sub-projects
tasks.register("jacocoFullReport", JacocoReport::class.java) {
    group = "Reporting"
    description = "Generates an aggregate report from all subprojects"

    // Initialize an empty ConfigurableFileCollection
    val classDirs = project.files()

    // Filter subprojects to include only those not ignored
    val includedProjects = subprojects.filter { !ignoredByJacoco.contains(it.name) }

    // Collect class directories from included projects
    includedProjects.forEach { prj ->
        val buildDebug = listOf(
                "${prj.buildDir}$buildKotlinClasses",
                "${prj.buildDir}$buildJavaClasses"
        )

        buildDebug.forEach { classPath ->
            val fileTree = prj.fileTree(classPath) {
//                include(fileFilter)
                exclude(coverageExclusions as List<String>)
            }
            // Accumulate file trees into the classDirs collection
            classDirs.from(fileTree)
        }
    }

    val dirs = includedProjects.map { "${it.projectDir}/src/main/java" }
    sourceDirectories.setFrom(project.files(dirs))
    classDirectories.setFrom(classDirs)
    executionData.setFrom(includedProjects.flatMap { it.tasks.withType(JacocoReport::class.java).map { task -> task.executionData } })

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}



