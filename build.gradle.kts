import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.grammarkit.tasks.GenerateLexer
import org.jetbrains.grammarkit.tasks.GenerateParser
import org.jetbrains.intellij.tasks.JarSearchableOptionsTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val CI = System.getenv("CI") != null
val clionVersion = "CL-${prop("clionVersion")}"
val clionPlugins = listOf("com.intellij.cidr.base", "com.intellij.clion")

plugins {
    idea
    id("org.jetbrains.grammarkit") version "2020.3.2"
    kotlin("jvm") version "1.3.72"
    id("org.jetbrains.intellij") version "1.0"
    id("de.undercouch.download") version "4.0.0"
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
        plugin("org.jetbrains.grammarkit")
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
            jvmTarget = "1.8"
        }
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
}

project(":") {
    intellij {
        version.set(prop("ideaVersion"))
    }

    val generateFortranLexer = task<GenerateLexer>("generateFortranLexer") {
        source = "src/main/kotlin/org/jetbrains/fortran/lang/lexer/FortranLexer.flex"
        targetDir = "src/gen/org/jetbrains/fortran/lang/lexer"
        targetClass = "_FortranLexer"
        purgeOldFiles = true
    }

    val generateFortranParser = task<GenerateParser>("generateFortranParser") {
        dependsOn(generateFortranLexer)
        source = "src/main/kotlin/org/jetbrains/fortran/lang/parser/FortranParser.bnf"
        targetRoot = "src/gen"
        pathToParser = "/org/jetbrains/fortran/lang/parser/FortranParser.java"
        pathToPsiRoot = "/org/jetbrains/fortran/lang/psi"
        purgeOldFiles = true
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
                    .flatMap { listOf(it.compile, it.testCompile) }
                    .forEach { it.resolve() }
        }
    }
}


fun prop(name: String): String = extra.properties[name] as? String
    ?: error("Property `$name` is not defined in gradle.properties")