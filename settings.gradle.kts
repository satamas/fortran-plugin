rootProject.name = "fortran-plugin"

// Special module with run, build and publish tasks
include("plugin")
include(
    "idea",
    "clion",
)

// Configure Gradle Build Cache. It is enabled in `gradle.properties` via `org.gradle.caching`.
buildCache {
    local {
        isEnabled = System.getenv("CI") == null
        directory = File(rootDir, "build/build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}

pluginManagement {
    repositories {
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        gradlePluginPortal()
    }
}
