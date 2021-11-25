@file:Suppress("unused")

package net.auoeke.extensions

import java.io.*
import java.net.*
import java.nio.file.*

inline val URL.exists: Boolean get() = asPath.exists
inline val URL.asPath: Path get() = asURI.asPath
inline val URL.asFile: File get() = asURI.asFile
inline val URL.asURI: URI get() = toURI()
