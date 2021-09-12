@file:Suppress("NOTHING_TO_INLINE", "unused")

package net.auoeke.extensions

import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.URI
import java.net.URL
import java.nio.file.*
import java.nio.file.attribute.FileAttribute

inline val Any?.string: String get() = this?.toString() ?: "null"

inline val String.capitalized: String get() = replaceFirstChar(Char::uppercaseChar)
inline val String.slashed: String get() = replace('.', '/')
inline val String.dotted: String get() = replace('/', '.')

inline val Path.exists: Boolean get() = Files.exists(this)
inline val Path.file: File get() = toFile()
inline val Path.uri: URI get() = toUri()
inline val Path.url: URL get() = uri.toURL()

inline val File.exists: Boolean get() = exists()
inline val File.asPath: Path get() = toPath()

inline fun <reified T> type(): Class<T> = T::class.java
inline fun property(name: String): String? = System.getProperty(name)

inline fun <T> T.letIf(condition: Boolean, transformation: (T) -> T): T = when {
    condition -> transformation(this)
    else -> this
}

inline fun <T> T.alsoIf(condition: Boolean, action: (T) -> Unit): T = also {
    if (condition) {
        action(this)
    }
}

inline fun <T> runIf(condition: Boolean, transformation: () -> T): T? = when {
    condition -> transformation()
    else -> null
}

@Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")
inline fun Boolean?.then(action: () -> Unit): Boolean? = also {
    if (this == true) {
        action()
    }
}

@Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")
inline fun <T> Boolean?.thenLet(action: () -> T): T? = when (this) {
    true -> action()
    else -> null
}

inline fun <reified T> Any?.cast() = this as T
inline fun <E> Iterator<E>.asMutable(): MutableIterator<E> = this as MutableIterator<E>
inline fun <E> ListIterator<E>.asMutable(): MutableListIterator<E> = this as MutableListIterator<E>
inline fun <E> Iterable<E>.asMutable(): MutableIterable<E> = this as MutableIterable<E>
inline fun <E> Collection<E>.asMutable(): MutableCollection<E> = this as MutableCollection<E>
inline fun <E> List<E>.asMutable(): MutableList<E> = this as MutableList<E>
inline fun <E> Set<E>.asMutable(): MutableSet<E> = this as MutableSet<E>
inline fun <K, V> Map.Entry<K, V>.asMutable(): MutableMap.MutableEntry<K, V> = this as MutableMap.MutableEntry<K, V>
inline fun <K, V> Map<K, V>.asMutable(): MutableMap<K, V> = this as MutableMap<K, V>

inline fun <T> Array<T>.listIterator(): ArrayIterator<T> = ArrayIterator(this)

inline fun <T> Iterator<T>.find(predicate: (T) -> Boolean): T? = null.also {
    this.forEach {
        if (predicate(it)) {
            return it
        }
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <T, O : T> Iterator<T>.find(predicate: (T) -> Boolean, action: (O) -> Unit): O? = null.also {
    this.forEach {
        if (predicate(it)) {
            return (it as O).also {o -> action(o)}
        }
    }
}

fun String.contains(ignoreCase: Boolean = false, vararg sequences: CharSequence): Boolean = false.also {
    sequences.forEach {
        if (this.contains(it, ignoreCase)) {
            return true
        }
    }
}

inline fun String.contains(vararg sequences: CharSequence): Boolean = this.contains(false, *sequences)

fun String.endsWith(ignoreCase: Boolean = false, vararg suffixes: CharSequence): Boolean = false.also {
    suffixes.forEach {
        if (this.endsWith(it, ignoreCase)) {
            return true
        }
    }
}

inline fun String.endsWith(vararg suffixes: CharSequence): Boolean = this.endsWith(false, *suffixes)

inline fun Path.copy(destination: Path, vararg options: CopyOption): Path = Files.copy(this, destination, *options)
inline fun Path.copy(destination: OutputStream): Long = Files.copy(this, destination)
inline fun InputStream.copy(destination: Path, vararg options: CopyOption): Long = Files.copy(this, destination, *options)

inline fun Path.delete(recursive: Boolean = false) {
    when {
        recursive -> this.walk(TreeDeleter)
        else -> Files.delete(this)
    }
}

inline fun Path.move(destination: Path, vararg options: CopyOption): Path = Files.move(this, destination, *options)
inline fun Path.write(contents: String, vararg options: OpenOption): Path = Files.writeString(this, contents, *options)
inline fun Path.write(contents: ByteArray, vararg options: OpenOption): Path = Files.write(this, contents, *options)
inline fun Path.mkdirs(vararg attributes: FileAttribute<*>): Path = Files.createDirectories(this, *attributes)

inline fun Path.walk(visitor: FileVisitor<Path>, depth: Int = Int.MAX_VALUE, options: Set<FileVisitOption>): Path = Files.walkFileTree(this, options, depth, visitor)
inline fun Path.walk(visitor: FileVisitor<Path>, depth: Int = Int.MAX_VALUE, vararg options: FileVisitOption): Path = this.walk(visitor, depth, options.toSet())
inline fun Path.walk(visitor: FileVisitor<Path>, vararg options: FileVisitOption): Path = this.walk(visitor, Int.MAX_VALUE, *options)
inline fun Path.walkFiles(noinline action: (Path) -> Unit): Path = Files.walkFileTree(this, FileWalker(action))
