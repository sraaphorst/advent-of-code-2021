package day12

// day12test.kt
// By Sebastian Raaphorst, 2022.

import org.junit.Test
import kotlin.test.assertEquals

class Day12Test {
    companion object {
        private val graph1Input = """
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
        """.trimIndent()
        val graph1 = graph1Input.createGraph()

        private val graph2Input = """
            dc-end
            HN-start
            start-kj
            dc-start
            dc-HN
            LN-dc
            HN-end
            kj-sa
            kj-HN
            kj-dc
        """.trimIndent()
        val graph2 = graph2Input.createGraph()

        private val graph3Input = """
            fs-end
            he-DX
            fs-he
            start-DX
            pj-DX
            end-zg
            zg-sl
            zg-pj
            pj-he
            RW-he
            fs-DX
            pj-RW
            zg-RW
            start-pj
            he-WI
            zg-he
            pj-fs
            start-RW
        """.trimIndent()
        val graph3 = graph3Input.createGraph()
    }

    @Test
    fun `number of paths in Graph1, small caves can be once visited`() {
        assertEquals(graph1.findDistinctPaths(false), 10)
    }

    @Test
    fun `number of paths in Graph2, small caves can be once visited`() {
        assertEquals(graph2.findDistinctPaths(false), 19)
    }

    @Test
    fun `number of paths in Graph3, small caves can be once visited`() {
        assertEquals(graph3.findDistinctPaths(false), 226)
    }

    @Test
    fun `number of paths in Graph1, one small cave can be twice visited`() {
        assertEquals(graph1.findDistinctPaths(true), 36)
    }
}