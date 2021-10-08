plugins {
    id("maven-publish")
    kotlin("jvm") version "latest.release"
}

group = "net.auoeke"
version = "0.12.0"

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

java {
    sourceCompatibility = JavaVersion.VERSION_16.also {targetCompatibility = it}

    withSourcesJar()
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = targetCompatibility
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

    publications {
        register("main", MavenPublication::class) {
            from(components["java"])
        }
    }
}
