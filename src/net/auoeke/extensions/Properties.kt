@file:Suppress("NOTHING_TO_INLINE", "unused", "UNCHECKED_CAST")

package net.auoeke.extensions

import java.io.*
import java.nio.file.*
import java.util.*
import kotlin.io.path.*

inline fun Properties(stream: InputStream): Properties = Properties().apply {load(stream)}
inline fun Properties(path: Path): Properties = Properties(path.inputStream())

inline operator fun Properties.invoke(name: String): String? = getProperty(name)

inline fun Properties.each(action: (Map.Entry<String, String>) -> Unit) = forEach {action(it as Map.Entry<String, String>)}
inline fun Properties.each(action: (String, String) -> Unit) = withEach {action(key, value)}
inline fun Properties.withEach(action: Map.Entry<String, String>.() -> Unit) = each(action)
