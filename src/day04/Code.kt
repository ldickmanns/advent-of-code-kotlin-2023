package day04

import readInput
import kotlin.math.min

fun main() {
    val input = readInput("day04/input")
//    val input = readInput("day04/input_test")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    var valueSum = 0
    input.forEach { line: String ->
        val withoutCardAndNumber = line.substringAfter(':')
        val winningNumbers: Set<Int> = withoutCardAndNumber
            .substringBefore('|')
            .trim()
            .split(' ')
            .filter { it != "" }
            .map { it.toInt() }
            .toHashSet()
        val myNumbers = withoutCardAndNumber
            .substringAfter('|')
            .trim()
            .split(' ')
            .filter { it != "" }
            .map { it.toInt() }

        var cardValue = 0
        myNumbers.forEach { number ->
            if (number in winningNumbers) {
                if (cardValue == 0) cardValue = 1
                else cardValue += cardValue
            }
        }

        valueSum += cardValue
    }
    return valueSum
}

fun part2(input: List<String>): Int {
    var numberOfCards = 0

    val cardMap = mutableMapOf<Int, Int>()
    input.indices.forEach { cardMap[it] = 1 } // original cards

    input.forEachIndexed { index, line ->
        // add the original card
        val numberOfCurrentCard = cardMap[index] ?: throw IllegalStateException()
        numberOfCards += numberOfCurrentCard
        println("Number of cards ${cardMap[index]}")

        val withoutCardAndNumber = line.substringAfter(':')
        val winningNumbers: Set<Int> = withoutCardAndNumber
            .substringBefore('|')
            .trim()
            .split(' ')
            .filter { it != "" }
            .map { it.toInt() }
            .toHashSet()
        val myNumbers = withoutCardAndNumber
            .substringAfter('|')
            .trim()
            .split(' ')
            .filter { it != "" }
            .map { it.toInt() }

        var winningCards = 0
        myNumbers.forEach { number ->
            if (number in winningNumbers) {
                winningCards += 1
            }
        }

        val nextIndex = index + 1
        val endIndex = min(index + winningCards, input.lastIndex)

        (nextIndex..endIndex).forEach { indexToAddCopies ->
            val numberBeforeAddingCopies = cardMap[indexToAddCopies] ?: throw IllegalStateException()
            cardMap[indexToAddCopies] = numberBeforeAddingCopies + numberOfCurrentCard
        }
    }

    return numberOfCards
}
