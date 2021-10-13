rootProject.name = "extensions"

fun module(name: String) {
    include(name)
    project(":$name").projectDir = File("modules/$name")
}

include(":plugin")

file("modules").list()!!.forEach(::module)
