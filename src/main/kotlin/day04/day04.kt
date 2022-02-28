package day04

// day03.kt
// By Sebastian Raaphorst, 2021.

import arrow.core.foldLeft
import kotlinx.coroutines.runBlocking

typealias Choices = List<Int>
typealias Position = Pair<Int, Int>
typealias InitialBoard = Map<Int, Position>

data class BingoBoard(val board: InitialBoard, val calledPositions: Set<Position>, val winningNumber: Int? = null) {
    fun callNumber(num: Int): BingoBoard = when(val newPos = board[num]) {
        is Position -> {
            // We have a new position, so add it and check for a winner.
            val (row, col) = newPos
            val newCalledPositions = calledPositions + newPos

            // Check for a winner.
            val rowWin = (0..4).fold(true) { win, newCol -> win && (row to newCol) in newCalledPositions }
            val colWin = (0..4).fold(true) { win, newRow -> win && (newRow to col) in newCalledPositions }

            // Return the new board.
            BingoBoard(board, newCalledPositions, if (rowWin || colWin) num else null)
        }
        else ->
            // Position not even in board, so we clearly do not have a winner and can reuse this board.
            this
    }

    fun winner(): Boolean =
        winningNumber !== null

    fun score(): Int? =
        winningNumber?.times(unmarkedNumberSum())

    private fun unmarkedNumberSum(): Int =
        board.foldLeft(0) { score, (num, pos) -> score + if (pos in calledPositions) 0 else num }

    companion object {
        // We must filter out empty strings since there are multiple spaces separating entries.
        fun parse(lines: List<String>): BingoBoard =
            BingoBoard(lines.map { it.trim() }.filter { it.isNotEmpty() }.zip(0..4).flatMap { (line, rowIdx) ->
                line.split(' ').filter { it.isNotEmpty() }.zip(0..4).map { (entry, colIdx) ->
                    (entry.toInt() to (rowIdx to colIdx))
                }
            }.toMap(),
                emptySet()
            )
    }
}

fun parseBingoData(rawInput: String): Pair<Choices, List<BingoBoard>> {
    val input = rawInput.trim().split('\n').filter(String::isNotBlank)
    val moves = input[0].split(',').map(String::toInt)
    val boards = (1 until input.size step 5).map { BingoBoard.parse(input.slice(IntRange(it, it+4))) }
    return moves to boards
}

tailrec fun findWinningScore(moves: Choices, boards: List<BingoBoard>): Int? {
    val move = moves.firstOrNull() ?: return null
    val (winners, losers) = boards.map { it.callNumber(move) }.partition(BingoBoard::winner)
    return winners.mapNotNull(BingoBoard::score).maxOrNull() ?: findWinningScore(moves.drop(1), losers)
}

// We could merge this with the above code easily, but then it would no longer be tail recursive, and given the
// stack size will be moves.size, this could overflow.
tailrec fun findLosingScore(moves: Choices, boards: List<BingoBoard>): Int? {
    val move = moves.firstOrNull() ?: return null
    val (winners, losers) = boards.map { it.callNumber(move) }.partition(BingoBoard::winner)
    return if (losers.isNotEmpty())
        findLosingScore(moves.drop(1), losers)
    else
        winners.mapNotNull(BingoBoard::score).minOrNull()
}

fun main(): Unit = runBlocking {
    // The data is a little more complicated here.
    // The first line is the row of moves, whereas then there is a blank line, followed by five lines denoting a
    // bingo board.
    val (moves, boards) = parseBingoData(object {}.javaClass.getResource("/day04.txt")!!.readText())

    println("--- Day 4: Giant Squid ---\n")

    // Answer: 63424
    println("Part 1: First highest scored bingo board score: ${findWinningScore(moves, boards)}")

    // Answer: 23541
    println("Part 2: Last lowest scored bingo board score: ${findLosingScore(moves, boards)}")
}