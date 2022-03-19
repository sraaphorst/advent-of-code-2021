package day14

// day14test.kt
// By Sebastian Raaphorst, 2022.

import org.junit.Test
import java.math.BigInteger
import kotlin.test.assertEquals

class Day14Test {
    companion object {
        private val input = """
            NNCB

            CH -> B
            HH -> N
            CB -> H
            NH -> C
            HB -> C
            HC -> B
            HN -> C
            NN -> C
            BH -> H
            NC -> B
            NB -> B
            BN -> B
            BB -> N
            BC -> B
            CC -> N
            CN -> C
        """.trimIndent()

        val initial = "NNCB"
        val rules = listOf(
                ('C' to 'H') to 'B',
                ('H' to 'H') to 'N',
                ('C' to 'B') to 'H',
                ('N' to 'H') to 'C',
                ('H' to 'B') to 'C',
                ('H' to 'C') to 'B',
                ('H' to 'N') to 'C',
                ('N' to 'N') to 'C',
                ('B' to 'H') to 'H',
                ('N' to 'C') to 'B',
                ('N' to 'B') to 'B',
                ('B' to 'N') to 'B',
                ('B' to 'B') to 'N',
                ('B' to 'C') to 'B',
                ('C' to 'C') to 'N',
                ('C' to 'N') to 'C'
        ).toMap()
    }

    @Test
    fun `input parses initial string`() {
        assertEquals(input.parsePolymerizationInput().first, initial)
    }

    @Test
    fun `input parses rules`() {
        assertEquals(input.parsePolymerizationInput().second, rules)
    }

    @Test
    fun `one step`() {
        assertEquals(initial.step(rules), "NCNBCHB")
    }

    @Test
    fun `two steps`() {
        assertEquals(initial.steps(2, rules), "NBCCNBBBCBHCB")
    }

    @Test
    fun `three steps`() {
        assertEquals(initial.steps(3, rules), "NBBBCNCCNBBNBNBBCHBHHBCHB")
    }

    @Test
    fun `four steps`() {
        assertEquals(initial.steps(4, rules), "NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB")
    }

    @Test
    fun `five steps`() {
        assertEquals(initial.steps(5, rules).length,97)
    }

    @Test
    fun `10 steps`() {
        assertEquals(initial.steps(10, rules).length,3073)
    }

    @Test
    fun `10 step counts`() {
        assertEquals(initial.steps(10, rules).counts(), listOf(
            'B' to 1749, 'C' to 298, 'H' to 161, 'N' to 865
        ).toMap())
    }

    @Test
    fun `10 step max-min difference`() {
        assertEquals(initial.steps(10, rules).maxMinDiff(),1588)
    }

    @Test
    fun `no step large counts`() {
        assertEquals(initial.countsForLargeSteps(0, rules), listOf(
                'B' to BigInteger.ONE,
                'C' to BigInteger.ONE,
                'N' to BigInteger.TWO
        ).toMap())
    }

    @Test
    fun `one step large counts`() {
        assertEquals(initial.countsForLargeSteps(1, rules), listOf(
                'H' to BigInteger.ONE,
                'B' to BigInteger.TWO,
                'C' to BigInteger.TWO,
                'N' to BigInteger.TWO
        ).toMap())
    }

    @Test
    fun `two steps large counts`() {
        assertEquals(initial.countsForLargeSteps(2, rules), listOf(
                'H' to BigInteger.ONE,
                'B' to BigInteger.valueOf(6L),
                'C' to BigInteger.valueOf(4L),
                'N' to BigInteger.TWO
        ).toMap())
    }

    @Test
    fun `10 step large counts`() {
        assertEquals(initial.countsForLargeSteps(10, rules), listOf(
                'B' to BigInteger.valueOf(1749L),
                'C' to BigInteger.valueOf(298L),
                'H' to BigInteger.valueOf(161L),
                'N' to BigInteger.valueOf(865L)
        ).toMap())
    }

    @Test
    fun `40 step max-min difference`() {
        assertEquals(initial.maxMinDiffForLargeSteps(40, rules), "2188189693529".toBigInteger())
    }
}