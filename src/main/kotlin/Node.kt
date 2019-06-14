import main.kotlin.keywords.ParserEnums

data class Node(
    val kind: ParserEnums,
    val value: String? = null,
    val op1: Node? = null,
    val op2: Node? = null,
    val op3: Node? = null
)