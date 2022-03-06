package day13

// day12test.kt
// By Sebastian Raaphorst, 2022.

import org.junit.Test
import kotlin.test.assertEquals

class Day13Test {
    companion object {
        private val input = """
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0

            fold along y=7
            fold along x=5
        """.trimIndent()

        private val parsed = input.parseOrigami()
        val paper = parsed.first
        val folds = parsed.second

        val render1 = """
            ...#..#..#.
            ....#......
            ...........
            #..........
            ...#....#.#
            ...........
            ...........
            ...........
            ...........
            ...........
            .#....#.##.
            ....#......
            ......#...#
            #..........
            #.#........
        """.trimIndent()

        val render2 = """
            #.##..#..#.
            #...#......
            ......#...#
            #...#......
            .#.#..#.###
            ...........
            ...........
        """.trimIndent()

        val render3 = """
            #####
            #...#
            #...#
            #...#
            #####
            .....
            .....
        """.trimIndent()
    }

    @Test
    fun `folds parsed`() {
        assertEquals(folds, listOf(Fold(Axis.Y, 7), Fold(Axis.X, 5)))
    }

    @Test
    fun `paper parsed and properly renders`() {
        assertEquals(paper.toDiagram(), render1)
    }

    @Test
    fun `paper folded at y=7`() {
        assertEquals(paper.executeFold(Fold(Axis.Y, 7)).toDiagram(), render2)
    }

    @Test
    fun `paper folded at y=7, x=5`() {
        assertEquals(paper.executeFold(Fold(Axis.Y, 7)).executeFold(Fold(Axis.X, 5)).toDiagram(), render3)
    }
}