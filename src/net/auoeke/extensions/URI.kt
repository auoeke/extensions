@file:Suppress("unused")

package net.auoeke.extensions

import java.io.*
import java.net.*
import java.nio.file.*
import kotlin.io.path.*

inline val URI.exists: Boolean get() = toPath().exists
inline val URI.asPath: Path get() = filesystem.provider().getPath(this)
inline val URI.asFile: File get() = asPath.asFile
inline val URI.asURL: URL get() = toURL()
inline val URI.newFilesystem: FileSystem get() = FileSystems.newFileSystem(this, mapOf<String, Any>())

// @formatter:off
val URI.filesystem: FileSystem get() = when (scheme) {
    "file" -> FileSystems.getDefault()
    else -> try {
        FileSystems.getFileSystem(this)
    } catch (_: FileSystemNotFoundException) {
        newFilesystem
    }
}
// @formatter:on
