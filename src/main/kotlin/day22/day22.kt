package day22

// day22.kt
// By Sebastian Raaphorst, 2022.

import kotlinx.coroutines.runBlocking

typealias Position = Triple<Int, Int, Int>

enum class Action {
    ON,
    OFF
}

fun combineCoordinates(xRange: List<Int>, yRange: List<Int>, zRange: List<Int>): Iterable<Position> =
        xRange.flatMap { x -> yRange.flatMap { y -> zRange.map { z -> Triple(x, y, z) } } }.toSet()

data class Step(val action: Action, val xRange: IntRange, val yRange: IntRange, val zRange: IntRange) {
    companion object {
        fun parse(input: String): Step {
            fun parseIntRange(str: String): IntRange {
                val start = str.drop(2).takeWhile { it == '-' || it.isDigit() }.toInt()
                val end = str.takeLastWhile { it == '-' || it.isDigit() }.toInt()
                return start..end
            }

            val action = Action.valueOf(input.split(' ').first().uppercase())
            val ranges = input.drop(action.name.length + 1).split(',').map{ parseIntRange(it) }
            return Step(action, ranges[0], ranges[1], ranges[2])
        }
    }
}

data class Reactor(val minmax: IntRange, val cubes: Set<Position> = setOf()) {
    fun executeStep(step: Step): Reactor = when (step.action) {
        Action.ON -> Reactor(minmax, cubes.union(combineCoordinates(
                step.xRange.filter { it in minmax },
                step.yRange.filter { it in minmax },
                step.zRange.filter { it in minmax }
        )))
        Action.OFF -> Reactor(minmax, cubes.subtract(combineCoordinates(
                step.xRange.filter { it in minmax },
                step.yRange.filter { it in minmax },
                step.zRange.filter { it in minmax }
        ).toSet()))
    }
}


fun main(): Unit = runBlocking {
    val steps = object {}.javaClass.getResource("/day22.txt")!!
            .readText().trim().split("\n")
            .map(Step::parse)

    println("--- Day 22: Reactor Reboot ---\n")

    // Answer: 623748
    println("Part 1 reboot: ${steps.fold(Reactor(-50..50)) { r, s -> r.executeStep(s) }.cubes.size}")
}