plugins {
    id("org.jetbrains.intellij.platform.module")
    alias(libs.plugins.kotlin)
}

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        val version = providers.gradleProperty("clionVersion")
        bundledPlugins("com.intellij.clion", "com.intellij.clion.cmake")
        clion(version) {
            useInstaller = !version.get().contains("EAP")
        }
    }
}