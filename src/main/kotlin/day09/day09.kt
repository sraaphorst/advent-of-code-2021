package day09

// day09.kt
// By Sebastian Raaphorst, 2022.

import kotlinx.coroutines.runBlocking

typealias HeightMap = List<List<Int>>
typealias Point = Pair<Int, Int>
typealias Neighbours = Set<Pair<Int, Int>>


// Deltas to add to a point to determine its candidate neighbours.
// We do not consider diagonal points to be neighbours.
val deltas = listOf(
    Pair(-1, 0),
    Pair(1, 0),
    Pair(0, -1),
    Pair(0, 1)
)


fun Point.neighbours(height: Int, width: Int): Set<Point> =
    deltas.map { Point(it.first + this.first, it.second + this.second) }
        .filter { it.first in 0 until height && it.second in 0 until width }.toSet()


fun HeightMap.points(): List<Point> =
    indices.flatMap { x -> this[x].indices.map { y -> Point(x, y) } }


fun HeightMap.height(): Int =
    size


fun HeightMap.width(): Int =
    first().size


fun HeightMap.value(point: Point): Int =
    this[point.first][point.second]


fun HeightMap.findLowPoints(): Set<Point> {
    val height = height()
    val width = width()

    fun isLowPoint(point: Point): Boolean {
        val value = value(point)
        return point.neighbours(height, width).all { value(it) > value }
    }

    return points().filter(::isLowPoint).toSet()
}


fun HeightMap.riskLevel(point: Point): Int =
    value(point) + 1


fun HeightMap.riskLevelSum(): Int =
    findLowPoints().sumOf(this::riskLevel)


// Find the product of the three largest basins in the HeightMap.
fun HeightMap.basinProduct(): Int {
    val height = height()
    val width = width()

    // To find a basin, pass in a low point as a seed for currSet. The default params will handle the rest.
    // This probably could be made to run much more efficiently, but this traversal works fine for the problem size.
    tailrec fun findBasin(currSet: Set<Point>, previousSet: Set<Point> = emptySet(), size: Int = 1): Int =
        if (currSet.isEmpty()) size
        else {
            // Find all the neighbours of the previous set that are not already in the basin and do not have value nine.
            val nextSet = currSet.flatMap { it.neighbours(height, width) }
                .filter { value(it) != 9 }
                .filter { !previousSet.contains(it) && !currSet.contains(it) }.toSet()
            findBasin(nextSet, currSet, size + nextSet.size)
        }

    // Start by finding the seeds of basins, which are the low points.
    // We will then perform something akin to a breadth-first search, spreading out from the low point to
    // encompass all points in the basin until there are no more candidates.
    return findLowPoints().map { findBasin(setOf(it)) }.sorted().takeLast(3).reduce(Int::times)
}


fun parseHeightMap(input: String): HeightMap =
    input.split('\n').map { it.map(Char::digitToInt) }


fun main() = runBlocking {
    val heightMap = parseHeightMap(object {}.javaClass.getResource("/day09.txt")!!.readText().trim())

    println("--- Day 9: Smoke Basin ---\n")

    // Answer: 494
    println("Part 1: Sum of low point risk levels: ${heightMap.riskLevelSum()}")

    // Answer: 1048128
    println("Part 2: Product of sizes of three largest basins: ${heightMap.basinProduct()}")
}