import org.gradle.api.internal.HasConvention
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.grammarkit.tasks.GenerateLexer
import org.jetbrains.grammarkit.tasks.GenerateParser
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.concurrent.thread

val CI = System.getenv("CI") != null
val clionVersion = "CL-${prop("clionVersion")}"
val clionPlugins = listOf("com.intellij.cidr.base", "com.intellij.clion")

plugins {
    idea
    id("org.jetbrains.grammarkit") version "2020.2.1"
    kotlin("jvm") version "1.3.72"
    id("org.jetbrains.intellij") version "0.4.21"
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
        downloadSources = !CI
        instrumentCode = false
        ideaDependencyCachePath = file("deps").absolutePath
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
        version = clionVersion
        setPlugins(*clionPlugins.toTypedArray())
        type = "CL"
    }
    dependencies {
        implementation(project(":"))
    }
}

project(":") {
    intellij {
        version = prop("ideaVersion")
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

project(":plugin") {
    intellij {
        version = clionVersion
        updateSinceUntilBuild = false
        pluginName = "fortran-plugin"
    }

    dependencies {
        implementation(project(":"))
        implementation(project(":clion"))
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
