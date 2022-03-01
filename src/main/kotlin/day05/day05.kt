package day05

// day05.kt
// By Sebastian Raaphorst, 2022.

import kotlinx.coroutines.runBlocking
import java.security.InvalidParameterException
import kotlin.math.abs

data class Coordinate(val x: Int, val y: Int) : Comparable<Coordinate> {
    override operator fun compareTo(other: Coordinate): Int =
        //if (x < other.x) x - other.x else y - other.y
        if (x < other.x) -1
        else if (other.x < x) 1
        else y - other.y
}

fun <T : Comparable<T>> minmax(t1: T, t2: T): Pair<T, T> =
    if (t1 < t2) (t1 to t2) else (t2 to t1)

enum class Orientation {
    HORIZONTAL,
    VERTICAL,
    DIAGONAL
}

class Line constructor(coord1: Coordinate, coord2: Coordinate) {
    private val c1: Coordinate
    private val c2: Coordinate
    val orientation: Orientation

    init {
        val (p1, p2) = minmax(coord1, coord2)
        this.c1 = p1
        this.c2 = p2
        if (this.c1.x == this.c2.x)
            this.orientation = Orientation.VERTICAL
        else if (this.c1.y == this.c2.y)
            this.orientation = Orientation.HORIZONTAL
        else if ((this.c2.x - this.c1.x) == abs(this.c2.y - this.c1.y))
            this.orientation = Orientation.DIAGONAL
        else
            throw InvalidParameterException("Line from $c1 to $c2 not vertical, horizontal, or diagonal.")
    }

    fun generatePoints(): List<Coordinate> = when (orientation) {
        Orientation.HORIZONTAL -> (c1.x..c2.x).map { Coordinate(it, c1.y) }
        Orientation.VERTICAL  -> (c1.y..c2.y).map { Coordinate(c1.x, it) }
        Orientation.DIAGONAL  -> (0..(c2.x - c1.x)).map {
            Coordinate(c1.x + it, c1.y + (if (c1.y <= c2.y) 1 else -1) * it)
        }
    }

    fun contains(c: Coordinate): Boolean = when (orientation) {
        // For diagonal, the last condition is necessary: otherwise anything in the square formed by the two diagonal
        // points will be considered to contain c.
        Orientation.HORIZONTAL -> c.y == c1.y && c1.x <= c.x && c.x <= c2.x
        Orientation.VERTICAL  -> c.x == c1.x && c1.y <= c.y && c.y <= c2.y
        Orientation.DIAGONAL  ->
            c.x >= c1.x && c.x <= c2.x &&
                    ((c1.y <= c2.y && c1.y <= c.y && c.y <= c2.y) ||  (c2.y < c1.y && c2.y <= c.y && c.y <= c1.y)) &&
                    (c.x - c1.x == abs(c.y - c1.y))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Line
        return c1.x == other.c1.x && c1.y == other.c1.y
    }

    override fun hashCode(): Int {
        return Pair(c1.hashCode(), c2.hashCode()).hashCode()
    }
}

fun findDangerZones(lines: List<Line>, filter: (Line) -> Boolean): Map<Coordinate, Int> {
    val filteredLines = lines.filter { filter(it) }
    return filteredLines.flatMap { it.generatePoints() }.toSet()
        .associateWith { c -> filteredLines.filter { it.contains(c) }.size }
        .filter { (_, i) -> i > 1 }
}

fun findHVDangerZoneCount(lines: List<Line>): Int =
    findDangerZones(lines) { it.orientation in setOf(Orientation.VERTICAL, Orientation.HORIZONTAL) }.size

fun findAllDangerZoneCount(lines: List<Line>): Int =
    findDangerZones(lines) { true }.size

fun parseLineData(rawInput: String): List<Line> =
    rawInput.trim()
        .split('\n')
        .map{
            val (p1, p2) = it.split(" -> ")
            val (x1, y1) = p1.split(',').map(String::toInt)
            val (x2, y2) = p2.split(',').map(String::toInt)
            Line(Coordinate(x1, y1), Coordinate(x2, y2))
        }

fun main() = runBlocking {
    val lineData = parseLineData(object {}.javaClass.getResource("/day05.txt")!!.readText())

    println("--- Day 5: Hydrothermal Venture ---\n")

    // Answer: 6564
    println("Part 1: Horizontal-vertical danger zones: ${findHVDangerZoneCount(lineData)}")

    // Answer: 19172
    println("Part 2: All danger zones: ${findAllDangerZoneCount(lineData)}")
}