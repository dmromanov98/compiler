package keywords

enum class Symbols(val symbol: Char) {
    LBRA('{'),
    RBRA('}'),
    EQUAL('='),
    SEMICOLON(';'),
    LPAR('('),
    RPAR(')'),
    PLUS('+'),
    MINUS('-'),
    LESS('<');

    companion object {
        @JvmStatic
        fun isSymb(char: Char): Boolean {
            var compareResult = false
            for (symbol in values()) {
                if (valueOf(symbol.name).symbol == char) {
                    compareResult = true
                    break
                }
            }
            return compareResult
        }

        @JvmStatic
        fun getSymbolByChar(char: Char): Symbols? {
            var symbolResult: Symbols? = null
            for (symbol in values()) {
                if (valueOf(symbol.name).symbol == char) {
                    symbolResult = symbol
                    break
                }
            }
            return symbolResult
        }
    }

}