package day03

import org.junit.Test
import kotlin.test.assertEquals

class Day03Test {
    companion object {
        val testData = listOf(
            "00100",
            "11110",
            "10110",
            "10111",
            "10101",
            "01111",
            "00111",
            "11100",
            "10000",
            "11001",
            "00010",
            "01010",
        )
    }

    @Test
    fun `count positions`() {
        assertEquals(countPositions(testData), listOf(7, 5, 8, 7, 5))
    }

    @Test
    fun `calculate gamma and epsilon`() {
        assertEquals(calculateGammaEpsilon(testData), Pair(22, 9))
    }

    @Test
    fun `calculate power consumption`() {
        assertEquals((calculateGammaEpsilon(testData).multiply()), 198)
    }

    @Test
    fun `find max min elements in list`() {
        assertEquals(findLifeSupportElements(testData), Pair(23, 10))
    }

    @Test
    fun `find life support rating`() {
        assertEquals(findLifeSupportElements(testData).multiply(), 230)
    }
}