@file:Suppress("NOTHING_TO_INLINE", "unused")

package net.auoeke.extensions

import java.io.*
import java.net.*
import java.nio.file.*

inline val File.exists: Boolean get() = exists()
inline val File.asPath: Path get() = toPath()
inline val File.asURI: URI get() = toURI()
inline val File.asURL: URL get() = asURI.asURL

inline operator fun File.div(relative: File) = resolve(relative)
inline operator fun File.div(relative: String) = resolve(relative)
