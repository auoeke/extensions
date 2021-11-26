@file:Suppress("NOTHING_TO_INLINE", "unused")

package net.auoeke.extensions

import java.util.jar.*

inline val Manifest.specificationVersion: String? get() = mainAttributes("Specification-Version")
inline val Manifest.implementationVersion: String? get() = mainAttributes("Implementation-Version")

inline operator fun Attributes.invoke(name: String): String? = getValue(name)
