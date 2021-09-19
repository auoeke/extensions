package net.auoeke.extensions

class StringIterator(private val sequence: CharSequence, var index: Int = 0) : ListIterator<Char> {
    private val length: Int = sequence.length

    override fun hasNext(): Boolean = index < length

    override fun hasPrevious(): Boolean = index > 0

    override fun next(): Char = sequence[index++]

    override fun nextIndex(): Int = index

    override fun previous(): Char = sequence[--index]

    override fun previousIndex(): Int = index - 1
}
