package day09

// day09.kt
// By Sebastian Raaphorst, 2022.

import kotlinx.coroutines.runBlocking

typealias HeightMap = List<List<Int>>
typealias Point = Pair<Int, Int>
typealias Neighbours = Set<Pair<Int, Int>>

val deltas = listOf(
    Pair(-1, 0),
    Pair(1, 0),
    Pair(0, -1),
    Pair(0, 1)
)

fun findLowPoints(heightMap: HeightMap): Set<Point>? {
    val height = heightMap.size
    val width = heightMap.getOrNull(0)?.size ?: return null

    fun neighbours(point: Point): Set<Point> =
        deltas.map { Point(it.first + point.first, it.second + point.second) }
            .filter { it.first in 0 until height && it.second in 0 until width }.toSet()

    fun isLowPoint(point: Point): Boolean {
        val value = heightMap[point.first][point.second]
        return neighbours(point).all { heightMap[it.first][it.second] > value }
    }

    return heightMap.indices.flatMap { x -> heightMap[x].indices.map { y -> Point(x, y) } }
        .filter(::isLowPoint).toSet()
}

fun riskLevel(heightMap: HeightMap, point: Point): Int =
    heightMap[point.first][point.second] + 1

fun riskLevelSum(heightMap: HeightMap): Int? =
    findLowPoints(heightMap)?.sumOf { riskLevel(heightMap, it) }

fun parseHeightMap(input: String): HeightMap =
    input.split('\n').map { it.map(Char::digitToInt) }

fun main() = runBlocking {
    val heightMap = parseHeightMap(object {}.javaClass.getResource("/day09.txt")!!.readText().trim())

    println("--- Day 9: Smoke Basin ---\n")

    // Answer: 494
    println("Part 1: Sum of low point risk levels: ${riskLevelSum(heightMap)}")
}