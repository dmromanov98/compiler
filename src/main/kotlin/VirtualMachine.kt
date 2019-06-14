package main.kotlin

import main.kotlin.hset.HSet
import main.kotlin.keywords.Instructions
import main.kotlin.llist.LList
import java.util.*
import kotlin.collections.HashSet

class VirtualMachine {

    fun run(program: MutableList<Any?>) {

        val ar = arrayOfNulls<Int>(26)

        val llAr = arrayOfNulls<LList<Int>>(26)

        val hsAr = arrayOfNulls<HSet<Int>>(26)

        val stack = Stack<Int>()
        var pc = 0
        var arg: Any? = null
        while (true) {
            val op = program[pc]
            if (pc < program.size - 1)
                arg = program[pc + 1]

            if (op == Instructions.FETCH) {
                if (arg is Char || arg is String) stack.add(ar[arg.hashCode() - 97])
                pc += 2
            } else if (op == Instructions.STORE) {
                when {

                    arg.toString().length == 2 -> {
                        val position = arg.toString()[0].hashCode() - 97
                        var list = llAr[position]
                        if (list == null)
                            list = LList()
                        list.add(stack.pop())
                        llAr[position] = list
                    }

                    arg.toString().length == 3 -> {
                        val position = arg.toString()[0].hashCode() - 97
                        var set = hsAr[position]
                        if (set == null)
                            set = HSet()
                        set.add(stack.pop())
                        hsAr[position] = set
                    }

                    else -> ar[arg.hashCode() - 97] = stack.pop()
                }
                pc += 2
            } else if (op == Instructions.PUSH) {
                if (arg is String) {
                    val args = arg.split(",")
                    if (args.size == 2) {
                        val position = args[0][0].hashCode() - 97

                        val value = if (args[0].length == 2) {
                            llAr[position]?.get(args[1].toInt())
                        } else if (args[0].length == 3){
                            if (hsAr[position]?.contains(args[1].toInt())!!)
                                1
                            else
                                0
                        } else {
                            0
                        }

                        stack.add(value)
                    } else
                        stack.add(arg.toIntOrNull())
                }
                pc += 2
            } else if (op == Instructions.POP) {
                stack.add(arg.hashCode() - 97)
                stack.pop()
                pc += 1
            } else if (op == Instructions.ADD) {
                stack[stack.size - 2] += stack[stack.size - 1]
                stack.pop()
                pc += 1
            } else if (op == Instructions.SUB) {
                stack[stack.size - 2] -= stack[stack.size - 1]
                stack.pop()
                pc += 1
            } else if (op == Instructions.OUT) {
                arg = program[pc + 2]
                when {
                    arg.toString().length == 2 -> {
                        val position = arg.toString()[0].hashCode() - 97
                        var result = ""
                        //val elements = llAr[position]?.elements
                        //println(elements[0])
                        llAr[position]?.elements!!.forEach {
                            result += "$it ,"
                        }
                        println("( ${result.removeSuffix(",")})")
                    }
                    arg.toString().length == 3 -> {
                        val position = arg.toString()[0].hashCode() - 97
                        var result = ""
                        hsAr[position]?.elements!!.forEach {
                            result += "$it ,"
                        }
                        println("{ ${result.removeSuffix(",")}}")
                    }
                    else -> println(ar[arg.hashCode() - 97])
                }
                pc += 3
            } else if (op == Instructions.DELFROMLL) {
                val position = arg.toString()[0].hashCode() - 97
                val list = llAr[position]

                list?.remove(stack.pop())

                llAr[position] = list
                pc += 2
            } else if (op == Instructions.DELFROMHS) {
                val position = arg.toString()[0].hashCode() - 97

                val set = hsAr[position]

                set?.remove(stack.pop())

                hsAr[position] = set
                pc += 2
            }else if (op == Instructions.LT) {
                if (stack[stack.size - 2] < stack[stack.size - 1])
                    stack[stack.size - 2] = 1
                else
                    stack[stack.size - 2] = 0
                stack.pop()
                pc += 1
            } else if (op == Instructions.JZ) {
                if (stack.pop() == 0 && arg is Int)
                    pc = arg.toInt()
                else
                    pc += 2
            } else if (op == Instructions.JNZ) {
                if (stack.pop() != 0 && arg is Int)
                    pc = arg.toInt()
                else
                    pc += 2
            } else if (op == Instructions.JMP && arg is Int) {
                pc = arg.toInt()
            } else if (op == Instructions.HALT)
                break
        }
    }

}