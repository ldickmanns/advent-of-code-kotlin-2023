package day03

import readInput
import kotlin.io.path.Path
import kotlin.io.path.writeLines

fun main() {
    val input = readInput("day03/input")
//    val input = readInput("day03/input_test")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    var sum = 0
    val markedInput = input
        .toMutableList()
        .map { it.toCharArray() }
    input.forEachIndexed { row: Int, line: String ->
        var numberString = ""
        var startIndex: Int? = null
        line.forEachIndexed { column: Int, char: Char ->
            if (char.isDigit()) {
                if (startIndex == null) {
                    startIndex = column
                }
                numberString += char
                if (column == line.lastIndex) {
                    if (numberString.isNotEmpty()) {
                        sum += computeAddedValue(
                            input = input,
                            numberString = numberString,
                            row = row,
                            startIndex = startIndex!!,
                        )
                        numberString = ""
                        startIndex = null
                    }
                }
            } else {
                if (numberString.isNotEmpty()) {
                    sum += computeAddedValue(
                        input = input,
                        numberString = numberString,
                        row = row,
                        startIndex = startIndex!!,
                    )
                    numberString = ""
                    startIndex = null
                }
            }
        }
    }

    Path("src/day03/output.txt").writeLines(markedInput.toList().map { it.joinToString("") })

    return sum
}

private fun computeAddedValue(
    input: List<String>,
    numberString: String,
    row: Int,
    startIndex: Int,
): Int {
    val hasNumberAdjacentSymbol = hasNumberAdjacentSymbol(
        input = input,
        numberString = numberString,
        row = row,
        startIndex = startIndex,
    )
    if (hasNumberAdjacentSymbol) {
        return numberString.toInt()
    }

    return 0
}

private fun hasNumberAdjacentSymbol(
    input: List<String>,
    numberString: String,
    row: Int,
    startIndex: Int,
): Boolean {
    val endIndex = startIndex + numberString.lastIndex
    (startIndex..endIndex).forEach { column ->
        val hasDigitAdjacentSymbol = hasDigitAdjacentSymbol(
            input = input,
            row = row,
            column = column
        )
        if (hasDigitAdjacentSymbol) {
            return true
        }
    }
    return false
}

private fun hasDigitAdjacentSymbol(
    input: List<String>,
    row: Int,
    column: Int,
): Boolean {
    val left = row - 1
    val right = row + 1
    val up = column - 1
    val down = column + 1

    outer@
    for (i in left..right) {
        if (i < 0 || i > input[row].lastIndex) {
            continue@outer
        }

        inner@
        for (j in up..down) {
            if (j < 0 || j > input.lastIndex) {
                continue@inner
            }

            val char = input[i][j]
            if (char.isDigit().not() && char != '.') {
                return true
            }
        }
    }
    return false
}

fun part2(input: List<String>): Int {
    val markedInput = input.map { it.toCharArray().map { false }.toTypedArray() }
    var sum = 0
    input.forEachIndexed { row: Int, line: String ->
        line.forEachIndexed { column: Int, char: Char ->
            if (char == '*') {
                sum += computeGearRatio(
                    input = input,
                    row = row,
                    column = column,
                    markedInput = markedInput
                )
            }
        }
    }
    return sum
}

private fun computeGearRatio(
    input: List<String>,
    row: Int,
    column: Int,
    markedInput: List<Array<Boolean>>,
): Int {
    val left = row - 1
    val right = row + 1
    val up = column - 1
    val down = column + 1

    var firstNumber: Int? = null
    var secondNumber: Int? = null

    outer@
    for (i in left..right) {
        if (i < 0 || i > input[row].lastIndex) {
            continue@outer
        }

        inner@
        for (j in up..down) {
            if (j < 0 || j > input.lastIndex) {
                continue@inner
            }

            val char = input[i][j]
            val isMarked = markedInput[i][j]
            if (char.isDigit() and isMarked.not()) {
                val number = findAndMarkNumber(
                    line = input[i],
                    column = j,
                    markedLine = markedInput[i],
                )
                if (firstNumber == null) {
                    firstNumber = number
                } else if (secondNumber == null) {
                    secondNumber = number
                }
            }
        }
    }

    return if (firstNumber != null && secondNumber != null) {
        firstNumber * secondNumber
    } else 0
}

private fun findAndMarkNumber(
    line: String,
    column: Int,
    markedLine: Array<Boolean>,
): Int {
    var numberString: String = line[column].toString()
    markedLine[column] = true

    var left = column - 1
    while (left >= 0 && line[left].isDigit()) {
        numberString = line[left] + numberString
        markedLine[left] = true
        left -= 1
    }

    var right = column + 1
    while (right <= line.lastIndex && line[right].isDigit()) {
        numberString += line[right]
        markedLine[right] = true
        right += 1
    }

    return numberString.toInt()
}
