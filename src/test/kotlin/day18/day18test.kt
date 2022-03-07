package day18

import day18.SnailfishNumberMonoid.combine
import org.junit.Test
import kotlin.test.assertEquals

class Day18Test {
    companion object {
        val sfinput = """
            [1,2]
            [[1,2],3]
            [9,[8,7]]
            [[1,9],[8,5]]
            [[[[1,2],[3,4]],[[5,6],[7,8]]],9]
            [[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]
            [[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]
        """.trimIndent().split('\n')
    }

    @Test
    fun `test parsing`() {
        assertEquals(sfinput.map { it.parseSnailFishNumber().toString() }, sfinput)
    }

    @Test
    fun `test addition`() {
        val sf1 = "[1,2]".parseSnailFishNumber()
        val sf2 = "[[3,4],5]".parseSnailFishNumber()
        val result = "[[1,2],[[3,4],5]]".parseSnailFishNumber()
        assertEquals(sf1.combine(sf2), result)
    }
}