package day02

import arrow.core.right
import org.junit.Test
import kotlin.test.assertEquals

class Day02Test {
    companion object {
        val testData = listOf(
            "forward 5",
            "down 5",
            "forward 8",
            "up 3",
            "down 8",
            "forward 2"
        )
    }

    @Test
    fun example1() {
        assertEquals(calculateCoordinates(testData, false), Pair(15, 10).right())
    }

    @Test
    fun example2() {
        assertEquals(calculateCoordinateProduct(testData, false), 150.right())
    }

    @Test
    fun example3() {
        assertEquals(calculateCoordinates(testData, true), Pair(15, 60).right())
    }

    @Test
    fun example4() {
        assertEquals(calculateCoordinateProduct(testData, true), 900.right())
    }
}