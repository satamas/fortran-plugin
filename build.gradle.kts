import groovy.xml.XmlParser
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML

// Environment settings
val isCI = System.getenv("CI") != null

// Platform and IDE settings
val platformVersion = prop("platformVersion").toInt()
val baseIDE = prop("baseIDE")
val baseVersion = prop("baseVersion")

val pluginVersion = prop("pluginVersion")
val basePluginArchiveName = "fortran-plugin"


plugins {
    id("java") // Java support
    alias(libs.plugins.gradleIntelliJPlugin) // Gradle IntelliJ Plugin
    alias(libs.plugins.gradlePropertiesPlugin) // Gradle Properties Plugin
    alias(libs.plugins.kotlin) // Kotlin support
    alias(libs.plugins.grammarkit) // IntelliJ Grammarkit
    alias(libs.plugins.changelog) // Gradle Changelog Plugin
    alias(libs.plugins.qodana) // Gradle Qodana Plugin
    alias(libs.plugins.kover) // Gradle Kover Plugin
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    groups.empty()
    repositoryUrl = prop("pluginRepositoryUrl")
}

// Configure Gradle Qodana Plugin - read more: https://github.com/JetBrains/gradle-qodana-plugin
qodana {
    cachePath = provider { file(".qodana").canonicalPath }
    reportPath = provider { file("build/reports/inspections").canonicalPath }
    saveReport = true
    showReport = environment("QODANA_SHOW_REPORT").map { it.toBoolean() }.getOrElse(false)
}

// Configure Gradle Kover Plugin - read more: https://github.com/Kotlin/kotlinx-kover#configuration
koverReport {
    defaults {
        xml {
            onCheck = true
        }
    }
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
    group = prop("pluginGroup")

    repositories {
        mavenCentral()
    }

    idea {
        module {
            generatedSourceDirs.add(file("src/gen"))
        }
    }

    intellij {
        version.set(baseVersion)
        type.set(baseIDE)
        downloadSources.set(!isCI)
        updateSinceUntilBuild.set(true)
        instrumentCode.set(false)
        ideaDependencyCachePath.set(file("deps").absolutePath)
        sandboxDir.set("$buildDir/$baseIDE-sandbox-$platformVersion")
    }

    kotlin {
        jvmToolchain(17)
        sourceSets {
            main {
                kotlin.srcDirs("src/$platformVersion/main/kotlin")
            }
            test {
                kotlin.srcDirs("src/$platformVersion/test/kotlin")
            }
        }
    }

    sourceSets {
        main {
            java.srcDirs("src/gen")
            resources.srcDirs("src/$platformVersion/main/resources")
        }
        test {
            resources.srcDirs("src/$platformVersion/test/resources")
        }
    }
    tasks {
        compileKotlin {
            compilerOptions {
                // Add jvm-default=all to allow for kotlin interface with defaults
                // https://kotlinlang.org/docs/java-to-kotlin-interop.html#compatibility-modes-for-default-methods
                freeCompilerArgs.add("-Xjvm-default=all")
            }
        }
        patchPluginXml {
            sinceBuild.set(prop("sinceBuild"))
            untilBuild.set(prop("untilBuild"))
        }

        // All these tasks don't make sense for non-root subprojects
        // Root project (i.e. `:plugin`) enables them itself if needed
        runIde { enabled = false }
        prepareSandbox { enabled = false }
        buildSearchableOptions { enabled = false }
        buildPlugin { enabled = false }
    }
}

