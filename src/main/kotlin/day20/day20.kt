package day20

// day20.kt
// By Sebastian Raaphorst, 2022.

import java.util.Collections.max
import java.util.Collections.min

typealias Encoding = List<Int>
typealias Position = Pair<Int, Int>
typealias Image = Set<Position>

// Convenience function for Cartesian product.
operator fun IntRange.times(other: IntRange) = this.flatMap { i -> other.map { j -> i to j }}

// Determine the new bounds for the expanded image.
fun Set<Int>.minmaxgrow(): Pair<Int, Int> =
        (min(this) - 1) to (max(this) + 1)

fun enhanceN(image: Image, encoding: Encoding, n: Int): Image {
    fun enhance(prevImage: Image, step: Int): Image {
        // Get the new canvas size.
        // Since the encoding is sneaky and throws a 1 for input 0, we have to do this default symbol.
        val default = if (encoding[0] == 1 && step % 2 == 0) 1 else 0
        val (minX, maxX) = prevImage.map(Position::first).toSet().minmaxgrow()
        val (minY, maxY) = prevImage.map(Position::second).toSet().minmaxgrow()

        // Create the new canvas.
        val xbounds = minX + 1 until maxX
        val ybounds = minY + 1 until maxY
        return (minX .. maxX).flatMap { x ->
            (minY..maxY).map { y ->
                val encidx = ((-1 .. 1) * (-1 .. 1)).map { (dx, dy) ->
                    val nx = x + dx
                    val ny = y + dy
                    if (nx in xbounds && ny in ybounds)
                        (if (prevImage.contains(nx to ny)) 1 else 0)
                    else default
                }.fold(0){cur, bit -> (cur shl 1) or bit }
                (x to y) to encoding[encidx]
            }.filter { (_, s) -> s == 1 }.map { it.first }
        }.toSet()
    }

    return (1 .. n).fold(image) { prevImage, step -> enhance(prevImage, step)}
}

fun parseEncoding(str: String): Encoding =
        str.toCharArray().map { if (it == '.') 0 else 1 }

fun parseImage(lst: List<String>): Image =
        lst.withIndex().flatMap { (row, data) ->
            data.toCharArray().withIndex().filter { (_, char) -> char == '#' }.map { (col, _) ->
                row to col
            }
        }.toSet()

fun main() {
    val input = object {}.javaClass.getResource("/day20.txt")!!.readText().trim().split("\n")
    val encoding = parseEncoding(input[0])
    val image = parseImage(input.drop(2))

    println("--- Day 20: Trench Map ---\n")

    // Answer: 5622
    println("Part 1: Number of lights after two enhancements: ${enhanceN(image, encoding, 2).size}")

    // Answer: 20395
    println("Part 2: Number of lights after 50 enhancements: ${enhanceN(image, encoding, 50).size}")
}
