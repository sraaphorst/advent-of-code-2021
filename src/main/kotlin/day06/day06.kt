package day06

// day06.kt
// By Sebastian Raaphorst, 2022.

import kotlinx.coroutines.runBlocking
import java.math.BigInteger

typealias Timer = Int
typealias Count = BigInteger
typealias Population = Map<Timer, Count>

const val TIMER_RESET = 6
const val TIMER_NEW = 8
const val TIMER_SPAWN = -1

class LanternFish(val population: Population) {
    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        other as LanternFish
        return (population.keys + other.population.keys).all {
            population.getOrDefault(it, Count.ZERO) == other.population.getOrDefault(it, Count.ZERO)
        }
    }

    override fun hashCode(): Int =
        (0..8).map{ it to population.getOrDefault(it, Count.ZERO)}.hashCode()

    override fun toString(): String =
        "Lanternfish($population)"

    fun populationSize(): Count =
        population.map { it.value }.fold(Count.ZERO) { acc, curr -> acc.add(curr) }
}

tailrec fun lanternfish(population: LanternFish, days: Int): LanternFish {
    if (days == 0)
        return population

    // Determine how many lanternfish with TIMER_RESET there should be this day.
    // This equates to the hatchlings that are one day older, and the population that will just spawn.
    val resetCount = population.population.getOrDefault(TIMER_RESET + 1, Count.ZERO) +
            population.population.getOrDefault(TIMER_SPAWN + 1, Count.ZERO)

    // Determine how many new lanternfish there should be this day.
    val newCount = population.population.getOrDefault(TIMER_SPAWN + 1, Count.ZERO)

    // Now decrease the rest of the population
    // 0, 1, 2, 3, 4, 5, 7
    val newPopulation = (setOf(1, 2, 3, 4, 5, 6, 8).map{ it-1 to population.population.getOrDefault(it, Count.ZERO) } +
            listOf(TIMER_RESET to resetCount, TIMER_NEW to newCount)).toMap()

    return lanternfish(LanternFish(newPopulation), days-1)
}

fun parsePopulation(string: String): LanternFish {
    val stages = string.split(',').map(String::toInt)
    return LanternFish((0..8).associateWith { s -> stages.count { it == s }.toBigInteger() })
}

fun main(): Unit = runBlocking {
    val population = parsePopulation(object {}.javaClass.getResource("/day06.txt")!!.readText().trim())

    println("--- Day 6: Lanternfish ---\n")

    // Answer: 374994
    println("Part 1: Population after 80 days: ${lanternfish(population, 80).populationSize()}")

    // Answer: 1686252324092
    println("Part 2: Population after 256 days: ${lanternfish(population, 256).populationSize()}")
}