package day01

import readInput

fun main() {
    val input = readInput("day01/input")
//    val input = readInput("day01/input_test")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    var sum = 0
    input.forEachIndexed { index: Int, line: String ->
        var firstDigit: Int? = null
        var lastDigit: Int? = null

        line.forEach { char: Char ->
            if (char.isDigit()) {
                if (firstDigit == null) firstDigit = char.digitToInt()
                lastDigit = char.digitToInt()
            }
        }

        firstDigit?.let { first ->
            lastDigit?.let { last ->
                val number = first * 10 + last
                println("Line ${index + 1}: $number")
                sum += number
            }
        }

    }
    return sum
}

fun part2(input: List<String>): Int {
    var sum = 0

    input.forEachIndexed { index: Int, line: String ->
        var firstIndex = Int.MAX_VALUE
        var lastIndex = -1
        var firstDigit: Int? = null
        var lastDigit: Int? = null

        numberStrings.forEachIndexed { index: Int, numberString: String ->
            val firstOccurrence = line.indexOf(numberString)
            if (firstOccurrence in 0..<firstIndex) {
                firstIndex = firstOccurrence
                firstDigit = index + 1
            }

            val lastOccurrence = line.lastIndexOf(numberString)
            if (lastOccurrence > lastIndex) {
                lastIndex = lastOccurrence
                lastDigit = index + 1
            }
        }

        line.forEachIndexed { index: Int, char: Char ->
            if (char.isDigit()) {
                if (index < firstIndex) {
                    firstIndex = index
                    firstDigit = char.digitToInt()
                }

                if (index > lastIndex) {
                    lastIndex = index
                    lastDigit = char.digitToInt()
                }
            }
        }

        firstDigit?.let { first ->
            lastDigit?.let { last ->
                val number = first * 10 + last
                println("Line ${index + 1}: $number")
                sum += number
            }
        }
    }

    return sum
}

private const val ONE = "one"
private const val TWO = "two"
private const val THREE = "three"
private const val FOUR = "four"
private const val FIVE = "five"
private const val SIX = "six"
private const val SEVEN = "seven"
private const val EIGHT = "eight"
private const val NINE = "nine"

private val numberStrings = listOf(ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE)
