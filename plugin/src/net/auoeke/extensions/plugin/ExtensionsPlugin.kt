package net.auoeke.extensions.plugin

import net.auoeke.extensions.Properties
import net.auoeke.extensions.type
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("UNCHECKED_CAST")
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
        add("extensions")
    }

    private fun Project.afterEvaluation() {
        modules.forEach {module ->
            val dependencies = module.value.split(" ").toHashSet()

            configurations.getByName("compileClasspath").allDependencies.all {
                if (dependencies.remove("${it.group}:${it.name}") && dependencies.isEmpty()) {
                    add(module.key)
                }
            }
        }
    }

    private fun Project.add(module: String) = extension.configurations.forEach {dependencies.add(it, "net.auoeke.extensions:$module:latest.release")}

    private companion object {
        val modules = Properties("/dependencies.properties") as Map<String, String>
    }
}
