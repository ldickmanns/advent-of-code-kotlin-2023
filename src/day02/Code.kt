package day02

import readInput

private val maxColorMap = mapOf(
    "red" to 12,
    "green" to 13,
    "blue" to 14,
)

fun main() {
     val input = readInput("day02/input")
//    val input = readInput("day02/input_test")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    var validGameNumberSum = 0
    input.forEach { line ->
        var isValidGame = true
        val gameNumber = line
            .substringBefore(':')
            .removePrefix("Game ")
            .toInt()

        val draws: List<String> = line
            .substringAfter(':')
            .split(';')

        draws.forEach { draw: String ->
            val colorDraws = draw.split(',')
            colorDraws.forEach { colorDraw ->
                val trimmed = colorDraw.trim()
                val amount = trimmed
                    .substringBefore(' ')
                    .toInt()

                val color = trimmed.substringAfter(' ')
                val maxAmount = maxColorMap[color] ?: throw IllegalStateException()
                if (amount > maxAmount) isValidGame = false
            }
        }

        if (isValidGame) validGameNumberSum += gameNumber
    }
    return validGameNumberSum
}

fun part2(input: List<String>): Int {
    var powerSum = 0
    input.forEach { line ->
        val minRequiredOfColorMap = mutableMapOf<String, Int>()

        val draws: List<String> = line
            .substringAfter(':')
            .split(';')

        draws.forEach { draw: String ->
            val colorDraws = draw.split(',')
            colorDraws.forEach { colorDraw ->
                val trimmed = colorDraw.trim()
                val amount = trimmed
                    .substringBefore(' ')
                    .toInt()

                val color = trimmed.substringAfter(' ')
                val minRequiredOfColor = minRequiredOfColorMap[color]
                if (minRequiredOfColor == null) {
                    minRequiredOfColorMap[color] = amount
                } else if (amount > minRequiredOfColor) {
                    minRequiredOfColorMap[color] = amount
                }
            }
        }

        powerSum += minRequiredOfColorMap
            .values
            .fold(1) { acc, item -> acc * item }
    }
    return powerSum
}
