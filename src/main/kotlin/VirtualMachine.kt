import keywords.Instructions
import java.util.*

class VirtualMachine {

    fun run(program: MutableList<Any?>) {
        var ar = arrayOfNulls<Int>(26)
        val stack = Stack<Int>()
        var pc = 0
        var arg: Any? = null
        while (true) {
            //print(stack)
            val op = program[pc]
            if (pc < program.size - 1)
                arg = program[pc + 1]

            if (op == Instructions.IFETCH) {
                if (arg is Char || arg is String) stack.add(ar[arg.hashCode() - 97])
                pc += 2
            } else if (op == Instructions.ISTORE) {
                ar[arg.hashCode() - 97] = stack.pop()
                pc += 2
            } else if (op == Instructions.IPUSH) {
                if (arg is String) {
                    stack.add(arg.toIntOrNull())
                }
                pc += 2
            } else if (op == Instructions.IPOP) {
                //TODO MAYBE NOT INT
                stack.add(arg.hashCode() - 97)
                //stack.add(Instructions.IPUSH)
                stack.pop()
                pc += 1
            } else if (op == Instructions.IADD) {
                stack[stack.size-2] += stack[stack.size-1]
                stack.pop()
                pc += 1
            } else if (op == Instructions.ISUB) {
                stack[stack.size-2] -= stack[stack.size-1]
                stack.pop()
                pc += 1
            } else if (op == Instructions.ILT) {
                if (stack[stack.size-2] < stack[stack.size-1])
                    stack[stack.size-2] = 1
                else
                    stack[stack.size-2] = 0
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
        println("Execution finished.")
        for (i in 0..25) {
            if (ar[i] != 0 && ar[i] != null)
                println("${(i.toByte() + 97).toChar()} = ${ar[i]}")
        }

    }

}