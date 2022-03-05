package day11

// day11test.kt
// By Sebastian Raaphorst, 2022.

import org.junit.Test
import kotlin.test.assertEquals

class Day11Test {
    companion object {
        val octopusInfo = """
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526
        """.trimIndent()

        val octopusGrid = octopusInfo.split('\n').toOctopusGrid()
    }

    @Test
    fun `after 10 steps`() {
        assertEquals(octopusGrid.steps(10), 204)
    }

    @Test
    fun `after 100 steps`() {
        assertEquals(octopusGrid.steps(100), 1656)
    }

    @Test
    fun `first full flash`() {
        assertEquals(octopusGrid.firstFullFlash(), 195)
    }
}