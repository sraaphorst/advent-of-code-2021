package day01

import org.junit.Test
import kotlin.test.assertEquals

class Day01Test {
    companion object {
        val testData = listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)
    }

    @Test
    fun example1() {
        assertEquals(calculateDepthIncreases(testData), 7)
    }

    @Test
    fun example2() {
        assertEquals(calculateWindows(testData), listOf(607, 618, 618, 617, 647, 716, 769, 792))
    }

    @Test
    fun example3() {
        assertEquals(calculateDepthIncreases(calculateWindows(testData)), 5)
    }
}