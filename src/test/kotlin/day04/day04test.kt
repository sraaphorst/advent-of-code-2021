package day04

import org.junit.Test
import kotlin.test.assertEquals

class Day04Test {
    companion object {
        private fun createBoard(entries: List<List<Int>>): BingoBoard =
            BingoBoard(entries.zip(0..4).flatMap { (row, rowIdx) ->
                row.zip(0..4).map { (entry, colIdx) -> (entry to (rowIdx to colIdx)) }
            }.toMap(),
                emptySet()
            )

        val testMoves = listOf(
            7, 4, 9, 5, 11, 17, 23, 2, 0, 14, 21, 24, 10, 16, 13, 6, 15, 25, 12, 22, 18, 20, 8, 19, 3, 26, 1
        )
        val testBoard1 = createBoard(
            listOf(
                listOf(22, 13, 17, 11, 0),
                listOf(8, 2, 23, 4, 24),
                listOf(21, 9, 14, 16, 7),
                listOf(6, 10, 3, 18, 5),
                listOf(1, 12, 20, 15, 19)
            )
        )

        val testBoard2 = createBoard(
            listOf(
                listOf(3, 15, 0, 2, 22),
                listOf(9, 18, 13, 17, 5),
                listOf(19, 8, 7, 25, 23),
                listOf(20, 11, 10, 24, 4),
                listOf(14, 21, 16, 12, 6)
            )
        )

        val testBoard3 = createBoard(
            listOf(
                listOf(14, 21, 17, 24, 4),
                listOf(10, 16, 15, 9, 19),
                listOf(18, 8, 23, 26, 20),
                listOf(22, 11, 13, 6, 5),
                listOf(2, 0, 12, 3, 7)
            )
        )

        val testData = """
            7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

            22 13 17 11  0
            8  2 23  4 24
            21  9 14 16  7
            6 10  3 18  5
            1 12 20 15 19

            3 15  0  2 22
            9 18 13 17  5
            19  8  7 25 23
            20 11 10 24  4
            14 21 16 12  6

            14 21 17 24  4
            10 16 15  9 19
            18  8 23 26 20
            22 11 13  6  5
            2  0 12  3  7
        """.trimIndent()

        val bingoData = parseBingoData(testData)
    }

    @Test
    fun `parse test data`() {
        assertEquals(bingoData, testMoves to listOf(testBoard1, testBoard2, testBoard3))
    }

    @Test
    fun `find winning score`() {
        val (moves, boards) = parseBingoData(testData)
        assertEquals(findWinningScore(moves, boards), 4512)
    }

    @Test
    fun `find last worst winner`() {
        val (moves, boards) = parseBingoData(testData)
        assertEquals(findLosingScore(moves, boards), 1924)
    }
}