package net.auoeke.extensions.plugin

import net.auoeke.extensions.*
import org.gradle.api.*

@Suppress("UNCHECKED_CAST", "unused")
class ExtensionsPlugin : Plugin<Project> {
    private val Project.extension get(): Extension = extensions.getByType(type<Extension>())

    override fun apply(project: Project) {
        project.configure()

        project.gradle.projectsEvaluated {
            project.afterEvaluation()
        }
    }

    private fun Project.configure() {
        extensions.create("kextensions", type<Extension>())
    }

    private fun Project.afterEvaluation() {
        add("extensions")

        modules.withEach {
            val dependencies = value.split(" ").toHashSet()

            configurations.getByName("compileClasspath").allDependencies.all {
                if (dependencies.remove("${it.group}:${it.name}") && dependencies.isEmpty()) {
                    add(key)
                }
            }
        }
    }

    private fun Project.add(module: String) = extension.configurations.each {
        dependencies.add(it, "${coordinates["group"]}:$module:${coordinates["version"]}")
    }

    private companion object {
        val coordinates = Properties(type.localResource("/gradle.properties")!!)
        val modules = Properties(type.localResource("/dependencies.properties")!!)
    }
}
