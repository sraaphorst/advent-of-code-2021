package day05

/// day05test.kt
// By Sebastian Raaphorst, 2022.

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day05Test {
    companion object {
        val testData = """
            0,9 -> 5,9
            8,0 -> 0,8
            9,4 -> 3,4
            2,2 -> 2,1
            7,0 -> 7,4
            6,4 -> 2,0
            0,9 -> 2,9
            3,4 -> 1,4
            0,0 -> 8,8
            5,5 -> 8,2
        """.trimIndent()

        val testLines = listOf(
            Line(Coordinate(0, 9), Coordinate(5, 9)),
            Line(Coordinate(8, 0), Coordinate(0, 8)),
            Line(Coordinate(9, 4), Coordinate(3, 4)),
            Line(Coordinate(2, 2), Coordinate(2, 1)),
            Line(Coordinate(7, 0), Coordinate(7, 4)),
            Line(Coordinate(6, 4), Coordinate(2, 0)),
            Line(Coordinate(0, 9), Coordinate(2, 9)),
            Line(Coordinate(3, 4), Coordinate(1, 4)),
            Line(Coordinate(0, 0), Coordinate(8, 8)),
            Line(Coordinate(5, 5), Coordinate(8, 2))
        )

        val lineData = parseLineData(testData)

        private val uc1 = Coordinate(3, 5)
        private val uc2 = Coordinate(0, 8)
        val uline = Line(uc1, uc2)
        val upoints = listOf(Coordinate(0, 8), Coordinate(1, 7),
            Coordinate(2, 6), Coordinate(3, 5))

        private val lc1 = Coordinate(3, 5)
        private val lc2 = Coordinate(6, 8)
        val lline = Line(lc1, lc2)
        val lpoints = listOf(Coordinate(3, 5), Coordinate(4, 6),
            Coordinate(5, 7), Coordinate(6, 8)
        )
    }

    @Test
    fun `parse data test`() {
        assertEquals(lineData, testLines)
    }

    @Test
    fun `number horizontal-vertical danger zones`() {
        assertEquals(findHVDangerZoneCount(lineData), 5)
    }

    @Test
    fun `diagonal upper status`() {
        assertEquals(uline.orientation, Orientation.DIAGONAL)
    }

    @Test
    fun `diagonal upper generation`() {
        assertEquals(uline.generatePoints(), upoints)
    }

    @Test
    fun `diagonal upper containment`() {
        assertTrue(upoints.all { uline.contains(it) })
    }

    @Test
    fun `diagonal lower status`() {
        assertEquals(lline.orientation, Orientation.DIAGONAL)
    }

    @Test
    fun `diagonal lower generation`() {
        assertEquals(lline.generatePoints(), lpoints)
    }

    @Test
    fun `diagonal lower containment`() {
        assertTrue(lpoints.all { lline.contains(it) })
    }

    @Test
    fun `number danger zones`() {
        val abc = findAllDangerZoneCount(lineData)
        assertEquals(findAllDangerZoneCount(lineData), 12)
    }
}