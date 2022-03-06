package day13

// day13.kt
// By Sebastian Raaphorst, 2022.

import kotlinx.coroutines.runBlocking

enum class Axis {
    X,
    Y
}

data class Fold(val axis: Axis, val coord: Int)

data class Paper(val points: List<Pair<Int, Int>>,
                 val maxX: Int = points.maxOf { it.first },
                 val maxY: Int = points.maxOf { it.second }) {

    fun toDiagram(): String =
        (0 .. maxY).map { y ->
            (0..maxX).map { x ->
                if (points.contains(Pair(x, y))) '#' else '.'}
                    .joinToString("")}
                .joinToString("\n")

    fun executeFold(fold: Fold): Paper = Paper(when (fold.axis) {
        Axis.Y -> points.map { Pair(it.first, if (it.second > fold.coord) 2 * fold.coord - it.second else it.second) }.distinct()
        Axis.X -> points.map { Pair(if (it.first > fold.coord) 2 * fold.coord - it.first else it.first, it.second) }.distinct()
                                                                },
            if (fold.axis == Axis.X) fold.coord - 1 else maxX,
            if (fold.axis == Axis.Y) fold.coord - 1 else maxY)
}

fun String.parseOrigami(): Pair<Paper, List<Fold>> {
    val (paperInput, foldInput) = trim().split("\n\n")
    val points = paperInput.split('\n').map{
        val (x, y) = it.split(',').map(String::toInt)
        Pair(x, y)
    }

    val folds = foldInput.uppercase().split('\n').map {
        val (axis, coord) = it.drop(11).split('=')
        Fold(Axis.valueOf(axis), coord.toInt())
    }
    return Paper(points) to folds
}

fun main() = runBlocking {
    val (paper, folds) = object {}.javaClass.getResource("/day13.txt")!!
            .readText().parseOrigami()

    println("--- Day 13: Transparent Origami ---\n")

    // Answer: 708
    println("Part 1: Number of dots visible after first fold: ${paper.executeFold(folds.first()).points.size}")

    /**
     * #### ###  #    #  # ###  ###  #### #  #
     * #    #  # #    #  # #  # #  # #    #  #
     * ###  ###  #    #  # ###  #  # ###  ####
     * #    #  # #    #  # #  # ###  #    #  #
     * #    #  # #    #  # #  # # #  #    #  #
     * #### ###  ####  ##  ###  #  # #    #  #
     */
    // Answer: EBLUBRFH
    println("Part 2: Final origami folding:")
    println(folds.fold(paper) { p, f -> p.executeFold(f) }.toDiagram().replace('.', ' '))
}