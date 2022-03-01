package day07

// day07test.kt
// By Sebastian Raaphorst, 2022.

import org.junit.Test
import kotlin.test.assertEquals

class Day07Test {
    companion object {
        const val initPositions = "16,1,2,0,4,2,7,1,2,14"
        val positions = listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14)
    }

    @Test
    fun `parse positions`() {
        assertEquals(parsePositions(initPositions), positions)
    }

    @Test
    fun `move to position 2`() {
        assertEquals(determineFuelCost(positions, 2), 37)
    }

    @Test
    fun `move to position 1`() {
        assertEquals(determineFuelCost(positions, 1), 41)
    }

    @Test
    fun `move to position 3`() {
        assertEquals(determineFuelCost(positions, 3), 39)
    }

    @Test
    fun `move to position 10`() {
        assertEquals(determineFuelCost(positions, 10), 71)
    }

    @Test
    fun `move to minimum position`() {
        assertEquals(determineMinimumFuelCost(positions, fuel1), 37)
    }

    @Test
    fun `median is best position`() {
        assertEquals(determineMedianFuelCost(positions, fuel1), 37)
    }

    @Test
    fun `move to position 5 with crab fuel`() {
        assertEquals(determineFuelCost(positions, 5, fuel2), 168)
    }

    @Test
    fun `move to position 2 with crab fuel`() {
        assertEquals(determineFuelCost(positions, 2, fuel2), 206)
    }
}