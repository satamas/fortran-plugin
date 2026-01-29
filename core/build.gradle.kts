import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.intellij.platform.module")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.grammarkit)
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
        val version = providers.gradleProperty("baseVersion")
        intellijIdea(version) {
            useInstaller = !version.get().contains("EAP")
        }
        testFramework(TestFrameworkType.Platform)
    }

    testImplementation(libs.junit)
    testImplementation(libs.test4j)
}

tasks {
    generateLexer {
        sourceFile.set(file("src/main/grammars/FortranLexer.flex"))
        targetOutputDir.set(layout.projectDirectory.dir("src/gen/org/jetbrains/fortran/lang/lexer"))
        purgeOldFiles.set(true)
    }
    generateParser {
        sourceFile.set(file("src/main/grammars/FortranParser.bnf"))
        targetRootOutputDir.set(layout.projectDirectory.dir("src/gen"))
        pathToParser.set("org/jetbrains/fortran/lang/parser/FortranParser.java")
        pathToPsiRoot.set("org/jetbrains/fortran/lang/psi")
        purgeOldFiles.set(true)
    }
    compileKotlin {
        dependsOn(generateLexer, generateParser)

        compilerOptions {
            // Add jvm-default=all to allow for kotlin interface with defaults
            // https://kotlinlang.org/docs/java-to-kotlin-interop.html#compatibility-modes-for-default-methods
            freeCompilerArgs.add("-Xjvm-default=all")
        }
    }
}

sourceSets {
    main {
        java.srcDirs("src/gen")
    }
}

idea {
    module {
        generatedSourceDirs.add(file("src/gen"))
        excludeDirs = excludeDirs + file("testData") + file("deps")
    }
}