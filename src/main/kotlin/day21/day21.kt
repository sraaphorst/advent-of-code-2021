package day21

// day21.kt
// By Sebastian Raaphorst, 2022.

import kotlin.math.min
import arrow.typeclasses.Monoid
import kotlinx.coroutines.runBlocking

enum class Player {
    ONE,
    TWO
}

// Integers mod 10.
fun zn(n: Int) = object : Monoid<Int> {
    override fun empty(): Int = 0
    override fun Int.combine(b: Int): Int = (this + b) % n
}

val z10 = zn(10)

data class PlayerState(val position: Int, val score: Int) {
    companion object {
        fun fromString(str: String): PlayerState =
                PlayerState(str.trim().last().digitToInt(), 0)
    }

    fun move(squares: Int): PlayerState {
        val newSquare = z10.combineAll(listOf(squares, position - 1)) + 1
        return PlayerState(newSquare, score + newSquare)
    }
}

data class BoardState(val player1: PlayerState, val player2: PlayerState, val turn: Player,
                      val winningCondition: (PlayerState, PlayerState) -> Player?) {
    fun winner(): Player? = winningCondition(player1, player2)

    fun move(squares: Int): BoardState = when (turn) {
        Player.ONE -> BoardState(player1.move(squares), player2, Player.TWO, winningCondition)
        Player.TWO -> BoardState(player1, player2.move(squares), Player.ONE, winningCondition)
    }
}

fun answer1(player1: PlayerState, player2: PlayerState): Int {
    val scoring = { b: BoardState, rolls: Int ->
        min(b.player1.score, b.player2.score) * rolls
    }

    val winner = { p1: PlayerState, p2: PlayerState ->
        when {
            p1.score >= 1000 -> Player.ONE
            p2.score >= 1000 -> Player.TWO
            else -> null
        }
    }

    fun aux(board: BoardState, rolls: Int, lastRoll: Int): Int = when (board.winner()) {
        null ->
            aux(
                    board.move((lastRoll % 100) + ((lastRoll + 1) % 100) + ((lastRoll + 2) % 100) + 3),
                    rolls + 3,
                    (lastRoll + 2) % 100 + 1
            )
        else -> scoring(board, rolls)
    }

    return aux(BoardState(player1, player2, Player.ONE, winner), 0, 0)
}

// To measure the wins between the two players.
typealias WinCount = Pair<ULong, ULong>

fun answer2(player1: PlayerState, player2: PlayerState): WinCount {
    // Convenience functions for manipulating WinCount.
    operator fun WinCount.plus(other: WinCount): WinCount =
            WinCount(first + other.first, second + other.second)

    operator fun WinCount.times(factor: ULong): WinCount =
            WinCount(first * factor, second * factor)

    val dieRange = (1..3)
    val diceRolls = dieRange.flatMap { r1 -> dieRange.flatMap { r2 -> dieRange.map { r3 -> r1 + r2 + r3 } } }
            .groupBy { it }
            .mapValues { it.value.size.toULong() }

    val winner = { p1: PlayerState, p2: PlayerState ->
        when {
            p1.score >= 21 -> Player.ONE
            p2.score >= 21 -> Player.TWO
            else -> null
        }
    }

    // We need a mutable map for caching. Not caching increases run time by a factor of 50 on my machine.
    val cache = mutableMapOf<BoardState, WinCount>()

    fun aux(board: BoardState): WinCount = when (board.winner()) {
        Player.ONE -> WinCount(1u, 0u)
        Player.TWO -> WinCount(0u, 1u)
        else -> cache.getOrPut(board) {
            diceRolls.map { (roll, frequency) -> aux(board.move(roll)) * frequency }
                    .reduce { acc, wins -> acc + wins }
        }
    }
    return aux(BoardState(player1, player2, Player.ONE, winner))
}

// No max function defined on ULong.
fun WinCount.max(): ULong = when {
    first > second -> first
    else -> second
}

fun main(): Unit = runBlocking {
    val input = object {}.javaClass.getResource("/day21.txt")!!.readText().trim().split("\n")
    val p1 = PlayerState.fromString(input.first())
    val p2 = PlayerState.fromString(input.last())

    println("--- Day 21: Dirac Dice ---\n")

    // Answer: 684495
    println("Part 1: Deterministic dice scoring: ${answer1(p1, p2)}")

    // Answer: 152587196649184
    println("Part 2: Dirac dice scoring: ${answer2(p1, p2).max()}")
}