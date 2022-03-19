package day20

// day20.kt
// By Sebastian Raaphorst, 2022.

import java.util.Collections.max
import java.util.Collections.min

typealias Encoding = List<Int>
typealias Position = Pair<Int, Int>
typealias Image = Set<Position>

val deltas = (1 downTo -1).flatMap { r -> (1 downTo -1).map { c -> r to c } }

// Convenience method for adding two positions together.
fun Position.shift(other: Position): Position =
        (this.first + other.first) to (this.second + other.second)

// Given a position, examine the surrounding positions and determine the binary encoding from 0 to 511.
// (-1,-1) (-1, 0) (-1, 1)
// ( 0,-1) ( 0, 0) ( 0, 1)
// ( 1,-1) ( 1, 0) ( 1, 1)
// where the values are the deltas from the pixel at position offset (0,0) and we take the binary number
// reading left to right, top to bottom.
fun Image.positionToBinary(position: Position): Int =
        deltas.withIndex().filter { (_, delta) -> contains(position.shift(delta)) }.map { it.index }
                .fold(0) { v, idx -> v or (1 shl idx) }

fun Set<Int>.minmaxgrow(): Pair<Int, Int> =
        (min(this) - 1) to (max(this) + 1)

fun printImage(image: Image) {
    val (m1, M1) = image.map { it.first }.toSet().minmaxgrow()
    (m1..M1).map { row ->
        (m1..M1).map { col -> if (image.contains(row to col)) 1 else 0 }
    }
}

tailrec
fun evolveN(image: Image, encoding: Encoding, n: Int): Image {
    fun evolve(): Image {
        // Get the new canvas size.
        val (minX, maxX) = image.map(Position::first).toSet().minmaxgrow()
        val (minY, maxY) = image.map(Position::second).toSet().minmaxgrow()
        val paddedImage = if (n % 2 == 1)
            (image +
                    listOf(minX, maxX).flatMap { x -> (minY .. maxY).map { y -> x to y }} +
                    listOf(minY, maxY).flatMap { y -> (minX .. maxX).map { x -> x to y }}).toSet()
        else
            image
//        val paddedImage = image
        printImage(paddedImage)
        return (minX..maxX).flatMap { x ->
            (minY..maxY)
                    .map { y -> (x to y) to encoding[paddedImage.positionToBinary(x to y)] }
                    .filter { (_, value) -> value == 1 }
                    .map { it.first }
        }.toSet()
    }

    return when (n) {
        0 -> image
        else -> evolveN(evolve(), encoding, n - 1)
    }
}

fun String.parseEncoding(): Encoding =
        this.toCharArray().map { if (it == '.') 0 else 1 }

fun List<String>.parseImage(): Image =
        this.withIndex().flatMap { (row, data) ->
            data.toCharArray().withIndex().filter { (_, char) -> char == '#' }.map { (col, _) ->
                row to col
            }
        }.toSet()

fun main() {
    val input = object {}.javaClass.getResource("/day20.txt")!!.readText().trim().split("\n")
    val encoding = input[0].parseEncoding()
    val image = input.drop(2).parseImage()
    val (m1, M1) = image.map { it.first }.toSet().minmaxgrow()
    val (m2, M2) = evolveN(image, encoding, 1).map { it.first }.toSet().minmaxgrow()

    val enc = evolveN(image, encoding, 1)
    val xyz = (m1..M1).map { row ->
        (m1..M1).map { col -> if (enc.contains(row to col)) 1 else 0 }
    }
    println(enc.size)
    println(xyz)

    val enc2 = evolveN(image, encoding, 2)
    val xyz2 = (m2..M2).map { row ->
        (m2..M2).map { col -> if (enc.contains(row to col)) 1 else 0 }
    }
    println(enc2.size)
    println(xyz2)

    println("--- Day 20: Trench Map ---\n")

    // Answer: 5551
    // Him: 5410/ 5622
    // Me: 5436 / 5543
    println("Part 1: Number of lights after no evolutions: ${image.size}")
    println("Part 1: Number of lights after one evolutions: ${evolveN(image, encoding, 1).size}")
    println("Part 1: Number of lights after two evolutions: ${evolveN(image, encoding, 2).size}")

    // Answer: 4807056953866
    //println("Part 2: Maximum element difference after 40 steps: ${polymer.maxMinDiffForLargeSteps(40, rules)}")
}
