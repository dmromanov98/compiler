import keywords.ConditionalStatements
import main.kotlin.keywords.Symbols
import main.kotlin.keywords.SymbolsAndStatements
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import kotlin.system.exitProcess

class Lexer {

    companion object {

        @JvmStatic
        var charArray: CharArray? = null

        @JvmStatic
        var charPosition = -1

        @JvmStatic
        var filePath: String? = null

        @JvmStatic
        var symb: SymbolsAndStatements? = null

        @JvmStatic
        var value: String? = null

    }

    /**
     * Printing lexer error to console
     * @param message - error message
     */
    private fun error(message: String) {
        println("Lexer error: $message")
        exitProcess(15)
    }

    /**
     * Getting char array from file
     *@return char array from file
     */
    fun getCharsFromFile(): CharArray {
        return try {
            val bufferedReader: BufferedReader = File(filePath).bufferedReader()
            var inputString = bufferedReader.use { it.readText() }
            inputString = inputString.replace("\\s".toRegex(), "")
            charArray = inputString.toCharArray()
            charArray as CharArray

        } catch (ex: FileNotFoundException) {
            ex.message?.let { error(it) }
            "".toCharArray()
        }

    }

    /**
     * Getting each char from char array with char position + 1
     * @return char
     */
    fun nextChar(): Char? {

        if ((charArray == null || charArray!!.isEmpty())) {
            if (!filePath.isNullOrEmpty()) {
                getCharsFromFile()
            } else {
                error("File path is incorrect")
            }
        }

        charPosition++

        return if (charPosition < charArray!!.size)
            charArray?.get(charPosition)
        else
            null
    }

    var charValue: Char? = ' '
    /**
     * Generating token
     */
    fun nextToken() {
        var tmpSymb: SymbolsAndStatements? = null
        var tmpValue: String? = null
        while (tmpSymb == null) {
            //println("$charValue ${Symbols.isSymb(charValue!!)}")
            when {
                (charValue == null) -> tmpSymb = SymbolsAndStatements.EOF

                charValue!!.isWhitespace() -> charValue = nextChar()

                Symbols.isSymb(charValue!!) -> {

                    when (val symbol = Symbols.getSymbolByChar(charValue!!)) {
                        null -> error("Character definition error: $charValue")
                        else -> {
                            tmpSymb = SymbolsAndStatements.valueOf(symbol.name)
                        }
                    }
                    //println(charValue)
                    charValue = nextChar()
                }

                charValue!!.isDigit() -> {
                    var intValue = 0
                    while (charValue != null && charValue!!.isDigit()) {
                        intValue = intValue * 10 + charValue!!.toString().toInt()
                        charValue = nextChar()
                    }
                    tmpValue = intValue.toString()
                    tmpSymb = SymbolsAndStatements.NUM
                }

                charValue!!.isLetter() -> {
                    var ident = ""
                    while (charValue != null && charValue!!.isLetter()) {
                        ident += charValue!!.toLowerCase()
                        charValue = nextChar()
                    }
                    //println(charValue!!)
                    when {
                        ConditionalStatements.isKeyWord(ident) ->
                            when (val keyWord = ConditionalStatements.getSymbolByKeyWord(ident)) {
                                null -> error("KeyWord definition error: $ident")
                                else -> {
                                    tmpSymb = SymbolsAndStatements.valueOf(keyWord.name)
                                }
                            }
                        (ident.length == 1) || (ident.length == 2)-> {
                            tmpSymb = SymbolsAndStatements.ID
                            tmpValue = ident
                            //charValue = nextChar()
                        }

                        else -> error("Unexpected symbol: $charValue")
                    }
                    //charValue = nextChar()
                }

            }

        }
        symb = tmpSymb
        value = tmpValue
    }

}