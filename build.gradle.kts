plugins {
    id("maven-publish")
    kotlin("jvm") version "latest.release"
}

group = "net.auoeke"
version = "0.7.0"

sourceSets {
    main {
        java.srcDir("src")
    }
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = targetCompatibility
}

repositories {
    mavenCentral()
}

java {
    withSourcesJar()
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
