import org.jetbrains.kotlin.gradle.tasks.*
import org.jetbrains.kotlin.konan.properties.*
import org.jetbrains.kotlin.utils.addToStdlib.*

plugins {
    id("maven-publish")
    kotlin("jvm") version "latest.release"
}

val Project.isModule: Boolean get() = projectDir.relativeTo(rootProject.rootDir).startsWith("modules")

allprojects {
    plugins.apply {
        apply("maven-publish")
        apply("org.jetbrains.kotlin.jvm")
    }

    sourceSets {
        main {
            java.srcDir("src")
        }

        test {
            java.srcDir("test")
        }
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:latest.release")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    }

    "16".let {version ->
        java {
            sourceCompatibility = JavaVersion.toVersion(version).also {targetCompatibility = it}
            withSourcesJar()
        }

        tasks.withType<KotlinCompile> {
            kotlinOptions.jvmTarget = version
        }
    }

    tasks.test {
        useJUnitPlatform()
    }

    publishing {
        repositories {
            maven {
                url = uri("https://maven.auoeke.net")

                credentials {
                    username = System.getProperty("maven.username")
                    password = System.getProperty("maven.password")
                }
            }
        }

        if (project == rootProject || project.isModule) {
            publications {
                register("main", MavenPublication::class) {
                    from(components["java"])
                }
            }
        }
    }
}

// @formatter:off
subprojects.filter {it.isModule}.forEach {it.run {
    tasks.withType<Jar> {
        archiveBaseName.set("extensions-${project.name}")
    }
}}
// @formatter:on

subprojects {
    dependencies {
        api(rootProject)
    }
}

loadProperties("dependencies.properties").cast<Map<String, String>>().forEach {
    project(it.key).dependencies {
        it.value.split(" ").forEach {dependency -> compileOnly("$dependency:latest.release")}
    }
}
