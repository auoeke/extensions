@file:Suppress("NOTHING_TO_INLINE", "unused", "UNCHECKED_CAST")

package net.auoeke.extensions

import org.intellij.lang.annotations.*
import java.io.*
import java.lang.instrument.*
import java.net.*
import java.nio.file.*
import java.nio.file.attribute.*
import java.security.*
import java.util.*
import java.util.jar.*
import java.util.stream.*
import kotlin.io.path.*
import kotlin.reflect.*

inline fun println(vararg x: Any?) = kotlin.io.println(x.joinToString(" "))
inline fun property(name: String): String? = System.getProperty(name)
inline fun <reified T> type(): Class<T> = T::class.java

fun internalName(type: Any): String = when (type) {
    is Class<*> -> when {
        type.isArray -> '[' + internalName(type.componentType)
        else -> type.descriptorString().removeSurrounding("L", ";")
    }
    is KClass<*> -> internalName(type.java)
    else -> when (type) {
        is String -> type
        else -> type.string
    }.replace('.', '/')
}

inline fun <reified T : Any> internalName(): String = internalName(T::class.javaObjectType)

fun descriptor(type: Any): String = when (type) {
    is Class<*> -> type.descriptorString()
    is KClass<*> -> type.java.descriptorString()
    is Char -> type.string
    else -> 'L' + when (type) {
        is String -> type
        else -> type.string
    }.replace('.', '/') + ';'
}

inline fun <reified T : Any> descriptor(): String = descriptor(T::class.javaObjectType)

fun methodDescriptor(returnType: Any, vararg parameterTypes: Any): String = "(${parameterTypes.joinToString("") {descriptor(it)}})${descriptor(returnType)}"

inline val Any.string: String get() = toString()
inline val Any?.string: String @JvmName("nullableString") get() = toString()
inline val Array<*>.string: String get() = contentToString()
inline val Any.classes: List<Class<*>> get() = type.hierarchy
inline fun <reified T> Any?.cast() = this as T
inline fun <reified T> Any?.isArray(): Boolean = type<Array<T>>().isInstance(this)
inline val <reified T : Any> T.type: Class<T> get() = javaClass
inline fun Any.Properties(stream: InputStream): Properties = Properties().apply {load(stream)}
inline fun Any.Properties(path: Path): Properties = Properties(path.inputStream())
inline fun Any.Properties(name: String): Properties = Properties(type.resource(name)!!)
inline fun Any.Manifest(): Manifest = Manifest(type.localResource("META-INF/MANIFEST.MF")!!.inputStream())

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

inline fun ClassLoader.resource(name: String): Path? = getResource(name)?.asPath
inline fun ClassLoader.resourceStream(name: String): InputStream? = getResourceAsStream(name)

inline val String.capitalized: String get() = replaceFirstChar(Char::uppercaseChar)
inline val String.slashed: String get() = replace('.', '/')
inline val String.dotted: String get() = replace('/', '.')

inline val Path.exists: Boolean get() = Files.exists(this)
inline val Path.asFile: File get() = toFile()
inline val Path.asURI: URI get() = toUri()
inline val Path.asURL: URL get() = asURI.toURL()
inline val Path.newFilesystem: FileSystem get() = FileSystems.newFileSystem(this)

inline fun Path.copy(destination: Path, vararg options: CopyOption): Path = Files.copy(this, destination, *options)
inline fun Path.copy(destination: OutputStream): Long = Files.copy(this, destination)
inline fun Path.list(glob: String = "*"): List<Path> = listDirectoryEntries(glob)
inline fun Path.listDeep(): List<Path> = list("**")

inline fun Path.delete(recurse: Boolean = false): Path = when {
    recurse -> walk(TreeDeleter)
    else -> apply {deleteExisting()}
}

