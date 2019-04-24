
fun main() {
    val lexer = Lexer()
    Lexer.filePath = "src/main/resources/Main.txt"
    lexer.getCharsFromFile().forEach { print(it.toString()) }
}