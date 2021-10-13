package net.auoeke.extensions

import java.lang.instrument.ClassFileTransformer
import java.security.ProtectionDomain

fun interface ClassTransformer : ClassFileTransformer {
    override fun transform(module: Module, loader: ClassLoader?, name: String, type: Class<*>?, domain: ProtectionDomain?, bytecode: ByteArray): ByteArray
}
