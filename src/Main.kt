/**
 * Created by subic on 5. 10. 2016.
 */
fun main(args: Array<String>) {
    val inputs: Array<String> = arrayOf(
            "++++++++++++++++++++++++++++++++++++++++++.",
            "++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.",
            "++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.")


    for (input: String in inputs) {
        interpretProgram(input)

    }

}

fun interpretProgram(input: String) {
    val tape: Tape = Tape()

    var index: Int = 0

    while (index < input.length) {
        val instruction: Instruction = Instruction.fromSymbol(input[index])

        when (instruction) {
            Instruction.DECREMENT -> tape.decrement()
            Instruction.INCREMENT -> tape.increment()
            Instruction.LEFT -> tape.moveLeft()
            Instruction.RIGHT -> tape.moveRight()
            Instruction.PRINT -> print(tape.getValue().toChar())
            Instruction.INPUT -> {
                val line: String? = readLine()
                if (line != null)
                    tape.writeValue(line[0])
            }
            Instruction.LOOP_START -> {
                if (tape.getValue() == tape.BLANK_VALUE) {
                    var loopCounter = 1
                    while (loopCounter != 0) {
                        index++
                        when {
                            Instruction.fromSymbol(input[index]) == Instruction.LOOP_END -> loopCounter--
                            Instruction.fromSymbol(input[index]) == Instruction.LOOP_START -> loopCounter++
                        }
                    }
                }
            }
            Instruction.LOOP_END -> {
                if (tape.getValue() != tape.BLANK_VALUE) {
                    var loopCounter = 1
                    while (loopCounter != 0) {
                        index--
                        when {
                            Instruction.fromSymbol(input[index]) == Instruction.LOOP_START -> loopCounter--
                            Instruction.fromSymbol(input[index]) == Instruction.LOOP_END -> loopCounter++
                        }
                    }
                }

            }
        }

        index++
    }
    println()

}

class Tape {
    val BLANK_VALUE: Byte = 0

    val tape: MutableList<Byte> = arrayListOf(0)
    var pointer = 0

    fun increment() {
        tape[pointer]++
    }

    fun decrement() {
        tape[pointer]--
    }

    fun moveLeft() {
        when (pointer) {
            0 -> tape.add(0, BLANK_VALUE)
            else -> pointer--
        }
    }

    fun moveRight() {
        when {
            pointer < tape.size - 1 -> pointer++
            else -> tape.add(tape.size, BLANK_VALUE)
        }
    }

    fun getValue(): Byte {
        return tape[pointer]
    }

    fun writeValue(char: Char) {
        tape[pointer] = char.toByte()
    }
}

enum class Instruction(symbol: Char) {
    LEFT('>'), RIGHT('<'), INCREMENT('+'), DECREMENT('-'), PRINT('.'), INPUT(','), LOOP_START('['), LOOP_END(']');

    companion object {
        fun fromSymbol(char: Char): Instruction {
            when (char) {
                '>' -> return LEFT
                '<' -> return RIGHT
                '+' -> return INCREMENT
                '-' -> return DECREMENT
                '.' -> return PRINT
                ',' -> return INPUT
                '[' -> return LOOP_START
                ']' -> return LOOP_END
                else -> throw Exception("Invalid character.")
            }
        }
    }
}