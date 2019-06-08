package main.kotlin

import main.kotlin.keywords.ParserEnums
import main.kotlin.keywords.SymbolsAndStatements
import Node

class Parser {
    var tokenPosition = 0
    lateinit var tokens: MutableList<Token>
    var n: Node? = null

    init {
        tokens = Lexer().getTokens()
    }

    private fun term(): Node? {
        return if (tokens[tokenPosition].key != null &&
            tokens[tokenPosition].key == SymbolsAndStatements.ID
        ) {
            val n = Node(ParserEnums.VAR, tokens[tokenPosition].value)
            tokenPosition++

            if ((n.value!!.length == 2 || n.value!!.length == 3) &&
                tokens[tokenPosition].key == SymbolsAndStatements.DOT
            )
                tokenPosition++
            else if (n.value.length == 2)
                error(" \".\" expected")
            n
        } else if (tokens[tokenPosition].key != null &&
            tokens[tokenPosition].key == SymbolsAndStatements.NUM
        ) {
            val n = Node(ParserEnums.CONST, tokens[tokenPosition].value)
            tokenPosition
            n
        } else {
            parenExpr()
        }
    }

    private fun summa(): Node? {

        var n = term()

        while (tokens[tokenPosition].key == SymbolsAndStatements.PLUS ||
            tokens[tokenPosition].key == SymbolsAndStatements.MINUS
        ) {
            val kind = if (tokens[tokenPosition].key == SymbolsAndStatements.PLUS) {
                ParserEnums.ADD
            } else {
                ParserEnums.SUB
            }
            tokenPosition++
            n = Node(kind, op1 = n, op2 = term())
        }
        return n
    }

    private fun test(): Node? {
        var n = summa()

        if (tokens[tokenPosition].key == SymbolsAndStatements.LESS) {
            tokenPosition++
            n = Node(ParserEnums.LT, op1 = n, op2 = summa())
        }
        return n
    }

    private fun expr(): Node? {
        if (tokens[tokenPosition].key != SymbolsAndStatements.ID)
            return test()

        var n = if (n != null && n!!.kind == ParserEnums.PRINT) {
            val value = tokens[tokenPosition].value
            val n1 = Node(ParserEnums.VAR, value)
            tokenPosition++
            n1
        } else
            test()

        if (n != null && n.kind == ParserEnums.VAR && (tokens[tokenPosition].key == SymbolsAndStatements.EQUAL ||
                    (tokens[tokenPosition].key == SymbolsAndStatements.GETLL) ||
                    (tokens[tokenPosition].key == SymbolsAndStatements.CONTAINSHS))
        ) {

            n = if (tokens[tokenPosition].key == SymbolsAndStatements.GETLL) {
                tokenPosition++
                Node(ParserEnums.GETFROMLL, op2 = n, op3 = parenExpr())
            } else if (tokens[tokenPosition].key == SymbolsAndStatements.CONTAINSHS) {
                tokenPosition++
                Node(ParserEnums.CONTAINSINHS, op2 = n, op3 = parenExpr())
            } else {
                tokenPosition++
                val n1 = expr()!!
                if (n1.op2 != null && n1.op3 != null)
                    Node(ParserEnums.SET, op1 = n, op2 = n1.op2, op3 = n1.op3)
                else
                    Node(ParserEnums.SET, op1 = n, op2 = n1)
            }

        } else if (n != null && n.kind == ParserEnums.VAR &&
            tokens[tokenPosition].key == SymbolsAndStatements.ADDLL
        ) {
            tokenPosition++
            n = Node(ParserEnums.ADDTOLL, op1 = n, op2 = parenExpr())
        } else if (n != null && n.kind == ParserEnums.VAR &&
            tokens[tokenPosition].key == SymbolsAndStatements.REMOVELL
        ) {
            tokenPosition++
            n = Node(ParserEnums.REMOVEFROMLL, op1 = n, op2 = parenExpr())
        } else if (n != null && n.kind == ParserEnums.VAR &&
            tokens[tokenPosition].key == SymbolsAndStatements.ADDHS
        ) {
            tokenPosition++
            n = Node(ParserEnums.ADDTOHS, op1 = n, op2 = parenExpr())
        } else if (n != null && n.kind == ParserEnums.VAR &&
            tokens[tokenPosition].key == SymbolsAndStatements.REMOVEHS
        ) {
            tokenPosition++
            n = Node(ParserEnums.REMOVEFROMHS, op1 = n, op2 = parenExpr())
        }
        return n
    }

    private fun parenExpr(): Node? {

        if (tokens[tokenPosition].key != SymbolsAndStatements.LPAR)
            error("\"(\" expected")

        tokenPosition++

        val n = expr()

        tokenPosition++
        println(tokens[tokenPosition].key)
        if (tokens[tokenPosition].key != SymbolsAndStatements.RPAR)
            error("\")\" expected")

        tokenPosition++

        return n
    }


    fun ifStatement(): Node? {
        n = Node(ParserEnums.IF1)
        tokenPosition++
        n = n!!.copy(op1 = parenExpr(), op2 = statement())
        if (tokens[tokenPosition].key == SymbolsAndStatements.ELSE) {
            n = n!!.copy(kind = ParserEnums.IF2)
            tokenPosition++
            n = n!!.copy(op3 = statement())
        }
        return n
    }

    fun whileStatement(): Node? {
        n = Node(ParserEnums.WHILE)
        tokenPosition++
        n = n!!.copy(op1 = parenExpr(), op2 = statement())
        return n
    }

    fun semicolonSymb(): Node? {
        n = Node(ParserEnums.EMPTY)
        tokenPosition++
        return n
    }

    private fun printStatment(): Node? {
        n = Node(ParserEnums.PRINT)
        tokenPosition++
        n = n!!.copy(op1 = parenExpr())
        return n
    }

    private fun lbraSymb(): Node? {
        n = Node(ParserEnums.EMPTY)
        tokenPosition++
        while (tokens[tokenPosition].key != SymbolsAndStatements.RBRA)
            n = Node(ParserEnums.SEQ, op1 = n, op2 = statement())
        tokenPosition++
        return n
    }

    private fun otherStatements(): Node? {
        n = Node(ParserEnums.EXPR, op1 = expr())
        tokenPosition++
        println(tokens[tokenPosition].key)
        if (tokens[tokenPosition].key != SymbolsAndStatements.SEMICOLON) {
            error("\";\" expected")
        }
        tokenPosition++
        return n
    }

    private fun statement(): Node? {
        return when (tokens[tokenPosition].key) {
            SymbolsAndStatements.IF -> ifStatement()
            SymbolsAndStatements.WHILE -> whileStatement()
            SymbolsAndStatements.SEMICOLON -> semicolonSymb()
            SymbolsAndStatements.PRINT -> printStatment()
            SymbolsAndStatements.LBRA -> lbraSymb()
            else -> otherStatements()
        }
    }

    fun parse(): Node? {
        //tokenPosition++
        val node = Node(ParserEnums.PROG, op1 = statement())
        if (tokens[tokenPosition].key != SymbolsAndStatements.EOF)
            error("Invalid statement syntax")
        return node
    }
}