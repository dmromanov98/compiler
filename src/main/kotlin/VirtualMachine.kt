import keywords.Instructions
import java.util.*

class VirtualMachine {

    fun run(program: MutableList<Instructions>) {
        var ar = arrayOfNulls<Instructions>(26)
        val stack = Stack<Instructions>()
        var pc = 0
        var arg: Instructions? = null
        while (true) {
            val op = program[pc]
            if (pc < program.size - 1)
                arg = program[pc + 1]

            if (op == Instructions.IFETCH) {
                stack.add(arg)
                pc += 2
            }else if  (op == Instructions.ISTORE){

            }
        }

    }

}