@file:Suppress("unused")

package net.auoeke.extensions

inline val String.capitalized: String get() = replaceFirstChar(Char::uppercaseChar)
inline val String.slashed: String get() = replace('.', '/')
inline val String.dotted: String get() = replace('/', '.')
