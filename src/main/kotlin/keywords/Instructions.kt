package keywords

/**
 * FETCH x - put x value on stack values
STORE x - save in x variable value from top of the stack
PUSH  n - put n on top of the stack
POP     - delete number from top of the stack
ADD     - sum two numbers from top of the stack
SUB     - sub two numbers from top of the stack
LT      - compare two numbers from top of the stack  (a < b). Result - 0 or 1
JZ    a - if 0 on top of the stack -> go to address a
JNZ   a - if !0 on top of the stack -> go to address a
JMP   a - go to address a
HALT    - exit
 */
enum class Instructions{
    IFETCH,
    ISTORE,
    IPUSH,
    IPOP,
    IADD,
    ISUB,
    ILT,
    JZ,
    JNZ,
    JMP,
    HALT
}