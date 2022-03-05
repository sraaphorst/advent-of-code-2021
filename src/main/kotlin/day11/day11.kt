package day11

// day11.kt
// By Sebastian Raaphorst, 2022.

import kotlinx.coroutines.runBlocking

typealias OctopusRow = List<Int?>
typealias OctopusGrid = List<OctopusRow>

// All directions around a tile, including diagonals.
val deltas = (-1..1).flatMap { x -> (-1..1).map { y -> Pair(x, y) } }


// Useful function that should just be in Kotlin
fun <T> Iterable<T?>.countNull(): Int =
    count { it == null }


// Accumulate flashes.
fun Pair<OctopusGrid, Int>.advance(new: Pair<OctopusGrid, Int>) =
    Pair(new.first, second + new.second)


// Calculate the new octopus configuration and then number of flashes of the octopuses during the step.
fun OctopusGrid.step(): Pair<OctopusGrid, Int> {
    val heights = indices
    val widths = first().indices

    // Calculate the neighbours of position. A position is not its own neighbour.
    fun neighbours(p: Pair<Int, Int>): List<Pair<Int, Int>> =
        deltas.map { Pair(it.first + p.first, it.second + p.second) }
            .filter { it !== Pair(0, 0) && heights.contains(it.first) && widths.contains(it.second) }

    // Remember: ALL octopuses are to increase energy levels during the step, so we set the initial
    // octopusToIncrease to the entire grid.
    // Also note that an octopus can be scheduled to increase MULTIPLE times if it has multiple neighbours that flash.

    // Thus, assume we are increasing the energy levels of all octopuses unless told otherwise.
    // Mark flashed octopuses with an energy level of null to indicate that they have flashed.
    // IDEA: Increase all octopus indicated to increase by 1. Null octopus stay the same.
    // Scan the grid. For any octopus > 9, set to null and add their neighbours to the next octopusToIncrease.
    // Repeat until octopusToIncrease is empty.
    tailrec fun aux(octopusGrid: OctopusGrid,
                    octopusToIncrease: List<Pair<Int, Int>> = heights.flatMap { x -> widths.map { y -> Pair(x, y) } }): Pair<OctopusGrid, Int> {
        // We have to normalize the octopus grid by setting any with energy level > 9 to 0.
        if (octopusToIncrease.isEmpty())
            return Pair(octopusGrid.map { row -> row.map { it ?: 0 } },
                octopusGrid.sumOf(OctopusRow::countNull))

        // Increase the energy levels of all octopus to increase and make the new grid.
        val newOctopusGrid = heights.map { x -> widths.map { y ->
            octopusGrid[x][y]?.plus(octopusToIncrease.count { it.first == x && it.second == y })
        } }

        // All the energy levels are now increased.
        // At this point, the octopus to increase have:
        // 1. A non-null energy level; and
        // 2. a neighbour with an energy level > 9.
        val newOctopusToIncrease = heights.flatMap { x -> widths.flatMap { y ->
            val energy = newOctopusGrid[x][y]
            if (energy != null && energy > 9)
                neighbours(Pair(x, y)).filter { newOctopusGrid[it.first][it.second] != null }
            else
                emptyList()
        } }

        // Filter any flashed octopuses down to null.
        val nullifiedOctopusGrid = heights.map { x -> widths.map { y ->
            val energy = newOctopusGrid[x][y]
            if (energy == null || energy > 9) null else energy
        } }

        return aux(nullifiedOctopusGrid, newOctopusToIncrease)
    }

    return aux(this)
}


// This method performs n steps, keeps track of the number of flashes, and advances to the next octopus configuration
// after each step.
fun OctopusGrid.steps(n: Int): Int =
    (0 until n).fold(Pair(this, 0)){ p, _ -> p.advance(p.first.step())}.second


// Determine the first step where all the octopuses flash at once.
fun OctopusGrid.firstFullFlash(): Int {
    tailrec fun aux(grid: OctopusGrid, steps: Int = 0): Int = when {
        grid.all { row -> row.all { it == 0 } } -> steps
        else -> aux(grid.step().first, steps + 1)
    }

    return aux(this)
}


fun String.toOctopusRow(): OctopusRow =
    map(Char::digitToInt)


fun List<String>.toOctopusGrid(): OctopusGrid =
    map(String::toOctopusRow)


fun main() = runBlocking {
    val octopusGrid = object {}.javaClass.getResource("/day11.txt")!!
        .readText().trim().split('\n').toOctopusGrid()

    println("--- Day 11: Dumbo Octopus ---\n")

    // Answer: 387363
    println("Part 1: Number of flashed octopuses: ${octopusGrid.steps(100)}")

    // Answer: 227
    println("Part 2: Step of first full flash: ${octopusGrid.firstFullFlash()}")
}