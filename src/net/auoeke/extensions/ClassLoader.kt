@file:Suppress("NOTHING_TO_INLINE", "unused")

package net.auoeke.extensions

import java.io.*
import java.nio.file.*

inline fun ClassLoader.resource(name: String): Path? = getResource(name)?.asPath
inline fun ClassLoader.resourceStream(name: String): InputStream? = getResourceAsStream(name)
