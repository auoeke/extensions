@file:Suppress("NOTHING_TO_INLINE", "unused")

package net.auoeke.extensions

import java.io.*
import java.net.*
import java.nio.file.*
import java.nio.file.attribute.*
import java.time.*
import kotlin.io.path.*

inline val Path.exists: Boolean get() = Files.exists(this)
inline val Path.asFile: File get() = toFile()
inline val Path.asURI: URI get() = toUri()
inline val Path.asURL: URL get() = asURI.toURL()
inline val Path.newFilesystem: FileSystem get() = FileSystems.newFileSystem(this)
inline val Path.attributes: BasicFileAttributes get() = readAttributes()
inline val Path.mtime: FileTime get() = getLastModifiedTime()
inline val Path.real: Path get() = mapIf(exists) {toRealPath()}

inline fun Path.mtime(vararg options: LinkOption): FileTime = getLastModifiedTime(*options)
inline fun Path.updateMtime() = setLastModifiedTime(FileTime.from(Instant.now()))
inline fun Path.copy(destination: Path, vararg options: CopyOption): Path = Files.copy(this, destination, *options)
inline fun Path.copy(destination: OutputStream): Long = Files.copy(this, destination)
inline fun Path.list(glob: String): List<Path> = listDirectoryEntries(glob)

inline fun Path.list(recurse: Boolean = false): List<Path> = when {
    recurse -> ArrayList<Path>().apply {walk {this.add(it)}}
    else -> Files.list(this).toList()
}

inline fun Path.listFiles(recurse: Boolean = false): List<Path> = when {
    recurse -> ArrayList<Path>().apply {walkFiles(this::add)}
    else -> Files.list(this).filter {!it.isDirectory()}.toList()
}

inline fun Path.delete(recurse: Boolean = false): Path = when {
    recurse -> walk(TreeDeleter)
    else -> apply {deleteExisting()}
}

inline fun Path.tryDelete(recurse: Boolean = false): Path = apply {
    if (exists) {
        delete(recurse)
    }
}

inline fun Path.move(destination: Path, vararg options: CopyOption): Path = Files.move(this, destination, *options)
inline fun Path.write(contents: String, vararg options: OpenOption): Path = Files.writeString(this, contents, *options)
inline fun Path.write(contents: ByteArray, vararg options: OpenOption): Path = Files.write(this, contents, *options)
inline fun Path.mkdirs(vararg attributes: FileAttribute<*>): Path = Files.createDirectories(this, *attributes)
inline fun Path.walk(visitor: FileVisitor<Path>, depth: Int = Int.MAX_VALUE, options: Set<FileVisitOption>): Path = Files.walkFileTree(this, options, depth, visitor)
inline fun Path.walk(visitor: FileVisitor<Path>, depth: Int = Int.MAX_VALUE, vararg options: FileVisitOption): Path = walk(visitor, depth, options.toSet())
inline fun Path.walk(visitor: FileVisitor<Path>, vararg options: FileVisitOption): Path = walk(visitor, Int.MAX_VALUE, *options)
inline fun Path.walk(vararg options: FileVisitOption, noinline action: (Path) -> Unit): Path = Files.walkFileTree(this, options.toSet(), Int.MAX_VALUE, TreeWalker(action))
inline fun Path.walkFiles(noinline action: (Path) -> Unit): Path = Files.walkFileTree(this, FileWalker(action))
inline fun Path.walkFiles(vararg options: FileVisitOption, noinline action: (Path) -> Unit): Path = Files.walkFileTree(this, options.toSet(), Int.MAX_VALUE, FileWalker(action))
inline fun Path.newFilesystem(loader: ClassLoader? = null): FileSystem = FileSystems.newFileSystem(this, loader)
inline fun Path.newFilesystem(env: Map<String, *>, loader: ClassLoader? = null): FileSystem = FileSystems.newFileSystem(this, env, loader)
inline fun Path.same(other: Path): Boolean = exists && other.exists && isSameFileAs(other)
inline fun Path.parent(level: Int): Path = root.mapIf(level > 0) {resolve(subpath(0, level))}
inline fun Path.ascend(levels: Int): Path = parent(nameCount - levels)
