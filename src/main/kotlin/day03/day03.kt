package day03

// day03.kt
// By Sebastian Raaphorst, 2022.

import kotlinx.coroutines.runBlocking

fun Pair<Int, Int>.sum(): Int =
    this.first + this.second

fun Pair<Int, Int>.multiply(): Int =
    this.first * this.second

// Counts the number of 1s per position in the lst.
// This allows us to calculate the Gamma and Epsilon rate.
// The Gamma rate is the most common value of each position, and the Epsilon rate is the least common.
// I am thinking there is probably a better way to do this than using strings.
// Furthermore, no error handling in here right now in case a string cannot be parsed.
// Can likely add this to Char::digitToInt and ensure binary.
fun countPositions(lst: Iterable<String>): List<Int> =
    lst.fold(List(lst.first().length){0}){count, element -> element.map(Char::digitToInt).zip(count).map { it.sum() }}

fun String.toBinary(): Int =
    Integer.parseInt(this, 2)

fun calculateGammaEpsilon(lst: List<String>): Pair<Int, Int> {
    val half = lst.size / 2
    val diagnostics = countPositions(lst)
    val gamma = diagnostics.fold(0){expansion, curr -> (expansion shl 1) or (if (curr >= half) 1 else 0)}
    val epsilon = (1 shl diagnostics.size) - gamma - 1
    return Pair(gamma, epsilon)
}

// Given a list of boolean strings:
// 1. Iterate over the subset remaining, position by position, and determine the symbol that appears the maximum
//    number of times (1 or 0).
// 2. If we are keeping the one that appears the maximum times, keep the entries that have the symbol appearing the
//    maximum number of times (resp. minimum).
// 3. Continue until we only have a list of one element left, convert it from binary to int, and return it.
// Using a value of keep_maximum true will provide the oxygen generator rating.
// Using a value of keep_maximum false will provide the CO2 scrubber rating.
fun findLifeSupportElements(lst: List<String>): Pair<Int, Int> {
    tailrec fun partition(curr_lst: List<String>, position: Int, keep_maximum: Boolean): Int {
        // Partition the curr_lst into elements with 1 and 0 in the position indicated.
        if (curr_lst.size == 1)
            return curr_lst.first().toBinary()
        val (new_lst_1, new_lst_0) = curr_lst.partition { it[position] == '1' }
        val (max_list, min_list) = if (new_lst_1.size >= new_lst_0.size) Pair(new_lst_1, new_lst_0)
                                   else Pair(new_lst_0, new_lst_1)
        return partition(if (keep_maximum) max_list else min_list, position + 1, keep_maximum)
    }

    return Pair(partition(lst, 0, true), partition(lst, 0, false))
}


fun main(): Unit = runBlocking {
    val diagnostics = object {}.javaClass.getResource("/day03.txt")!!
        .readText().trim().split("\n")

    println("--- Day 3: Binary Diagnostic ---")

    // Answer: 4147524
    println("Part 1: Power Consumption: ${calculateGammaEpsilon(diagnostics).multiply()}")

    // Answer: 3570354
    println("Part 2: Life Support Rating: ${findLifeSupportElements(diagnostics).multiply()}")
}