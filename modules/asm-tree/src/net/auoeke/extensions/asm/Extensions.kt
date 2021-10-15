package net.auoeke.extensions.asm

import net.auoeke.extensions.*
import org.objectweb.asm.*
import org.objectweb.asm.tree.*
import java.io.*
import java.nio.file.*
import kotlin.io.path.*

inline fun ClassNode(bytecode: ByteArray, options: Int = 0, initializer: ClassNode.() -> Unit = {}): ClassNode = ClassNode().apply {
    ClassReader(bytecode).accept(this, options)
    initializer()
}

inline fun ClassNode(stream: InputStream, options: Int = 0, initializer: ClassNode.() -> Unit = {}): ClassNode = ClassNode(stream.readBytes(), options, initializer)
inline fun ClassNode(path: Path, options: Int = 0, initializer: ClassNode.() -> Unit = {}): ClassNode = ClassNode(path.inputStream(), options, initializer)
inline fun ClassNode(name: String, options: Int = 0, initializer: ClassNode.() -> Unit = {}): ClassNode = ClassNode(Path("${name.slashed}.class"), options, initializer)
inline fun ClassNode(type: Class<*>, options: Int = 0, initializer: ClassNode.() -> Unit = {}): ClassNode = ClassNode(type.path!!, options, initializer)

fun ClassNode.visit(version: Int, access: Int, name: String, superclass: String? = "java/lang/Object", signature: String? = null, vararg interfaces: String): ClassNode = apply {
    visit(version, access, name, signature, superclass, interfaces)
}

fun ClassNode.version(version: Int): ClassNode = apply {this.version = version}
fun ClassNode.access(access: Int): ClassNode = apply {this.access = access}
fun ClassNode.superclass(superclass: String?): ClassNode = apply {superName = superclass}
fun ClassNode.signature(signature: String?): ClassNode = apply {this.signature = signature}
fun ClassNode.interfaces(vararg interfaces: String): ClassNode = apply {this.interfaces = interfaces.toList()}

fun ClassNode.field(access: Int, name: String, descriptor: String, signature: String? = null, value: Any? = null): FieldNode = visitField(access, name, descriptor, signature, value) as FieldNode
fun ClassNode.method(access: Int, name: String, descriptor: String, signature: String? = null, vararg exceptions: String): MethodNode = visitMethod(access, name, descriptor, signature, exceptions) as MethodNode

fun ClassNode.write(writer: ClassWriter = ClassWriter(ClassWriter.COMPUTE_FRAMES)): ByteArray = writer.also(::accept).toByteArray()
inline fun ClassNode.write(writer: (Int) -> ClassWriter): ByteArray = write(writer(ClassWriter.COMPUTE_FRAMES))