inline fun Path.move(destination: Path, vararg options: CopyOption): Path = Files.move(this, destination, *options)
inline fun Path.write(contents: String, vararg options: OpenOption): Path = Files.writeString(this, contents, *options)
inline fun Path.write(contents: ByteArray, vararg options: OpenOption): Path = Files.write(this, contents, *options)
inline fun Path.mkdirs(vararg attributes: FileAttribute<*>): Path = Files.createDirectories(this, *attributes)
inline fun Path.walk(visitor: FileVisitor<Path>, depth: Int = Int.MAX_VALUE, options: Set<FileVisitOption>): Path = Files.walkFileTree(this, options, depth, visitor)
inline fun Path.walk(visitor: FileVisitor<Path>, depth: Int = Int.MAX_VALUE, vararg options: FileVisitOption): Path = walk(visitor, depth, options.toSet())
inline fun Path.walk(visitor: FileVisitor<Path>, vararg options: FileVisitOption): Path = walk(visitor, Int.MAX_VALUE, *options)
inline fun Path.walkFiles(noinline action: (Path) -> Unit): Path = Files.walkFileTree(this, FileWalker(action))
inline fun Path.newFilesystem(loader: ClassLoader? = null): FileSystem = FileSystems.newFileSystem(this, loader)
inline fun Path.newFilesystem(env: Map<String, *>, loader: ClassLoader? = null): FileSystem = FileSystems.newFileSystem(this, env, loader)

inline fun Path.same(other: Path): Boolean = isSameFileAs(other)
inline fun Path.parent(level: Int): Path = root.applyIf(level > 0) {resolve(subpath(0, level))}
inline fun Path.ascend(levels: Int): Path = parent(nameCount - levels)

inline val File.exists: Boolean get() = exists()
inline val File.asPath: Path get() = toPath()
inline val File.asURI: URI get() = toURI()
inline val File.asURL: URL get() = asURI.asURL

inline operator fun File.div(relative: File) = resolve(relative)
inline operator fun File.div(relative: String) = resolve(relative)

inline val URL.exists: Boolean get() = asPath.exists
inline val URL.asPath: Path get() = asURI.asPath
inline val URL.asFile: File get() = asURI.asFile
inline val URL.asURI: URI get() = toURI()

inline val URI.exists: Boolean get() = toPath().exists
inline val URI.asFile: File get() = asPath.asFile
inline val URI.asURL: URL get() = toURL()

// @formatter:off
val URI.asPath: Path get() = try {
    toPath()
} catch (exception: FileSystemNotFoundException) {
    newFilesystem.provider().getPath(this)
}

inline val URI.newFilesystem: FileSystem get() = FileSystems.newFileSystem(this, mapOf<String, Any>())

val URI.filesystem: FileSystem get() = when (scheme) {
    "file" -> FileSystems.getDefault()
    else -> try {
        FileSystems.getFileSystem(this)
    } catch (exception: FileSystemNotFoundException) {
        newFilesystem
    }
}
// @formatter:on

inline fun InputStream.copy(destination: Path, vararg options: CopyOption): Long = Files.copy(this, destination, *options)

inline val Manifest.version: String? get() = mainAttributes("Implementation-Version")

inline operator fun Attributes.invoke(name: String): String? = getValue(name)
inline operator fun Properties.invoke(name: String): String? = getProperty(name)

inline fun Properties.each(action: (Map.Entry<String, String>) -> Unit) = forEach {action(it as Map.Entry<String, String>)}
inline fun Properties.each(action: (String, String) -> Unit) = withEach {action(key, value)}
inline fun Properties.withEach(action: Map.Entry<String, String>.() -> Unit) = each(action)

inline fun Char.repeat(count: Int): String = string.repeat(count)

inline fun <T, R> T.runIf(condition: Boolean, function: T.() -> R): R? = when {
    condition -> function()
    else -> null
}

inline fun <T> T.applyIf(condition: Boolean, action: T.() -> Unit): T = apply {runIf(condition, action)}
inline fun <T, R> T.letIf(condition: Boolean, transformation: (T) -> R): R? = runIf(condition, transformation)
inline fun <T> T.mapIf(condition: Boolean, transformation: (T) -> T): T = runIf(condition, transformation) ?: this
inline fun <T> T.alsoIf(condition: Boolean, action: (T) -> Unit): T = applyIf(condition, action)

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

inline fun <E> Iterator<E>.asMutable(): MutableIterator<E> = this as MutableIterator<E>
inline fun <E> ListIterator<E>.asMutable(): MutableListIterator<E> = this as MutableListIterator<E>
inline fun <E> Iterable<E>.asMutable(): MutableIterable<E> = this as MutableIterable<E>
inline fun <E> Collection<E>.asMutable(): MutableCollection<E> = this as MutableCollection<E>
inline fun <E> List<E>.asMutable(): MutableList<E> = this as MutableList<E>
inline fun <E> Set<E>.asMutable(): MutableSet<E> = this as MutableSet<E>
inline fun <K, V> Map.Entry<K, V>.asMutable(): MutableMap.MutableEntry<K, V> = this as MutableMap.MutableEntry<K, V>
inline fun <K, V> Map<K, V>.asMutable(): MutableMap<K, V> = this as MutableMap<K, V>

