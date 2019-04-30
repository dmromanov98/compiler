package keywords

enum class ConditionalStatements(val keyWord: String) {
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    PRINT("print");

    companion object {
        @JvmStatic
        fun isKeyWord(keyWordString: String): Boolean {
            var compareResult = false
            for (keyWordValue in values()) {
                if (valueOf(keyWordValue.name).keyWord == keyWordString) {
                    compareResult = true
                    break
                }
            }
            return compareResult
        }

        @JvmStatic
        fun getSymbolByKeyWord(keyWordString: String): ConditionalStatements? {
            var keyWordResult: ConditionalStatements? = null
            for (keyWordValue in values()) {
                if (valueOf(keyWordValue.name).keyWord == keyWordString) {
                    keyWordResult = keyWordValue
                    break
                }
            }
            return keyWordResult
        }
    }

}