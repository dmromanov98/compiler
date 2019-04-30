fun main(args: Array<String>) {
    val portObtainedInArgs = args.isNotEmpty()
    Lexer.filePath = if (portObtainedInArgs)
        args[0]
    else
        "Main.txt"

    val parser = Parser(Lexer.filePath!!)
    val compiler = Compiler()
    val node = parser.parse()
    val program = compiler.compile(node)
    val virtualMachine = VirtualMachine()
    virtualMachine.run(program)
}
