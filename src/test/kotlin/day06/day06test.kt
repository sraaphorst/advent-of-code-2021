package day06

// day06test.kt
// By Sebastian Raaphorst, 2022.

import org.junit.Test
import kotlin.test.assertEquals

class Day06Test {
    companion object {
        private fun bi(i: Int): Count = Count.valueOf(i.toLong())

        const val initialPopulationString = "3,4,3,1,2"
        val initialPopulation = LanternFish(listOf(1 to bi(1), 2 to bi(1), 3 to bi(2), 4 to bi(1)).toMap())

        const val day1PopulationString = "2,3,2,0,1"
        const val day2PopulationString = "1,2,1,6,0,8"
        const val day3PopulationString = "0,1,0,5,6,7,8"
        const val day4PopulationString = "6,0,6,4,5,6,7,8,8"
        const val day5PopulationString = "5,6,5,3,4,5,6,7,7,8"
        const val day6PopulationString = "4,5,4,2,3,4,5,6,6,7"
        const val day7PopulationString = "3,4,3,1,2,3,4,5,5,6"
        const val day8PopulationString = "2,3,2,0,1,2,3,4,4,5"
        const val day9PopulationString = "1,2,1,6,0,1,2,3,3,4,8"
        const val day10PopulationString = "0,1,0,5,6,0,1,2,2,3,7,8"
        const val day11PopulationString = "6,0,6,4,5,6,0,1,1,2,6,7,8,8,8"
        const val day12PopulationString = "5,6,5,3,4,5,6,0,0,1,5,6,7,7,7,8,8"
        const val day13PopulationString = "4,5,4,2,3,4,5,6,6,0,4,5,6,6,6,7,7,8,8"
        const val day14PopulationString = "3,4,3,1,2,3,4,5,5,6,3,4,5,5,5,6,6,7,7,8"
        const val day15PopulationString = "2,3,2,0,1,2,3,4,4,5,2,3,4,4,4,5,5,6,6,7"
        const val day16PopulationString = "1,2,1,6,0,1,2,3,3,4,1,2,3,3,3,4,4,5,5,6,8"
        const val day17PopulationString = "0,1,0,5,6,0,1,2,2,3,0,1,2,2,2,3,3,4,4,5,7,8"
        const val day18PopulationString = "6,0,6,4,5,6,0,1,1,2,6,0,1,1,1,2,2,3,3,4,6,7,8,8,8,8"

    }

    @Test
    fun `parse population`() {
        assertEquals(parsePopulation(initialPopulationString), initialPopulation)
    }

    @Test
    fun `day 1`() {
        assertEquals(lanternfish(initialPopulation, 1), parsePopulation(day1PopulationString))
    }

    @Test
    fun `day 2`() {
        assertEquals(lanternfish(initialPopulation, 2), parsePopulation(day2PopulationString))
    }

    @Test
    fun `day 3`() {
        assertEquals(lanternfish(initialPopulation, 3), parsePopulation(day3PopulationString))
    }

    @Test
    fun `day 4`() {
        assertEquals(lanternfish(initialPopulation, 4), parsePopulation(day4PopulationString))
    }

    @Test
    fun `day 5`() {
        assertEquals(lanternfish(initialPopulation, 5), parsePopulation(day5PopulationString))
    }

    @Test
    fun `day 6`() {
        assertEquals(lanternfish(initialPopulation, 6), parsePopulation(day6PopulationString))
    }

    @Test
    fun `day 7`() {
        assertEquals(lanternfish(initialPopulation, 7), parsePopulation(day7PopulationString))
    }

    @Test
    fun `day 8`() {
        assertEquals(lanternfish(initialPopulation, 8), parsePopulation(day8PopulationString))
    }

    @Test
    fun `day 9`() {
        assertEquals(lanternfish(initialPopulation, 9), parsePopulation(day9PopulationString))
    }

    @Test
    fun `day 10`() {
        assertEquals(lanternfish(initialPopulation, 10), parsePopulation(day10PopulationString))
    }

    @Test
    fun `day 11`() {
        assertEquals(lanternfish(initialPopulation, 11), parsePopulation(day11PopulationString))
    }

    @Test
    fun `day 12`() {
        assertEquals(lanternfish(initialPopulation, 12), parsePopulation(day12PopulationString))
    }

    @Test
    fun `day 13`() {
        assertEquals(lanternfish(initialPopulation, 13), parsePopulation(day13PopulationString))
    }

    @Test
    fun `day 14`() {
        assertEquals(lanternfish(initialPopulation, 14), parsePopulation(day14PopulationString))
    }

    @Test
    fun `day 15`() {
        assertEquals(lanternfish(initialPopulation, 15), parsePopulation(day15PopulationString))
    }

    @Test
    fun `day 16`() {
        assertEquals(lanternfish(initialPopulation, 16), parsePopulation(day16PopulationString))
    }

    @Test
    fun `day 17`() {
        assertEquals(lanternfish(initialPopulation, 17), parsePopulation(day17PopulationString))
    }

    @Test
    fun `day 18`() {
        assertEquals(lanternfish(initialPopulation, 18), parsePopulation(day18PopulationString))
    }

    @Test
    fun `fish on day 18`() {
        assertEquals(lanternfish(initialPopulation, 18).populationSize(), Count.valueOf(26))
    }

    @Test
    fun `fish on day 80`() {
        assertEquals(lanternfish(initialPopulation, 80).populationSize(), Count.valueOf(5934))
    }

    @Test
    fun `fish on day 256`() {
        assertEquals(lanternfish(initialPopulation, 256).populationSize(), Count.valueOf(26984457539L))
    }
}