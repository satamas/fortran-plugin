import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML

// Environment settings
val isCI = System.getenv("CI") != null


plugins {
    id("java") // Java support
    alias(libs.plugins.gradleIntelliJPlugin) // Gradle IntelliJ Plugin
    alias(libs.plugins.kotlin) // Kotlin support
    alias(libs.plugins.changelog) // Gradle Changelog Plugin
    alias(libs.plugins.qodana) // Gradle Qodana Plugin
    alias(libs.plugins.kover) // Gradle Kover Plugin
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        val baseIDE = providers.gradleProperty("baseIDE").get()
        when (baseIDE) {
            "IC" -> {
                val version = providers.gradleProperty("baseVersion")
                intellijIdeaCommunity(version, !version.get().contains("EAP"))
            }

            "CL" -> {
                val version = providers.gradleProperty("clionVersion")
                clion(version, !version.get().contains("EAP"))
            }

            else -> throw GradleException("Unexpected IDE type: $baseIDE")
        }
        instrumentationTools()
        pluginModule(implementation(project(":core")))
        pluginModule(runtimeOnly(project(":clion")))
        pluginModule(runtimeOnly(project(":debugger")))
    }
}

kotlin {
    jvmToolchain(21)
}

intellijPlatform {
    projectName = "fortran-plugin"
    pluginConfiguration {
        val pluginVersion = providers.gradleProperty("pluginVersion").get() + "." +
                providers.gradleProperty("buildNumber").get()

        id = "org.jetbrains.fortran"
        name = "Fortran"
        vendor {
            name = "Semyon Atamas"
            email = "semyon.atamas@jetbrains.com"
            url = "https://github.com/satamas/fortran-plugin"
        }
        version = pluginVersion

        val changelog = rootProject.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
        changeNotes = with(changelog) {
            renderItem(
                (getOrNull(pluginVersion) ?: getUnreleased())
                    .withHeader(false)
                    .withEmptySections(false),
                Changelog.OutputType.PLAIN_TEXT,
            )
        }
        description = providers.fileContents(rootProject.layout.projectDirectory.file("README.md")).asText.map {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            with(it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        }
    }
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    groups.empty()
    repositoryUrl = providers.gradleProperty("pluginRepositoryUrl")
}

// Configure Gradle Qodana Plugin - read more: https://github.com/JetBrains/gradle-qodana-plugin
qodana {
    cachePath = provider { file(".qodana").canonicalPath }
    reportPath = provider { file("build/reports/inspections").canonicalPath }
    saveReport = true
    showReport = providers.environmentVariable("QODANA_SHOW_REPORT").map { it.toBoolean() }.getOrElse(false)
}

// Configure Gradle Kover Plugin - read more: https://github.com/Kotlin/kotlinx-kover#configuration
kover {
    reports {
        total {
            xml {
                onCheck = true
            }
        }
    }
}