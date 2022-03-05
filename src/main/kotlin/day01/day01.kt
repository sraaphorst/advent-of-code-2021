package day01

// day01.kt
// By Sebastian Raaphorst, 2022.

import kotlinx.coroutines.runBlocking

fun calculateDepthIncreases(depths: List<Int>): Int =
    depths.dropLast(1).zip(depths.drop(1)).count { (d1, d2) -> d2 > d1 }

fun calculateWindows(depths: List<Int>): List<Int> {
    tailrec fun aux(cur_depths: List<Int>, windows: List<Int>): List<Int> = when(cur_depths.size) {
        0, 1, 2 -> windows
        else -> aux(cur_depths.drop(1), windows + cur_depths.take(3).sum())
    }
    return aux(depths, emptyList())
}

fun main(): Unit = runBlocking {
    val depths = object {}.javaClass.getResource("/day01.txt")!!
        .readText().trim().split("\n").map { it.toInt() }

    println("--- Day 1: Sonar Sweep ---\n")

    // Answer: 1195
    println("Part 1: Depth Increases: ${calculateDepthIncreases(depths)}")

    // Answer: 1235
    println("Part 2: Window Increases: ${calculateDepthIncreases(calculateWindows(depths))}")
}