// Main project interface and metadata with run, build and publish tasks
project(":plugin") {
    version = "$pluginVersion.${prop("buildNumber")}"
    intellij {
        pluginName.set("fortran-plugin")
        val pluginList = mutableListOf<String>()
        if (type.get() == "IU") {
            pluginList += listOf(
                    prop("nativeDebugPlugin"),
            )
        }
        plugins.set(pluginList)
    }

    dependencies {
        implementation(project(":"))
        implementation(project(":clion"))
    }

    // Collects all jars produced by compilation of project modules and merges them into singe one.
    // We need to put all plugin manifest files into single jar to make new plugin model work
    val mergePluginJarTask = task<Jar>("mergePluginJars") {
        duplicatesStrategy = DuplicatesStrategy.FAIL
        archiveBaseName.set(basePluginArchiveName)

        exclude("META-INF/MANIFEST.MF")
        exclude("**/classpath.index")

        val pluginLibDir by lazy {
            val sandboxTask = tasks.prepareSandbox.get()
            sandboxTask.destinationDir.resolve("${sandboxTask.pluginName.get()}/lib")
        }

        val pluginJars by lazy {
            pluginLibDir.listFiles().orEmpty().filter { it.isPluginJar() }
        }

        destinationDirectory.set(project.layout.dir(provider { pluginLibDir }))

        doFirst {
            for (file in pluginJars) {
                from(zipTree(file))
            }
        }

        doLast {
            delete(pluginJars)
        }
    }

    // Add plugin sources to the plugin ZIP.
    // gradle-intellij-plugin will use it as a plugin sources if the plugin is used as a dependency
    val createSourceJar = task<Jar>("createSourceJar") {
        dependsOn(":generateLexer")
        dependsOn(":generateParser")

        for (prj in rootProject.allprojects) {
            from(prj.kotlin.sourceSets.main.get().kotlin) {
                include("**/*.java")
                include("**/*.kt")
            }
        }

        destinationDirectory.set(layout.buildDirectory.dir("libs"))
        archiveBaseName.set(basePluginArchiveName)
        archiveClassifier.set("src")
    }

    tasks {
        buildPlugin {
            dependsOn(createSourceJar)
            from(createSourceJar) { into("lib/src") }
            // Set proper name for final plugin zip.
            // Otherwise, base name is the same as gradle module name
            archiveBaseName.set(basePluginArchiveName)
        }
        runIde {
            dependsOn(mergePluginJarTask)
            jvmArgs("-Xmx768m", "-XX:+UseG1GC", "-XX:SoftRefLRUPolicyMSPerMB=50")
            jvmArgs("-Didea.auto.reload.plugins=false")
            jvmArgs("-Dide.show.tips.on.startup.default.value=false")
            enabled = true
        }
        prepareSandbox {
            finalizedBy(mergePluginJarTask)
            enabled = true
        }
        buildSearchableOptions {
            dependsOn(mergePluginJarTask)
            enabled = prop("enableBuildSearchableOptions").toBoolean()
        }
        verifyPlugin {
            dependsOn(mergePluginJarTask)
        }
        buildPlugin { enabled = true }
        patchPluginXml {
            val changelog = rootProject.changelog // local variable for configuration cache compatibility
            // Get the latest available change notes from the changelog file
            changeNotes = properties("pluginVersion").map { pluginVersion ->
                with(changelog) {
                    renderItem(
                            (getOrNull(pluginVersion) ?: getUnreleased())
                                    .withHeader(false)
                                    .withEmptySections(false),
                            Changelog.OutputType.HTML,
                    )
                }
            }
            pluginDescription = providers.fileContents(rootProject.layout.projectDirectory.file("README.md")).asText.map {
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

    // TODO: Add sign and publish info grabbers
}

// Main fortran language project
project(":") {
    tasks {
        generateLexer {
            sourceFile.set(file("src/main/grammars/FortranLexer.flex"))
            targetDir.set("src/gen/org/jetbrains/fortran/lang/lexer")
            targetClass.set("_FortranLexer")
            purgeOldFiles.set(true)
        }
        generateParser {
            sourceFile.set(file("src/main/grammars/FortranParser.bnf"))
            targetRoot.set("src/gen")
            pathToParser.set("org/jetbrains/fortran/lang/parser/FortranParser.java")
            pathToPsiRoot.set("org/jetbrains/fortran/lang/psi")
            purgeOldFiles.set(true)
        }
        compileKotlin {
            dependsOn(generateLexer, generateParser)
        }
    }

    task("resolveDependencies") {
        doLast {
            rootProject.allprojects
                    .map { it.configurations }
                    .flatMap { it.filter { c -> c.isCanBeResolved } }
                    .forEach { it.resolve() }
        }
    }
}

project(":clion") {
    intellij {
        type.set("CL")
        plugins.set(listOf("com.intellij.cidr.base", "com.intellij.clion"))
    }
    dependencies {
        implementation(project(":"))
        implementation(project(":debugger"))
    }
}

project(":debugger") {
    intellij {
        if (type.get() == "IU") {
            plugins.set(listOf(prop("nativeDebugPlugin")))
        } else {
            plugins.set(listOf("com.intellij.cidr.base", "com.intellij.clion"))
        }
    }

    dependencies {
        implementation(project(":"))
    }
}

fun hasProp(name: String): Boolean = extra.has(name)
fun prop(name: String): String = extra.properties[name] as? String
        ?: error("Property `$name` is not defined in gradle.properties")

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

fun File.isPluginJar(): Boolean {
    if (!isFile) return false
    if (extension != "jar") return false
    return zipTree(this).files.any { it.isManifestFile() }
}

fun File.isManifestFile(): Boolean {
    if (extension != "xml") return false
    val rootNode = try {
        val parser = XmlParser()
        parser.parse(this)
    } catch (e: Exception) {
        logger.error("Failed to parse $path", e)
        return false
    }
    return rootNode.name() == "idea-plugin"
}