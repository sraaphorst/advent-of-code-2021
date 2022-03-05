package day10

// day10test.kt
// By Sebastian Raaphorst, 2022.

import org.junit.Test
import kotlin.test.assertEquals

class Day10Test {
    companion object {
        private val input = """
            [({(<(())[]>[[{[]{<()<>>
            [(()[<>])]({[<{<<[]>>(
            {([(<{}[<>[]}>{[]{[(<()>
            (((({<>}<{<{<>}{[]{[]{}
            [[<[([]))<([[{}[[()]]]
            [{[{({}]{}}([{[{{{}}([]
            {<[[]]>}<{[{[{[]{()[[[]
            [<(<(<(<{}))><([]([]()
            <{([([[(<>()){}]>(<<{{
            <{([{{}}[<[[[<>{}]]]>[]]
        """.trimIndent()

        val chunkList = input.split('\n').toChunkList()
    }

    @Test
    fun `chunk list score`() {
        assertEquals(chunkList.score(), 26397)
    }

    @Test
    fun `chunk list completion score`() {
        assertEquals(chunkList.completionScore(), 288957L)
    }
}