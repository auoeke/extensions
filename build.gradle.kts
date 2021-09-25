plugins {
    id("maven-publish")
    kotlin("jvm") version "latest.release"
}

group = "net.auoeke"
version = "0.11.0"

sourceSets {
    main {
        java.srcDir("src")
    }

    test {
        java.srcDir("test")
    }
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = targetCompatibility
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:latest.release")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

java {
    withSourcesJar()
}

tasks.named<Test>("test") {
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
