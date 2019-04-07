fun main() {
    val lexer = Lexer()
    Lexer.filePath = "src/resources/Main.txt"
    println(lexer.getCharsFromFile())
    var a = lexer.nextToken()
    while(!a!!.containsKey(SymbAndWords.EOF)){
        println(a)
        a = lexer.nextToken()
    }
}