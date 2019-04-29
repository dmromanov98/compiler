import keywords.Instructions
import keywords.ParserEnums

class Compiler {
    var program = mutableListOf<Any?>()
    var pc = 0

    fun generation(command: Any?) {
        program.add(command)
        pc++
    }

    fun compile(node: Node?) {
        when {
            (node!!.kind == ParserEnums.VAR) -> {
                generation(Instructions.IFETCH)
                generation(node.value)
            }
            (node.kind == ParserEnums.CONST) -> {
                generation(Instructions.IPUSH)
                generation(node.value)
            }
            (node.kind == ParserEnums.ADD) -> {
                compile(node.op1)
                compile(node.op2)
                generation(Instructions.IADD)
            }
            (node.kind == ParserEnums.SUB) -> {
                compile(node.op1)
                compile(node.op2)
                generation(Instructions.ISUB)
            }
            (node.kind == ParserEnums.LT) -> {
                compile(node.op1)
                compile(node.op2)
                generation(Instructions.ILT)
            }
            (node.kind == ParserEnums.SET) -> {
                compile(node.op2)
                generation(Instructions.ISTORE)
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
            (node.kind == ParserEnums.DO) -> {
                val addr = pc
                compile(node.op1)
                compile(node.op2)
                generation(Instructions.JNZ)
                generation(addr)
            }
            (node.kind == ParserEnums.SEQ) -> {
                compile(node.op1)
                compile(node.op2)
            }
            (node.kind == ParserEnums.EXPR) -> {
                compile(node.op1)
                generation(Instructions.IPOP)
            }
            (node.kind == ParserEnums.PROG) -> {
                compile(node.op1)
                generation(Instructions.HALT)
            }

        }
    }
}