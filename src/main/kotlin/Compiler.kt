import keywords.Instructions
import keywords.ParserEnums

class Compiler {
    var program = mutableListOf<Any?>()
    var pc = 0

    fun generation(command: Any?) {
        program.add(command)
        pc++
    }

    fun compile(node: Node?): MutableList<Any?> {
        when {
            (node!!.kind == ParserEnums.VAR) -> {
                generation(Instructions.FETCH)
                generation(node.value)
            }
            (node.kind == ParserEnums.CONST) -> {
                generation(Instructions.PUSH)
                generation(node.value)
            }
            (node.kind == ParserEnums.ADD) -> {
                compile(node.op1)
                compile(node.op2)
                generation(Instructions.ADD)
            }
            (node.kind == ParserEnums.SUB) -> {
                compile(node.op1)
                compile(node.op2)
                generation(Instructions.SUB)
            }
            (node.kind == ParserEnums.PRINT)->{
                generation(Instructions.OUT)
                compile(node.op1)
            }
            (node.kind == ParserEnums.LT) -> {
                compile(node.op1)
                compile(node.op2)
                generation(Instructions.LT)
            }
            (node.kind == ParserEnums.SET) -> {
                compile(node.op2)
                generation(Instructions.STORE)
                generation(node.op1!!.value)
            }
            (node.kind == ParserEnums.IF1) -> {
                compile(node.op1)
                generation(Instructions.JZ)
                val addr = pc
                generation(0)
                compile(node.op2)
                program[addr] = pc
            }
            (node.kind == ParserEnums.IF2) -> {
                compile(node.op1)
                generation(Instructions.JZ)
                val addr1 = pc
                generation(0)
                compile(node.op2)
                generation(Instructions.JMP)
                val addr2 = pc
                generation(0)
                program[addr1] = pc
                compile(node.op3)
                program[addr2] = pc
            }
            (node.kind == ParserEnums.WHILE) -> {
                val addr1 = pc
                compile(node.op1)
                generation(Instructions.JZ)
                val addr2 = pc
                generation(0)
                compile(node.op2)
                generation(Instructions.JMP)
                generation(addr1)
                program[addr2] = pc
            }
            (node.kind == ParserEnums.SEQ) -> {
                compile(node.op1)
                compile(node.op2)
            }
            (node.kind == ParserEnums.EXPR) -> {
                compile(node.op1)
                generation(Instructions.POP)
            }
            (node.kind == ParserEnums.PROG) -> {
                compile(node.op1)
                generation(Instructions.HALT)
            }

        }
        return program
    }
}