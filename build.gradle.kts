import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.intellij.tasks.JarSearchableOptionsTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val CI = System.getenv("CI") != null
val clionVersion = "CL-${prop("clionVersion")}"
val clionPlugins = listOf("com.intellij.cidr.base", "com.intellij.clion")

plugins {
    idea
    id("org.jetbrains.grammarkit") version "2022.3.1"
    kotlin("jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.15.0"
    id("de.undercouch.download") version "5.4.0"
}

idea {
    module {
        excludeDirs = excludeDirs + file("testData") + file("deps")
    }
}

allprojects {
    apply {
        plugin("idea")
        plugin("kotlin")
        plugin("org.jetbrains.intellij")
    }

    repositories {
        mavenCentral()
    }

    idea {
        module {
            generatedSourceDirs.add(file("src/gen"))
        }
    }

    intellij {
        downloadSources.set(!CI)
        instrumentCode.set(false)
        ideaDependencyCachePath.set(file("deps").absolutePath)
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    sourceSets {
        getByName("main").java.srcDirs("src/gen")
    }
}

project(":clion") {
    intellij {
        version.set(clionVersion)
        plugins.set(clionPlugins)
        type.set("CL")
    }
    dependencies {
        implementation(project(":"))
    }
    tasks.withType<JarSearchableOptionsTask> {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    tasks.named<Zip>("buildPlugin") {
        archiveBaseName.set("fortran-plugin")
    }
}

project(":") {
    intellij {
        version.set(prop("ideaVersion"))
    }

    grammarKit {
        grammarKitRelease.set("2022.3.1")
    }

    val generateFortranLexer = task<GenerateLexerTask>("generateFortranLexer") {
        sourceFile.set(file("src/main/kotlin/org/jetbrains/fortran/lang/lexer/FortranLexer.flex"))
        targetDir.set("src/gen/org/jetbrains/fortran/lang/lexer")
        targetClass.set("_FortranLexer")
        purgeOldFiles.set(true)
    }

    val generateFortranParser = task<GenerateParserTask>("generateFortranParser") {
        dependsOn(generateFortranLexer)
        sourceFile.set(project.layout.projectDirectory.file("src/main/kotlin/org/jetbrains/fortran/lang/parser/FortranParser.bnf"))
        targetRoot.set("src/gen")
        targetRootOutputDir.set(project.layout.projectDirectory.dir(targetRoot.get()))
        pathToParser.set("/org/jetbrains/fortran/lang/parser/FortranParser.java")
        pathToPsiRoot.set("/org/jetbrains/fortran/lang/psi")
        purgeOldFiles.set(true)
    }

    tasks.withType<KotlinCompile> {
        dependsOn(generateFortranParser)
    }

    tasks.withType<Test> {
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
        }
    }

    task("resolveDependencies") {
        doLast {
            rootProject.allprojects
                .map { it.configurations }
                .flatMap { listOf(it.getByName("compile"), it.getByName("testCompile")) }
                    .forEach { it.resolve() }
        }
    }
}


fun prop(name: String): String = extra.properties[name] as? String
    ?: error("Property `$name` is not defined in gradle.properties")