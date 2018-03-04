import org.jetbrains.intellij.tasks.PublishTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import de.undercouch.gradle.tasks.download.Download
import org.gradle.api.internal.HasConvention
import org.gradle.api.tasks.SourceSet
import org.jetbrains.grammarkit.GrammarKitPluginExtension
import org.jetbrains.grammarkit.tasks.GenerateLexer
import org.jetbrains.grammarkit.tasks.GenerateParser
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.gradle.api.JavaVersion.VERSION_1_8
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.jvm.tasks.Jar
import org.jetbrains.intellij.tasks.PrepareSandboxTask
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Path
import kotlin.concurrent.thread

buildscript {
    repositories {
        maven { setUrl("https://jitpack.io") }
    }
    dependencies {
        classpath("com.github.hurricup:gradle-grammar-kit-plugin:2017.1.1")
    }
}


val CI = System.getenv("CI") != null

plugins {
    idea
    kotlin("jvm") version "1.1.4"
    id("org.jetbrains.intellij") version "0.2.17"
    id("de.undercouch.download") version "3.2.0"
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
        version = prop("ideaVersion")
        downloadSources = !CI
        updateSinceUntilBuild = false
        instrumentCode = false
        ideaDependencyCachePath = file("deps").absolutePath
    }

    configure<GrammarKitPluginExtension> {
        grammarKitRelease = "1.5.2"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            languageVersion = "1.1"
            apiVersion = "1.1"
        }
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = VERSION_1_8
        targetCompatibility = VERSION_1_8
    }

    java.sourceSets {
        getByName("main").java.srcDirs("src/gen")
    }
}

project(":") {
    val clionVersion = prop("clionVersion")
    intellij {
        pluginName = "fortran-plugin"
        alternativeIdePath = "debugger/lib/clion-$clionVersion"
    }

    java.sourceSets {
        create("debugger") {
            kotlin.srcDirs("debugger/src/main/kotlin")
            compileClasspath += getByName("main").compileClasspath +
                    getByName("main").output +
                    files("debugger/lib/clion-$clionVersion/lib/clion.jar")
        }
    }

    tasks.withType<Jar> {
        from(java.sourceSets.getByName("debugger").output)
    }

    val generateFortranLexer = task<GenerateLexer>("generateFortranLexer") {
        source = "src/main/kotlin/org/jetbrains/fortran/lang/lexer/FortranLexer.flex"
        targetDir = "src/gen/org/jetbrains/fortran/lang/lexer"
        targetClass = "_FortranLexer"
        purgeOldFiles = true
    }


    val generateFortranParser = task<GenerateParser>("generateFortranParser") {
        source = "src/main/kotlin/org/jetbrains/fortran/lang/parser/FortranParser.bnf"
        targetRoot = "src/gen"
        pathToParser = "/org/jetbrains/fortran/lang/parser/FortranParser.java"
        pathToPsiRoot = "/org/jetbrains/fortran/lang/psi"
        purgeOldFiles = true
    }

    val isWindows = System.getProperty("os.name").toLowerCase().indexOf( "win" ) >= 0

    val downloadClion = task<Download>("downloadClion") {
        if (isWindows) {
            onlyIf { !file("${project.projectDir}/debugger/lib/clion-$clionVersion.zip").exists() }
            src("https://download.jetbrains.com/cpp/CLion-$clionVersion.zip")
            dest(file("${project.projectDir}/debugger/lib/clion-$clionVersion.zip"))
        } else {
            onlyIf { !file("${project.projectDir}/debugger/lib/clion-$clionVersion.tar.gz").exists() }
            src("https://download.jetbrains.com/cpp/CLion-$clionVersion.tar.gz")
            dest(file("${project.projectDir}/debugger/lib/clion-$clionVersion.tar.gz"))
        }
    }

    val unpackClion = task<Copy>("unpackClion") {
        if (isWindows) {
            onlyIf { !file("${project.projectDir}/debugger/lib/clion-$clionVersion").exists() }
            from(zipTree("debugger/lib/clion-$clionVersion.zip"))
            into(file("${project.projectDir}/debugger/lib/clion-$clionVersion"))
        } else {
            onlyIf { !file("${project.projectDir}/debugger/lib/clion-$clionVersion").exists() }
            from(tarTree("debugger/lib/clion-$clionVersion.tar.gz"))
            into(file("${project.projectDir}/debugger/lib/"))
        }
        dependsOn(downloadClion)
    }

    tasks.withType<KotlinCompile> {
        dependsOn(
                generateFortranLexer, generateFortranParser, unpackClion
        )
    }

    tasks.withType<Test> {
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
        }
    }

    task("resolveDependencies") {
        dependsOn(unpackClion)
        doLast {
            rootProject.allprojects
                    .map { it.configurations }
                    .flatMap { listOf(it.compile, it.testCompile) }
                    .forEach { it.resolve() }
        }
    }
}



fun prop(name: String): String =
        extra.properties[name] as? String
                ?: error("Property `$name` is not defined in gradle.properties")


inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)

val SourceSet.kotlin: SourceDirectorySet
    get() =
        (this as HasConvention)
                .convention
                .getPlugin(KotlinSourceSet::class.java)
                .kotlin


fun SourceSet.kotlin(action: SourceDirectorySet.() -> Unit) =
        kotlin.action()


fun String.execute(wd: String? = null, ignoreExitCode: Boolean = false): String =
        split(" ").execute(wd, ignoreExitCode)

fun List<String>.execute(wd: String? = null, ignoreExitCode: Boolean = false): String {
    val process = ProcessBuilder(this)
            .also { pb -> wd?.let { pb.directory(File(it)) } }
            .start()
    var result = ""
    val errReader = thread { process.errorStream.bufferedReader().forEachLine { println(it) } }
    val outReader = thread {
        process.inputStream.bufferedReader().forEachLine { line ->
            println(line)
            result += line
        }
    }
    process.waitFor()
    outReader.join()
    errReader.join()
    if (process.exitValue() != 0 && !ignoreExitCode) error("Non-zero exit status for `$this`")
    return result
}