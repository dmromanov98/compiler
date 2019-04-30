import keywords.Instructions
import java.util.*

class VirtualMachine {

    fun run(program: MutableList<Any?>) {
        var ar = arrayOfNulls<Int>(26)
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
                ar[arg.hashCode() - 97] = stack.pop()
                pc += 2
            } else if (op == Instructions.PUSH) {
                if (arg is String) {
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
                println(ar[arg.hashCode() - 97])
                pc += 3
            } else if (op == Instructions.LT) {
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