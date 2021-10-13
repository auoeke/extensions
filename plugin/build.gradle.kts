plugins {
    id("java-gradle-plugin")
}

tasks.jar {
    from("${rootProject.rootDir}/dependencies.properties")
}

gradlePlugin {
    plugins {
        register("main") {
            id = "net.auoeke.extensions"
            implementationClass = "net.auoeke.extensions.plugin.ExtensionsPlugin"
        }
    }
}
