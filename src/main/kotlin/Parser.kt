import keywords.ParserEnums
import keywords.SymbolsAndStatements
import kotlin.system.exitProcess

class Parser {
    private val lexer: Lexer

    constructor(path: String) {
        Lexer.filePath = path
        lexer = Lexer()
    }

    private fun error(message: String) {
        print("Parser error: $message")
        exitProcess(16)
    }

    private fun term(): Node? {
        return if (Lexer.symb != null && Lexer.symb == SymbolsAndStatements.ID) {
            val n = Node(ParserEnums.VAR, Lexer.value!!)
            lexer.nextToken()
            n
        } else if (Lexer.symb != null && Lexer.symb == SymbolsAndStatements.NUM) {
            val n = Node(ParserEnums.CONST, Lexer.value!!)
            lexer.nextToken()
            n
        } else {
            parenExpr()
        }
    }

    private fun summa(): Node? {

        var n = term()

        while (Lexer.symb == SymbolsAndStatements.PLUS || Lexer.symb == SymbolsAndStatements.MINUS) {
            val kind = if (Lexer.symb == SymbolsAndStatements.PLUS) {
                ParserEnums.ADD
            } else {
                ParserEnums.SUB
            }
            lexer.nextToken()
            n = Node(kind, op1 = n, op2 = term())
        }
        return n
    }

    private fun test(): Node? {
        var n = summa()
        println(Lexer.symb)
        if (Lexer.symb == SymbolsAndStatements.LESS) {
            lexer.nextToken()
            n = Node(ParserEnums.LT, op1 = n, op2 = summa())
        }
        return n
    }

    private fun expr(): Node? {
        if (Lexer.symb != SymbolsAndStatements.ID)
            return test()
        var n = test()
        if (n != null && n.kind == ParserEnums.VAR && (Lexer.symb == SymbolsAndStatements.EQUAL ||
                    (Lexer.symb == SymbolsAndStatements.ID && Lexer.value != null && Lexer.value?.length == 2))) {
            lexer.nextToken()

            n = if (Lexer.symb == SymbolsAndStatements.GETLL) {
                lexer.nextToken()
                Node(ParserEnums.SET, op1 = n, op2 = parenExpr())
            }else
                Node(ParserEnums.SET, op1 = n, op2 = expr())

        } else if (n != null && n.kind == ParserEnums.VAR && Lexer.symb == SymbolsAndStatements.ADDLL) {
            lexer.nextToken()
            n = Node(ParserEnums.ADDTOLL, op1 = n, op2 = parenExpr())
        } else if (n != null && n.kind == ParserEnums.VAR && Lexer.symb == SymbolsAndStatements.REMOVELL) {
            lexer.nextToken()
            n = Node(ParserEnums.REMOVEFROMLL, op1 = n, op2 = parenExpr())
        }
        return n
    }

    private fun parenExpr(): Node? {

        //println("${Lexer.symb} HERE")
        if (Lexer.symb != SymbolsAndStatements.LPAR)
            error("\"(\" expected")

        lexer.nextToken()

        println("parenExpr after ( = ${Lexer.symb} ${Lexer.value}")

        val n = expr()

        println("parenExpr after expr = ${Lexer.symb} ${Lexer.value}")
        if (Lexer.symb != SymbolsAndStatements.RPAR)
            error("\")\" expected")

        lexer.nextToken()
        return n
    }

    var n: Node? = null

    private fun statement(): Node? {
        if (Lexer.symb == SymbolsAndStatements.IF) {
            n = Node(ParserEnums.IF1)
            lexer.nextToken()
            n = n!!.copy(op1 = parenExpr(), op2 = statement())
            if (Lexer.symb == SymbolsAndStatements.ELSE) {
                n = n!!.copy(kind = ParserEnums.IF2)
                lexer.nextToken()
                n = n!!.copy(op3 = statement())
            }
        } else if (Lexer.symb == SymbolsAndStatements.WHILE) {
            n = Node(ParserEnums.WHILE)
            lexer.nextToken()
            n = n!!.copy(op1 = parenExpr(), op2 = statement())
        } else if (Lexer.symb == SymbolsAndStatements.SEMICOLON) {
            n = Node(ParserEnums.EMPTY)
            lexer.nextToken()
        } else if (Lexer.symb == SymbolsAndStatements.PRINT) {
            n = Node(ParserEnums.PRINT)
            lexer.nextToken()
            n = n!!.copy(op1 = expr())
        } else if (Lexer.symb == SymbolsAndStatements.LBRA) {
            n = Node(ParserEnums.EMPTY)
            lexer.nextToken()
            while (Lexer.symb != SymbolsAndStatements.RBRA)
                n = Node(ParserEnums.SEQ, op1 = n, op2 = statement())
            lexer.nextToken()
        } else {
            n = Node(ParserEnums.EXPR, op1 = expr())
            //lexer.nextToken()
            //println(Lexer.symb)
            if (Lexer.symb != SymbolsAndStatements.SEMICOLON)
                lexer.nextToken()
            if (Lexer.symb != SymbolsAndStatements.SEMICOLON) {
                error("\";\" expected")
            }
            lexer.nextToken()
        }
        return n
    }

    fun parse(): Node? {
        lexer.nextToken()
        val node = Node(ParserEnums.PROG, op1 = statement())
        if (Lexer.symb != SymbolsAndStatements.EOF)
            error("Invalid statement syntax")
        return node
    }

}