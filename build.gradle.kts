import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import kotlin.io.path.Path

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("multiplatform") version "1.9.22"
    id("io.ktor.plugin") version "2.3.7"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

group = "me.tomasan7"
version = "0.0.1"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        jvmToolchain(17)
    }

    js {
        binaries.executable()
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
                implementation(kotlin("reflect"))
            }
        }

        jvmMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.8.0-RC2")

                implementation("io.ktor:ktor-server-default-headers:$ktor_version")
                implementation("io.ktor:ktor-server-forwarded-header:$ktor_version")
                implementation("io.ktor:ktor-server-call-logging:$ktor_version")
                implementation("io.ktor:ktor-server-core:$ktor_version")
                implementation("io.ktor:ktor-server-websockets:$ktor_version")
                implementation("io.ktor:ktor-server-freemarker:$ktor_version")
                implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
                implementation("io.ktor:ktor-server-sessions:$ktor_version")
                implementation("io.ktor:ktor-server-netty:$ktor_version")
                implementation("ch.qos.logback:logback-classic:$logback_version")

                runtimeOnly("org.slf4j:slf4j-api:2.0.11")
                implementation("org.fusesource.jansi:jansi:2.3.2")
                implementation("ch.qos.logback:logback-classic:1.4.14")
                implementation("org.reflections:reflections:0.10.2")
            }
        }

        jvmTest {
            dependencies {
                implementation("io.ktor:ktor-server-tests-jvm")
                implementation(kotlin("test"))
            }
        }
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

    val compileFrontend by registering(Copy::class) {
        val outputResourcePath = Path("frontend/TicTacToe.js")

        val jsBrowserDistTask = getByName<KotlinWebpack>("jsBrowserDevelopmentWebpack")
        dependsOn(jsBrowserDistTask)
        from(jsBrowserDistTask.mainOutputFile) {
            rename { outputResourcePath.fileName.toString() }
        }
        val jvmProcessResourcesTask = getByName<ProcessResources>("jvmProcessResources")
        into(jvmProcessResourcesTask.destinationDir.resolve(outputResourcePath.parent.toString()))
    }

    named("compileKotlinJvm") {
        dependsOn(compileFrontend)
    }
}

application {
    mainClass.set("me.tomasan7.tttweb.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}
