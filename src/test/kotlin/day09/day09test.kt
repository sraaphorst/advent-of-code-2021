package day09

// day09test.kt
// By Sebastian Raaphorst, 2022.

import org.junit.Test
import kotlin.test.assertEquals

class Day09Test {
    companion object {
        private val input = """
            2199943210
            3987894921
            9856789892
            8767896789
            9899965678
        """.trimIndent()

        val heightMap: HeightMap = parseHeightMap(input)
    }

    @Test
    fun `risk level sum`() {
        assertEquals(riskLevelSum(heightMap), 15)
    }
}