package day07

// day07.kt
// By Sebastian Raaphorst, 2022.

import kotlinx.coroutines.runBlocking
import java.util.Collections.max
import java.util.Collections.min
import kotlin.math.abs

// More than one crab can occupy a position.
typealias CrabPosition=Int
typealias CrabPositions=List<CrabPosition>

typealias Fuel=Int
typealias FuelFunction=(Fuel) -> Fuel
val fuel1: FuelFunction = {it}
val fuel2: FuelFunction = {(it+1) * it / 2}

fun parsePositions(input: String): CrabPositions =
    input.trim().split(',').map(String::toInt)

// In Part 1, n moves takes n fuel.
// In Part 2, n moves takes (n+1)n/2 fuel.
fun determineFuelCost(positions: CrabPositions, position: CrabPosition, fuel: FuelFunction = fuel1): Fuel =
    positions.sumOf { fuel(abs(it - position)) }

fun determineMedianFuelCost(positions: CrabPositions, fuel: FuelFunction): Fuel =
    determineFuelCost(positions, positions.sorted()[positions.size / 2], fuel)

fun determineMinimumFuelCost(positions: CrabPositions, fuel: FuelFunction): Fuel =
    min((min(positions)..max(positions)).map { determineFuelCost(positions, it, fuel) })

fun main() = runBlocking {
    val positions = parsePositions(object {}.javaClass.getResource("/day07.txt")!!.readText())

    println("--- Day 7: The Treachery of Whales ---\n")

    // The median position is the position that costs the least fuel.
    // Answer: 340056
    println("Part 1: Minimum crab fuel usage: ${determineMedianFuelCost(positions, fuel1)}")

    // There probably is a better way than brute forcing this, as we did in the first part.
    // Answer: 96592275
    println("Part 2: Minimum crab fuel usage: ${determineMinimumFuelCost(positions, fuel2)}")
}