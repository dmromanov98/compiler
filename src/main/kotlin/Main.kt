package main.kotlin

import Lexer
import Parser

fun main(args: Array<String>) {
    val portObtainedInArgs = args.isNotEmpty()
    Lexer.filePath = if (portObtainedInArgs)
        args[0]
    else
        "Main.txt"

//    val lexer = Lexer()
//
//    for (i in 0..60){
//        lexer.nextToken()
//        println("${Lexer.symb} ${Lexer.value}")
//
//    }
    val parser = Parser(Lexer.filePath!!)
    val compiler = Compiler()
    val node = parser.parse()
    val program = compiler.compile(node)
//    program.forEach{
//        println(it)
//    }
    val virtualMachine = VirtualMachine()
    virtualMachine.run(program)
}

