@file:Suppress("NOTHING_TO_INLINE", "unused")

package net.auoeke.extensions

import org.intellij.lang.annotations.*

fun CharSequence.count(char: Char): Int = count {it == char}
fun CharSequence.count(substring: String): Int = Regex.fromLiteral(substring).findAll(this).count()
inline fun CharSequence.containsAny(ignoreCase: Boolean, vararg sequences: CharSequence): Boolean = sequences.any {contains(it, ignoreCase)}
inline fun CharSequence.containsAny(vararg sequences: CharSequence): Boolean = containsAny(false, *sequences)
inline fun CharSequence.endsWith(ignoreCase: Boolean = false, vararg suffixes: CharSequence): Boolean = suffixes.any {endsWith(it, ignoreCase)}
inline fun CharSequence.endsWith(vararg suffixes: CharSequence): Boolean = endsWith(false, *suffixes)
inline fun CharSequence.remove(pattern: Regex): String = replace(pattern, "")
inline fun CharSequence.remove(@RegExp pattern: String): String = remove(pattern.toRegex())
