import kotlin.system.exitProcess

class Lexer {

    fun error(message: String) {
        println("Lexer error: $message")
        exitProcess(15)
    }


    fun getChars(): CharArray
    {

    }


}