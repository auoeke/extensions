@file:Suppress("NOTHING_TO_INLINE", "unused", "UNCHECKED_CAST", "PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package net.auoeke.extensions

import java.io.*
import java.nio.file.*
import java.util.stream.*
import kotlin.reflect.*
import kotlin.system.*

inline fun println(vararg x: Any?) = kotlin.io.println(x.joinToString(" "))
inline fun property(name: String): String? = System.getProperty(name)
inline fun time(action: () -> Unit): Long = measureNanoTime(action)

inline fun <T> time(action: () -> T, finally: (Long) -> Unit): T {
    val start = System.nanoTime()

    try {
        return action()
    } finally {
        finally(System.nanoTime() - start)
    }
}

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

fun descriptor(type: Any): String = when (type) {
    is Class<*> -> type.descriptorString()
    is KClass<*> -> type.java.descriptorString()
    is Char -> type.string
    else -> 'L' + when (type) {
        is String -> type
        else -> type.string
    }.replace('.', '/') + ';'
}

fun methodDescriptor(returnType: Any, vararg parameterTypes: Any): String = "(${parameterTypes.joinToString("") {descriptor(it)}})${descriptor(returnType)}"

inline fun <reified T> type(): Class<T> = T::class.java
inline fun <reified T : Any> internalName(): String = internalName(T::class.javaObjectType)
inline fun <reified T : Any> descriptor(): String = descriptor(T::class.javaObjectType)

inline fun InputStream.copy(destination: Path, vararg options: CopyOption): Long = Files.copy(this, destination, *options)

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
