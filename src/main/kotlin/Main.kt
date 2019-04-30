fun main() {
    //val lexer = Lexer()
    Lexer.filePath = "src/main/resources/Main.txt"
    //var str = ""
    //lexer.getCharsFromFile().forEach { str += it.toString() }
    //println(str)
    val parser = Parser(Lexer.filePath!!)
//    while (str.length > Lexer.charPosition) {
//        lexer.nextToken()
//        println("{${Lexer.symb} , ${Lexer.value}}")
//    }

//    Lexer.charPosition = -1
//    Lexer.symb = null
//    Lexer.value = null

    val compiler = Compiler()
    val node = parser.parse()
    val program = compiler.compile(node)
    val virtualMachine = VirtualMachine()

    //program.forEach { println(it) }
    virtualMachine.run(program)
    //out(node)
}

fun out(node: Node?) {
    if (node != null) {
        println("${node.kind} ${node.value}")
        out(node.op1)
        out(node.op2)
        out(node.op3)
    }
}