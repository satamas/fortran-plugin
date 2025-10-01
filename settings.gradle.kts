rootProject.name = "fortran-plugin"

// Special module with run, build and publish tasks
include(
    "clion",
    "debugger",
    "core"
)

// Configure Gradle Build Cache. It is enabled in `gradle.properties` via `org.gradle.caching`.
buildCache {
    local {
        isEnabled = System.getenv("CI") == null
        directory = File(rootDir, "build/build-cache")
    }
}

pluginManagement {
    repositories {
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        gradlePluginPortal()
    }
}
