package main.kotlin.llist

class LList<T> {
    var size: Int = 0
    lateinit var first: Entry<T>
    lateinit var last: Entry<T>

    fun add(element: T) {
        when (size) {
            0 -> {
                first = Entry(currentElement = element)
                last = first
                size++
            }
            1 -> {
                last = Entry(currentElement = element, previousElement = first)
                first = first.copy(nextElement = last)
                last.copy(previousElement = first)
                size++
            }
            else -> {
                last = Entry(currentElement = element, previousElement = last)
                last = last.previousElement!!.copy(nextElement = last)
                size++
            }
        }
    }

    init {
    }

    data class Entry<T>(
        val currentElement: T? = null,
        val nextElement: Entry<T>? = null,
        val previousElement: Entry<T>? = null
    )
}