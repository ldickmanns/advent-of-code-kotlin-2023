package day05

import println
import readInput

@JvmInline
value class SourceRange(val range: LongRange)

@JvmInline
value class DestinationRange(val range: LongRange)

fun main() {
    val input = readInput("day05/input")
//    val input = readInput("day05/input_test")

    println(part1(input))
    println(part2(input))
}


fun part1(input: List<String>): Long {
    val seeds = input
        .first()
        .substringAfter(':')
        .trim()
        .split(' ')
        .map { it.toLong() }


    val maps = parseMaps(input = input)

    val locations =
        maps.fold(seeds) { currentValues: List<Long>, sourceDestinationMap: Map<SourceRange, DestinationRange> ->
            val newList = mutableListOf<Long>()

            outer@
            for (value in currentValues) {
                for ((sourceRange, destinationRange) in sourceDestinationMap) {
                    if (value in sourceRange.range) {
                        val difference = value - sourceRange.range.first
                        val newValue = destinationRange.range.first + difference
                        newList.add(newValue)
                        continue@outer
                    }
                }
                newList.add(value)
            }

            newList
        }

    return locations.min()
}

private fun parseMaps(
    input: List<String>,
): List<Map<SourceRange, DestinationRange>> {
    val sourceDestinationMaps = mutableListOf<Map<SourceRange, DestinationRange>>()
    val withoutSeeds = input.drop(2)

    var currentMap: MutableMap<SourceRange, DestinationRange>? = null
    withoutSeeds.forEachIndexed { index, line ->
        if (currentMap == null) {
            currentMap = mutableMapOf()
            return@forEachIndexed
        }
        if (line.isEmpty() || index == withoutSeeds.lastIndex) {
            currentMap?.let { sourceDestinationMaps.add(it) } ?: throw IllegalStateException()
            currentMap = null
            return@forEachIndexed
        }

        val (sourceRange, destinationRange) = parseSourceAndDestinationRange(rangeString = line)
        currentMap?.let {
            it[sourceRange] = destinationRange
        } ?: throw IllegalStateException()
    }

    return sourceDestinationMaps
}

// destination range start, source range start, range length
private fun parseSourceAndDestinationRange(
    rangeString: String
): Pair<SourceRange, DestinationRange> {
    val numbers = rangeString
        .trim()
        .split(' ')
        .map { it.toLong() }

    val destinationRangeStart = numbers[0]
    val sourceRangeStart = numbers[1]
    val rangeLength = numbers[2]

    val destinationRangeEnd = destinationRangeStart + rangeLength - 1
    val sourceRangeEnd = sourceRangeStart + rangeLength - 1

    return Pair(
        first = SourceRange(sourceRangeStart..sourceRangeEnd),
        second = DestinationRange(destinationRangeStart..destinationRangeEnd)
    )
}

fun part2(input: List<String>): Long {
    val seedsRanges = input
        .first()
        .substringAfter(':')
        .trim()
        .split(' ')
        .map { it.toLong() }
        .windowed(size = 2, step = 2)
        .map { list ->
            val rangeStart = list[0]
            val rangeLength = list[1]
            val rangeEnd = rangeStart + rangeLength - 1
            (rangeStart..rangeEnd)
        }.also { it.println() }

    val maps = parseMaps(input = input)

    var minValue = Long.MAX_VALUE

    var currentRangeIndex = 0
    seedsRanges.forEach { seedsRange: LongRange ->
        println("Currently processing range ${currentRangeIndex + 1}/${seedsRanges.size}")
        currentRangeIndex++

        println("Processing ${seedsRange.last - seedsRange.first + 1} items")
        seedsRange.forEach { seed: Long ->
            if (seed % 10_000_000L == 0L) {
                println("Processing seed $seed in range $seedsRange")
            }

            val location = maps.fold(seed) { currentValue, sourceDestinationMap ->
                    for ((sourceRange, destinationRange) in sourceDestinationMap) {
                        if (currentValue in sourceRange.range) {
                            val difference = currentValue - sourceRange.range.first
                            return@fold destinationRange.range.first + difference
                        }
                    }
                    currentValue
                }
            if (location < minValue) minValue = location
        }
    }

    return minValue
}
