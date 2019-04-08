import keywords.SymbolsAndStatements

fun main() {
    val lexer = Lexer()
    Lexer.filePath = "src/main/resources/Main.txt"
    println("Text from file: ${lexer.getCharsFromFile()}")
    var a = lexer.nextToken()
    
}