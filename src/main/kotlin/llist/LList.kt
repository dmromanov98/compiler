package main.kotlin.llist

import java.util.*

class LList<T> {
    var size: Int = 0
    var first: Entry<T>? = null
    var last: Entry<T>? = null

//    fun add(element: T) {
//        when (size) {
//            0 -> {
//                first = Entry(currentElement = element)
//                last = first.copy()
//                size++
//            }
//            1 -> {
//                val l = last
//                val newNode = Entry(element, null, l)
//                last = newNode
//
//                if (l == null)
//                    first = newNode
//                else
//                    l.next = newNode
//
//                size++
//            }
//            else -> {
//                last = Entry(currentElement = element, previousElement = last)
//                last = last.previousElement!!.copy(nextElement = last)
//                size++
//            }
//        }
//    }

    fun add(element: T) {

        var l = last
        val newNode = Entry(element, null, l)
        last = newNode

        if (l == null)
            first = newNode
        else
            last = last!!.copy(nextElement = newNode)
            //l = l.copy(nextElement = newNode)

        size++

    }

    init {
    }

    data class Entry<T>(
        val currentElement: T? = null,
        val nextElement: Entry<T>? = null,
        val previousElement: Entry<T>? = null
    )
}