package day20

// day20test.kt
// By Sebastian Raaphorst, 2022.

import org.junit.Test
import kotlin.test.assertEquals

class Day20Test {
    companion object {
        val encoding = """
            ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##
            #..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###
            .######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#.
            .#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#.....
            .#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#..
            ...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.....
            ..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#
        """.trimIndent().filterNot { it == '\n' }.parseEncoding()

        val image = """
            #..#.
            #....
            ##..#
            ..#..
            ..###
        """.trimIndent().split('\n').parseImage()

        val image2 = """
            .##.##.
            #..#.#.
            ##.#..#
            ####..#
            .#..##.
            ..##..#
            ...#.#.
        """.trimIndent().split('\n').parseImage().map { (x,y) ->(x-1) to (y-1) }.toSet()

        val image3 = """
            .......#.
            .#..#.#..
            #.#...###
            #...##.#.
            #.....#.#
            .#.#####.
            ..#.#####
            ...##.##.
            ....###..
        """.trimIndent().split('\n').parseImage().map { (x,y) -> (x-2) to (y-2) }.toSet()
    }

    @Test
    fun `evolveN with 1`() {
        assertEquals(enhanceN(image, encoding, 1), image2)
    }

    @Test
    fun `evolveN with 2`() {
        assertEquals(enhanceN(image, encoding, 2), image3)
    }

    @Test
    fun `num lights once`() {
        assertEquals(enhanceN(image, encoding, 1).size, 24)
    }

    @Test
    fun `num lights twice`() {
        assertEquals(enhanceN(image, encoding, 2).size, 35)
    }

    @Test
    fun `num lights 50`() {
        assertEquals(enhanceN(image, encoding, 50).size, 3351)
    }
}