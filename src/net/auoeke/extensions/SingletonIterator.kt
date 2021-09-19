package net.auoeke.extensions

class SingletonIterator<T>(private val element: T) : Iterator<T> {
    private var done: Boolean = false

    override fun hasNext(): Boolean = !done

    override fun next(): T = element.also {done = true}
}
