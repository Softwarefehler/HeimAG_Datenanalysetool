
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val coroutines_version: String by project
val koin_version: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.11"
    kotlin("plugin.serialization") version "2.0.0"
}

group = "ch.heimag.datenanalysetool"
version = "0.0.1"

application {
    mainClass.set("ch.heimag.datenanalysetool.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-freemarker:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-xml:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutines_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-http-redirect:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-html-builder:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-forwarded-header:$ktor_version")
    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")
    implementation("com.mysql:mysql-connector-j:8.2.0")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version")
}

data class CommandLineConfig(val cmd: String, val windowsCmd: String? = null)

fun getPid(): Long {
    return ProcessHandle.current().pid()
}

fun Exec.runCommandLine(commandLineConfig: CommandLineConfig) {
    val isWindows = System.getProperty("os.name").lowercase().contains("windows")

    if (isWindows) {
        commandLine("cmd.exe", "/C", commandLineConfig.windowsCmd ?: commandLineConfig.cmd)
    } else {
        commandLine(commandLineConfig.cmd.splitToSequence(' ').toList())
    }
}

fun Exec.runCommandLine(vararg arguments: String) {
    runCommandLine(CommandLineConfig(arguments.joinToString(" ")))
}

fun Exec.runNpmCommand(vararg npmArguments: String) {
    workingDir = File("src/main/vue-project")

    runCommandLine("npm " + npmArguments.joinToString(" "))
}

tasks.register<Exec>("install-vue") {
    group = "build setup"
    description = "installs all the npm packages for the the vue-project."

    runNpmCommand("ci")
}

tasks.register<Exec>("build-vue") {
    group = "build"
    description = "builds the vue-project."
    runNpmCommand("run", "build")
}

tasks.register<Exec>("run-vue-watch") {
    group = "application"
    description = "builds the vue-project continuously."

    // The PID of the current jvm instance for the 'syncLifetimeToPidPlugin' in the vue-project.
    // -> See reasoning within the plugin.
    environment("PID", getPid())
    runNpmCommand("run", "build-watch")
}