inline fun <T> Array<T>.listIterator(): ArrayIterator<T> = ArrayIterator(this)
inline fun CharSequence.listIterator(): ListIterator<Char> = StringIterator(this)

inline fun <T> Iterable<T>.each(action: (T) -> Unit) = forEach(action)
inline fun <T> Iterable<T>.withEach(action: T.() -> Unit) = forEach(action)
inline fun <T> Iterator<T>.each(action: (T) -> Unit) = forEach(action)
inline fun <T> Iterator<T>.withEach(action: T.() -> Unit) = forEach(action)
inline fun <T> Array<T>.each(action: (T) -> Unit) = forEach(action)
inline fun <T> Array<T>.withEach(action: T.() -> Unit) = forEach(action)
inline fun <T> Stream<T>.each(noinline action: (T) -> Unit) = forEach(action)
inline fun <T> Stream<T>.withEach(noinline action: T.() -> Unit) = forEach(action)
inline fun <K, V> Map<K, V>.each(action: (Map.Entry<K, V>) -> Unit) = forEach(action)
inline fun <K, V> Map<K, V>.each(action: (K, V) -> Unit) = forEach {entry -> action(entry.key, entry.value)}
inline fun <K, V> Map<K, V>.withEach(action: Map.Entry<K, V>.() -> Unit) = forEach(action)

inline fun <T> Iterator<T>.find(predicate: (T) -> Boolean): T? = null.also {
    each {
        if (predicate(it)) {
            return it
        }
    }
}

inline fun <T, O : T> Iterator<T>.find(predicate: (T) -> Boolean, action: (O) -> Unit): O? = null.also {
    each {
        if (predicate(it)) {
            return (it as O).also(action)
        }
    }
}

inline fun <T, M : MutableMap<T, T?>> Iterator<T>.toMap(map: M): M = map.also {
    each {
        map[it] = when {
            hasNext() -> next()
            else -> null
        }
    }
}

inline fun <T> Iterator<T>.toMap(): HashMap<T, T?> = toMap(HashMap<T, T?>())
inline fun <T, M : MutableMap<T, T?>> Iterable<T>.toMap(map: M): M = iterator().toMap(map)
inline fun <T> Iterable<T>.toMap(): HashMap<T, T?> = iterator().toMap()

inline fun <T> MutableCollection<T>.add(vararg values: T) = values.each {this.add(it)}

fun CharSequence.count(char: Char): Int = count {it == char}
fun CharSequence.count(substring: String): Int = Regex.fromLiteral(substring).findAll(this).count()
inline fun CharSequence.containsAny(ignoreCase: Boolean, vararg sequences: CharSequence): Boolean = sequences.any {contains(it, ignoreCase)}
inline fun CharSequence.containsAny(vararg sequences: CharSequence): Boolean = containsAny(false, *sequences)
inline fun CharSequence.endsWith(ignoreCase: Boolean = false, vararg suffixes: CharSequence): Boolean = suffixes.any {endsWith(it, ignoreCase)}
inline fun CharSequence.endsWith(vararg suffixes: CharSequence): Boolean = endsWith(false, *suffixes)
inline fun CharSequence.remove(pattern: Regex): String = replace(pattern, "")
inline fun CharSequence.remove(@RegExp pattern: String): String = remove(pattern.toRegex())

inline fun Instrumentation.transform(transformer: ClassTransformer) = addTransformer(transformer)

inline fun Instrumentation.pretransform(crossinline transformer: (Module, ClassLoader?, String, ProtectionDomain?, ByteArray) -> ByteArray) = transform {module, loader, name, type, domain, bytecode ->
    when (type) {
        null -> transformer(module, loader, name, domain, bytecode)
        else -> bytecode
    }
}

inline fun Instrumentation.retransform(transformer: ClassTransformer) = transform {module, loader, name, type, domain, bytecode ->
    when (type) {
        null -> bytecode
        else -> transformer.transform(module, loader, name, type, domain, bytecode)
    }
}
