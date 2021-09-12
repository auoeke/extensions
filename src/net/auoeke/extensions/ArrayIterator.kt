package net.auoeke.extensions

class ArrayIterator<T>(private val array: Array<T>, private var index: Int = 0) : MutableListIterator<T> {
    private val size: Int = array.size

    override fun hasPrevious(): Boolean = index > 0

    override fun nextIndex(): Int = index

    override fun previous(): T = array[--index]

    override fun previousIndex(): Int = index - 1

    override fun add(element: T) = throw UnsupportedOperationException()

    override fun hasNext(): Boolean = index < size

    override fun next(): T = array[index++]

    override fun remove() = throw UnsupportedOperationException()

    override fun set(element: T) {
        array[index] = element
    }
}
