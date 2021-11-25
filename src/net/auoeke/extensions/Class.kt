@file:Suppress("NOTHING_TO_INLINE", "unused")

package net.auoeke.extensions

import java.io.*
import java.nio.file.*
import java.security.*
import kotlin.reflect.*

inline val Class<*>.loader: ClassLoader? get() = classLoader
inline val Class<*>.hierarchy: List<Class<*>> get() = hierarchy(null)
inline val Class<*>.codeSource: CodeSource? get() = protectionDomain?.codeSource
inline val Class<*>.path: Path? get() = resource("/${name.slashed}.class")
inline val Class<*>.source: Path? get() = path?.ascend(name.count('.') + 1)

inline fun Class<*>.resource(name: String): Path? = getResource(name)?.asPath
inline fun Class<*>.resourceStream(name: String): InputStream? = getResourceAsStream(name)
inline fun Class<*>.localResource(path: String): Path? = source?.resolve(path)

fun Class<*>.hierarchy(limit: Class<*>?): List<Class<*>> = ArrayList<Class<*>>().also {
    var type: Class<*>? = this

    while (type !== limit) {
        it += type!!.apply {type = superclass}
    }
}

fun Class<*>.hierarchy(excludeObject: Boolean) = hierarchy(when {
    excludeObject -> null
    else -> type<Any>()
})

inline fun <reified T> Class<*>.hierarchy() = hierarchy(type<T>())

inline val KClass<*>.superclass: Class<*> get() = java.superclass
inline val KClass<*>.loader: ClassLoader? get() = java.loader
inline val KClass<*>.hierarchy: List<Class<*>> get() = java.hierarchy
inline val KClass<*>.codeSource: CodeSource? get() = java.codeSource
inline val KClass<*>.path: Path? get() = java.path
inline val KClass<*>.source: Path? get() = java.source

inline fun KClass<*>.resource(name: String): Path? = java.resource(name)
inline fun KClass<*>.resourceStream(name: String): InputStream? = java.resourceStream(name)
inline fun KClass<*>.localResource(path: String): Path? = java.localResource(path)
