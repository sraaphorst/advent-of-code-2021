package day21

import org.junit.Test
import kotlin.test.assertEquals

// day21.kt
// By Sebastian Raaphorst, 2022.

class Day21Test {
    companion object {
        val p1 = PlayerState(4, 0)
        val p2 = PlayerState(8, 0)
    }

    @Test
    fun `deterministic winner`() {
        assertEquals(739785, answer1(p1, p2))
    }

    @Test
    fun `dirac dice winner`() {
        assertEquals(WinCount(444_356_092_776_315u, 341_960_390_180_808u), answer2(p1, p2))
    }
}

