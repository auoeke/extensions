@file:Suppress("unused", "FunctionName", "NOTHING_TO_INLINE")

package net.auoeke.extensions.asm

import org.objectweb.asm.Handle
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import java.util.function.Function

@Suppress("unused")
class InstructionList : InsnList, List<AbstractInsnNode> {
    override val size: Int get() = super.size()

    private val labelConstructor = Function<String, Label> {Label()}
    private val labels = HashMap<String, Label>()

    constructor(vararg instructions: AbstractInsnNode, initializer: InstructionList.() -> Unit = {}) {
        add(*instructions)
        initializer(this)
    }

    constructor(instructions: InsnList?, initializer: InstructionList.() -> Unit = {}) {
        add(instructions)
        initializer(this)
    }

    override fun isEmpty(): Boolean = size == 0
    override fun lastIndexOf(element: AbstractInsnNode): Int = indexOf(element)
    override fun listIterator(): ListIterator<AbstractInsnNode> = iterator()
    override fun listIterator(index: Int): ListIterator<AbstractInsnNode> = iterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): InstructionList = throw UnsupportedOperationException()
    override fun containsAll(elements: Collection<AbstractInsnNode>): Boolean = elements.all(::contains)

    // @formatter:off
    fun <T : InsnList> T.frame(type: Int, numLocal: Int, local: Array<Any>?, numStack: Int, stack: Array<Any>?): T = append(FrameNode(
        type,
        numLocal,
        local?.let(::labelNodes),
        numStack,
        stack?.let(::labelNodes)
    ))
    // @formatter:on

    fun jump(opcode: Int, label: String) = append(JumpInsnNode(opcode, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun jump(opcode: Int, label: Label) = append(JumpInsnNode(opcode, labelNode(label)))
    fun ifeq(label: String) = append(JumpInsnNode(Opcodes.IFEQ, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun ifeq(label: Label) = append(JumpInsnNode(Opcodes.IFEQ, labelNode(label)))
    fun ifne(label: String) = append(JumpInsnNode(Opcodes.IFNE, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun ifne(label: Label) = append(JumpInsnNode(Opcodes.IFNE, labelNode(label)))
    fun iflt(label: String) = append(JumpInsnNode(Opcodes.IFLT, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun iflt(label: Label) = append(JumpInsnNode(Opcodes.IFLT, labelNode(label)))
    fun ifge(label: String) = append(JumpInsnNode(Opcodes.IFGE, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun ifge(label: Label) = append(JumpInsnNode(Opcodes.IFGE, labelNode(label)))
    fun ifgt(label: String) = append(JumpInsnNode(Opcodes.IFGT, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun ifgt(label: Label) = append(JumpInsnNode(Opcodes.IFGT, labelNode(label)))
    fun ifle(label: String) = append(JumpInsnNode(Opcodes.IFLE, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun ifle(label: Label) = append(JumpInsnNode(Opcodes.IFLE, labelNode(label)))
    fun if_icmpeq(label: String) = append(JumpInsnNode(Opcodes.IF_ICMPEQ, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun if_icmpeq(label: Label) = append(JumpInsnNode(Opcodes.IF_ICMPEQ, labelNode(label)))
    fun if_icmpne(label: String) = append(JumpInsnNode(Opcodes.IF_ICMPNE, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun if_icmpne(label: Label) = append(JumpInsnNode(Opcodes.IF_ICMPNE, labelNode(label)))
    fun if_icmplt(label: String) = append(JumpInsnNode(Opcodes.IF_ICMPLT, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun if_icmplt(label: Label) = append(JumpInsnNode(Opcodes.IF_ICMPLT, labelNode(label)))
    fun if_icmpge(label: String) = append(JumpInsnNode(Opcodes.IF_ICMPGE, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun if_icmpge(label: Label) = append(JumpInsnNode(Opcodes.IF_ICMPGE, labelNode(label)))
    fun if_icmpgt(label: String) = append(JumpInsnNode(Opcodes.IF_ICMPGT, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun if_icmpgt(label: Label) = append(JumpInsnNode(Opcodes.IF_ICMPGT, labelNode(label)))
    fun if_icmple(label: String) = append(JumpInsnNode(Opcodes.IF_ICMPLE, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun if_icmple(label: Label) = append(JumpInsnNode(Opcodes.IF_ICMPLE, labelNode(label)))
    fun if_acmpeq(label: String) = append(JumpInsnNode(Opcodes.IF_ACMPEQ, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun if_acmpeq(label: Label) = append(JumpInsnNode(Opcodes.IF_ACMPEQ, labelNode(label)))
    fun if_acmpne(label: String) = append(JumpInsnNode(Opcodes.IF_ACMPNE, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun if_acmpne(label: Label) = append(JumpInsnNode(Opcodes.IF_ACMPNE, labelNode(label)))
    fun goto(label: String) = append(JumpInsnNode(Opcodes.GOTO, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun goto(label: Label) = append(JumpInsnNode(Opcodes.GOTO, labelNode(label)))
    fun jsr(label: String) = append(JumpInsnNode(Opcodes.JSR, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun jsr(label: Label) = append(JumpInsnNode(Opcodes.JSR, labelNode(label)))
    fun ifnull(label: String) = append(JumpInsnNode(Opcodes.IFNULL, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun ifnull(label: Label) = append(JumpInsnNode(Opcodes.IFNULL, labelNode(label)))
    fun ifnonnull(label: String) = append(JumpInsnNode(Opcodes.IFNONNULL, labelNode(labels.computeIfAbsent(label, labelConstructor))))
    fun ifnonnull(label: Label) = append(JumpInsnNode(Opcodes.IFNONNULL, labelNode(label)))
    fun label(label: String) = append(labelNode(labels.computeIfAbsent(label, labelConstructor)))
    fun label(label: Label) = append(labelNode(label))
    fun tableswitch(min: Int, max: Int, dflt: Label, vararg labels: Label) = append(TableSwitchInsnNode(min, max, labelNode(dflt), *labelNodes(labels)))
    fun lookupswitch(dflt: Label, keys: IntArray?, labels: Array<Label>) = append(LookupSwitchInsnNode(labelNode(dflt), keys, labelNodes(labels)))

    private companion object {
        private fun labelNode(label: Label): LabelNode = when (label.info) {
            null -> LabelNode().also {label.info = it}
            else -> label.info
        } as LabelNode

        private fun labelNodes(objects: Array<*>): Array<*> = objects.map {
            when (it) {
                is Label -> labelNode(it)
                else -> it
            }
        }.toTypedArray()

        private fun labelNodes(labels: Array<out Label>): Array<LabelNode> = labels.map(Companion::labelNode).toTypedArray()
    }
}

inline fun <T : InsnList> T.append(instruction: AbstractInsnNode): T = apply {
    add(instruction)
}

inline fun <T : InsnList> T.add(vararg instructions: AbstractInsnNode): T = apply {
    instructions.forEach(::add)
}

/** @see InsnNode */
inline fun <T : InsnList> T.insn(opcode: Int): T = append(InsnNode(opcode))
inline fun <T : InsnList> T.nop(): T = append(InsnNode(Opcodes.NOP))
inline fun <T : InsnList> T.aconst_null(): T = append(InsnNode(Opcodes.ACONST_NULL))
inline fun <T : InsnList> T.iconst_m1(): T = append(InsnNode(Opcodes.ICONST_M1))
inline fun <T : InsnList> T.iconst_0(): T = append(InsnNode(Opcodes.ICONST_0))
inline fun <T : InsnList> T.iconst_1(): T = append(InsnNode(Opcodes.ICONST_1))
inline fun <T : InsnList> T.iconst_2(): T = append(InsnNode(Opcodes.ICONST_2))
inline fun <T : InsnList> T.iconst_3(): T = append(InsnNode(Opcodes.ICONST_3))
inline fun <T : InsnList> T.iconst_4(): T = append(InsnNode(Opcodes.ICONST_4))
inline fun <T : InsnList> T.iconst_5(): T = append(InsnNode(Opcodes.ICONST_5))
inline fun <T : InsnList> T.lconst_0(): T = append(InsnNode(Opcodes.LCONST_0))
inline fun <T : InsnList> T.lconst_1(): T = append(InsnNode(Opcodes.LCONST_1))
inline fun <T : InsnList> T.fconst_0(): T = append(InsnNode(Opcodes.FCONST_0))
inline fun <T : InsnList> T.fconst_1(): T = append(InsnNode(Opcodes.FCONST_1))
inline fun <T : InsnList> T.fconst_2(): T = append(InsnNode(Opcodes.FCONST_2))
inline fun <T : InsnList> T.dconst_0(): T = append(InsnNode(Opcodes.DCONST_0))
inline fun <T : InsnList> T.dconst_1(): T = append(InsnNode(Opcodes.DCONST_1))
inline fun <T : InsnList> T.iaload(): T = append(InsnNode(Opcodes.IALOAD))
inline fun <T : InsnList> T.laload(): T = append(InsnNode(Opcodes.LALOAD))
inline fun <T : InsnList> T.faload(): T = append(InsnNode(Opcodes.FALOAD))
inline fun <T : InsnList> T.daload(): T = append(InsnNode(Opcodes.DALOAD))
inline fun <T : InsnList> T.aaload(): T = append(InsnNode(Opcodes.AALOAD))
inline fun <T : InsnList> T.baload(): T = append(InsnNode(Opcodes.BALOAD))
inline fun <T : InsnList> T.caload(): T = append(InsnNode(Opcodes.CALOAD))
inline fun <T : InsnList> T.saload(): T = append(InsnNode(Opcodes.SALOAD))
inline fun <T : InsnList> T.iastore(): T = append(InsnNode(Opcodes.IASTORE))
inline fun <T : InsnList> T.lastore(): T = append(InsnNode(Opcodes.LASTORE))
inline fun <T : InsnList> T.fastore(): T = append(InsnNode(Opcodes.FASTORE))
inline fun <T : InsnList> T.dastore(): T = append(InsnNode(Opcodes.DASTORE))
inline fun <T : InsnList> T.aastore(): T = append(InsnNode(Opcodes.AASTORE))
inline fun <T : InsnList> T.bastore(): T = append(InsnNode(Opcodes.BASTORE))
inline fun <T : InsnList> T.castore(): T = append(InsnNode(Opcodes.CASTORE))
inline fun <T : InsnList> T.sastore(): T = append(InsnNode(Opcodes.SASTORE))
inline fun <T : InsnList> T.pop(): T = append(InsnNode(Opcodes.POP))
inline fun <T : InsnList> T.pop2(): T = append(InsnNode(Opcodes.POP2))
inline fun <T : InsnList> T.dup(): T = append(InsnNode(Opcodes.DUP))
inline fun <T : InsnList> T.dup_x1(): T = append(InsnNode(Opcodes.DUP_X1))
inline fun <T : InsnList> T.dup_x2(): T = append(InsnNode(Opcodes.DUP_X2))
inline fun <T : InsnList> T.dup2(): T = append(InsnNode(Opcodes.DUP2))
inline fun <T : InsnList> T.dup2_x1(): T = append(InsnNode(Opcodes.DUP2_X1))
inline fun <T : InsnList> T.dup2_x2(): T = append(InsnNode(Opcodes.DUP2_X2))
inline fun <T : InsnList> T.swap(): T = append(InsnNode(Opcodes.SWAP))
inline fun <T : InsnList> T.iadd(): T = append(InsnNode(Opcodes.IADD))
inline fun <T : InsnList> T.ladd(): T = append(InsnNode(Opcodes.LADD))
inline fun <T : InsnList> T.fadd(): T = append(InsnNode(Opcodes.FADD))
inline fun <T : InsnList> T.dadd(): T = append(InsnNode(Opcodes.DADD))
inline fun <T : InsnList> T.isub(): T = append(InsnNode(Opcodes.ISUB))
inline fun <T : InsnList> T.lsub(): T = append(InsnNode(Opcodes.LSUB))
inline fun <T : InsnList> T.fsub(): T = append(InsnNode(Opcodes.FSUB))
inline fun <T : InsnList> T.dsub(): T = append(InsnNode(Opcodes.DSUB))
inline fun <T : InsnList> T.imul(): T = append(InsnNode(Opcodes.IMUL))
inline fun <T : InsnList> T.lmul(): T = append(InsnNode(Opcodes.LMUL))
inline fun <T : InsnList> T.fmul(): T = append(InsnNode(Opcodes.FMUL))
inline fun <T : InsnList> T.dmul(): T = append(InsnNode(Opcodes.DMUL))
inline fun <T : InsnList> T.idiv(): T = append(InsnNode(Opcodes.IDIV))
inline fun <T : InsnList> T.ldiv(): T = append(InsnNode(Opcodes.LDIV))
inline fun <T : InsnList> T.fdiv(): T = append(InsnNode(Opcodes.FDIV))
inline fun <T : InsnList> T.ddiv(): T = append(InsnNode(Opcodes.DDIV))
inline fun <T : InsnList> T.irem(): T = append(InsnNode(Opcodes.IREM))
inline fun <T : InsnList> T.lrem(): T = append(InsnNode(Opcodes.LREM))
inline fun <T : InsnList> T.frem(): T = append(InsnNode(Opcodes.FREM))
inline fun <T : InsnList> T.drem(): T = append(InsnNode(Opcodes.DREM))
inline fun <T : InsnList> T.ineg(): T = append(InsnNode(Opcodes.INEG))
inline fun <T : InsnList> T.lneg(): T = append(InsnNode(Opcodes.LNEG))
inline fun <T : InsnList> T.fneg(): T = append(InsnNode(Opcodes.FNEG))
inline fun <T : InsnList> T.dneg(): T = append(InsnNode(Opcodes.DNEG))
inline fun <T : InsnList> T.ishl(): T = append(InsnNode(Opcodes.ISHL))
inline fun <T : InsnList> T.lshl(): T = append(InsnNode(Opcodes.LSHL))
inline fun <T : InsnList> T.ishr(): T = append(InsnNode(Opcodes.ISHR))
inline fun <T : InsnList> T.lshr(): T = append(InsnNode(Opcodes.LSHR))
inline fun <T : InsnList> T.iushr(): T = append(InsnNode(Opcodes.IUSHR))
inline fun <T : InsnList> T.lushr(): T = append(InsnNode(Opcodes.LUSHR))
inline fun <T : InsnList> T.iand(): T = append(InsnNode(Opcodes.IAND))
inline fun <T : InsnList> T.land(): T = append(InsnNode(Opcodes.LAND))
inline fun <T : InsnList> T.ior(): T = append(InsnNode(Opcodes.IOR))
inline fun <T : InsnList> T.lor(): T = append(InsnNode(Opcodes.LOR))
inline fun <T : InsnList> T.ixor(): T = append(InsnNode(Opcodes.IXOR))
inline fun <T : InsnList> T.lxor(): T = append(InsnNode(Opcodes.LXOR))
inline fun <T : InsnList> T.i2l(): T = append(InsnNode(Opcodes.I2L))
inline fun <T : InsnList> T.i2f(): T = append(InsnNode(Opcodes.I2F))
inline fun <T : InsnList> T.i2d(): T = append(InsnNode(Opcodes.I2D))
inline fun <T : InsnList> T.l2i(): T = append(InsnNode(Opcodes.L2I))
inline fun <T : InsnList> T.l2f(): T = append(InsnNode(Opcodes.L2F))
inline fun <T : InsnList> T.l2d(): T = append(InsnNode(Opcodes.L2D))
inline fun <T : InsnList> T.f2i(): T = append(InsnNode(Opcodes.F2I))
inline fun <T : InsnList> T.f2l(): T = append(InsnNode(Opcodes.F2L))
inline fun <T : InsnList> T.f2d(): T = append(InsnNode(Opcodes.F2D))
inline fun <T : InsnList> T.d2i(): T = append(InsnNode(Opcodes.D2I))
inline fun <T : InsnList> T.d2l(): T = append(InsnNode(Opcodes.D2L))
inline fun <T : InsnList> T.d2f(): T = append(InsnNode(Opcodes.D2F))
inline fun <T : InsnList> T.i2b(): T = append(InsnNode(Opcodes.I2B))
inline fun <T : InsnList> T.i2c(): T = append(InsnNode(Opcodes.I2C))
inline fun <T : InsnList> T.i2s(): T = append(InsnNode(Opcodes.I2S))
inline fun <T : InsnList> T.lcmp(): T = append(InsnNode(Opcodes.LCMP))
inline fun <T : InsnList> T.fcmpl(): T = append(InsnNode(Opcodes.FCMPL))
inline fun <T : InsnList> T.fcmpg(): T = append(InsnNode(Opcodes.FCMPG))
inline fun <T : InsnList> T.dcmpl(): T = append(InsnNode(Opcodes.DCMPL))
inline fun <T : InsnList> T.dcmpg(): T = append(InsnNode(Opcodes.DCMPG))
inline fun <T : InsnList> T.ireturn(): T = append(InsnNode(Opcodes.IRETURN))
inline fun <T : InsnList> T.lreturn(): T = append(InsnNode(Opcodes.LRETURN))
inline fun <T : InsnList> T.freturn(): T = append(InsnNode(Opcodes.FRETURN))
inline fun <T : InsnList> T.dreturn(): T = append(InsnNode(Opcodes.DRETURN))
inline fun <T : InsnList> T.areturn(): T = append(InsnNode(Opcodes.ARETURN))
inline fun <T : InsnList> T.vreturn(): T = append(InsnNode(Opcodes.RETURN))
inline fun <T : InsnList> T.arraylength(): T = append(InsnNode(Opcodes.ARRAYLENGTH))
inline fun <T : InsnList> T.athrow(): T = append(InsnNode(Opcodes.ATHROW))
inline fun <T : InsnList> T.monitorenter(): T = append(InsnNode(Opcodes.MONITORENTER))
inline fun <T : InsnList> T.monitorexit(): T = append(InsnNode(Opcodes.MONITOREXIT))

/** @see IntInsnNode */
inline fun <T : InsnList> T.intInsn(opcode: Int, operand: Int): T = append(IntInsnNode(opcode, operand))
inline fun <T : InsnList> T.bipush(operand: Int): T = append(IntInsnNode(Opcodes.BIPUSH, operand))
inline fun <T : InsnList> T.sipush(operand: Int): T = append(IntInsnNode(Opcodes.SIPUSH, operand))
inline fun <T : InsnList> T.newarray(operand: Int): T = append(IntInsnNode(Opcodes.NEWARRAY, operand))

/** @see VarInsnNode */
inline fun <T : InsnList> T.varInsn(opcode: Int, index: Int): T = append(VarInsnNode(opcode, index))
inline fun <T : InsnList> T.iload(index: Int): T = append(VarInsnNode(Opcodes.ILOAD, index))
inline fun <T : InsnList> T.lload(index: Int): T = append(VarInsnNode(Opcodes.LLOAD, index))
inline fun <T : InsnList> T.fload(index: Int): T = append(VarInsnNode(Opcodes.FLOAD, index))
inline fun <T : InsnList> T.dload(index: Int): T = append(VarInsnNode(Opcodes.DLOAD, index))
inline fun <T : InsnList> T.aload(index: Int): T = append(VarInsnNode(Opcodes.ALOAD, index))
inline fun <T : InsnList> T.istore(index: Int): T = append(VarInsnNode(Opcodes.ISTORE, index))
inline fun <T : InsnList> T.lstore(index: Int): T = append(VarInsnNode(Opcodes.LSTORE, index))
inline fun <T : InsnList> T.fstore(index: Int): T = append(VarInsnNode(Opcodes.FSTORE, index))
inline fun <T : InsnList> T.dstore(index: Int): T = append(VarInsnNode(Opcodes.DSTORE, index))
inline fun <T : InsnList> T.astore(index: Int): T = append(VarInsnNode(Opcodes.ASTORE, index))
inline fun <T : InsnList> T.ret(index: Int): T = append(VarInsnNode(Opcodes.RET, index))

/** @see TypeInsnNode */
inline fun <T : InsnList> T.type(opcode: Int, type: String): T = append(TypeInsnNode(opcode, type))
inline fun <T : InsnList> T.anew(descriptor: String): T = append(TypeInsnNode(Opcodes.NEW, descriptor))
inline fun <T : InsnList> T.anewarray(descriptor: String): T = append(TypeInsnNode(Opcodes.ANEWARRAY, descriptor))
inline fun <T : InsnList> T.checkcast(descriptor: String): T = append(TypeInsnNode(Opcodes.CHECKCAST, descriptor))
inline fun <T : InsnList> T.instanceof(descriptor: String): T = append(TypeInsnNode(Opcodes.INSTANCEOF, descriptor))

/** @see FieldInsnNode */
inline fun <T : InsnList> T.field(opcode: Int, owner: String, name: String, descriptor: String): T = append(FieldInsnNode(opcode, owner, name, descriptor))
inline fun <T : InsnList> T.getstatic(owner: String, name: String, descriptor: String): T = append(FieldInsnNode(Opcodes.GETSTATIC, owner, name, descriptor))
inline fun <T : InsnList> T.putstatic(owner: String, name: String, descriptor: String): T = append(FieldInsnNode(Opcodes.PUTSTATIC, owner, name, descriptor))
inline fun <T : InsnList> T.getfield(owner: String, name: String, descriptor: String): T = append(FieldInsnNode(Opcodes.GETFIELD, owner, name, descriptor))
inline fun <T : InsnList> T.putfield(owner: String, name: String, descriptor: String): T = append(FieldInsnNode(Opcodes.PUTFIELD, owner, name, descriptor))

/** @see MethodInsnNode */
inline fun <T : InsnList> T.method(opcode: Int, owner: String, name: String, descriptor: String, isInterface: Boolean): T = append(MethodInsnNode(opcode, owner, name, descriptor, isInterface))
inline fun <T : InsnList> T.invokevirtual(owner: String, name: String, descriptor: String): T = append(MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, name, descriptor, false))
inline fun <T : InsnList> T.invokespecial(owner: String, name: String, descriptor: String): T = append(MethodInsnNode(Opcodes.INVOKESPECIAL, owner, name, descriptor, false))
inline fun <T : InsnList> T.invokestatic(owner: String, name: String, descriptor: String, isInterface: Boolean): T = append(MethodInsnNode(Opcodes.INVOKESTATIC, owner, name, descriptor, isInterface))
inline fun <T : InsnList> T.invokeinterface(owner: String, name: String, descriptor: String): T = append(MethodInsnNode(Opcodes.INVOKEINTERFACE, owner, name, descriptor, true))

/** @see InvokeDynamicInsnNode */
inline fun <T : InsnList> T.invokedynamic(name: String, descriptor: String, bootstrapMethodHandle: Handle, vararg bootstrapMethodArguments: Any): T {
    return append(InvokeDynamicInsnNode(name, descriptor, bootstrapMethodHandle, *bootstrapMethodArguments))
}

/** @see JumpInsnNode */
inline fun <T : InsnList> T.jump(opcode: Int, label: LabelNode): T = append(JumpInsnNode(opcode, label))
inline fun <T : InsnList> T.ifeq(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.ifne(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.iflt(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.ifge(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.ifgt(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.ifle(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.if_icmpeq(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.if_icmpne(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.if_icmplt(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.if_icmpge(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.if_icmpgt(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.if_icmple(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.if_acmpeq(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.if_acmpne(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.goto(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.jsr(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.ifnull(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))
inline fun <T : InsnList> T.ifnonnull(label: LabelNode): T = append(JumpInsnNode(Opcodes.IFEQ, label))

/** @see LdcInsnNode */
inline fun <T : InsnList> T.ldc(value: Any): T = append(LdcInsnNode(value))

/** @see IincInsnNode */
inline fun <T : InsnList> T.iinc(index: Int, increment: Int): T = append(IincInsnNode(index, increment))

/** @see MultiANewArrayInsnNode */
inline fun <T : InsnList> T.multianewarray(descriptor: String, numDimensions: Int): T = append(MultiANewArrayInsnNode(descriptor, numDimensions))
