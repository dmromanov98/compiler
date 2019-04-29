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
            return parenExpr()
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
        if (n != null && n.kind == ParserEnums.VAR && Lexer.symb == SymbolsAndStatements.EQUAL) {
            lexer.nextToken()
            n = Node(ParserEnums.SET, op1 = n, op2 = expr())
        }
        return n
    }

    private fun parenExpr(): Node? {

        if (Lexer.symb != SymbolsAndStatements.LPAR)
            error("\"(\" expected")

        lexer.nextToken()

        val n = expr()

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
        } else if (Lexer.symb == SymbolsAndStatements.DO) {
            n = Node(ParserEnums.DO)
            lexer.nextToken()
            n = n!!.copy(op1 = statement())
            if (Lexer.symb != SymbolsAndStatements.WHILE) {
                error("\"while\" expected")
            }
            lexer.nextToken()
            n = n!!.copy(op2 = parenExpr())

            if (Lexer.symb == SymbolsAndStatements.SEMICOLON) {
                error("\";\" expected")
            }

        } else if (Lexer.symb == SymbolsAndStatements.SEMICOLON) {
            n = Node(ParserEnums.EMPTY)
            lexer.nextToken()
        } else if (Lexer.symb == SymbolsAndStatements.LBRA) {
            n = Node(ParserEnums.EMPTY)
            lexer.nextToken()
            while (Lexer.symb != SymbolsAndStatements.RBRA)
                n = Node(ParserEnums.SEQ, op1 = n, op2 = statement())
            lexer.nextToken()
        } else {
            n = Node(ParserEnums.EXPR, op1 = expr())
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