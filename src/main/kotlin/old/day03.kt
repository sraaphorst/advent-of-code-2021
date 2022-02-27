//package day03
//
//import kotlin.math.abs
//
//import arrow.core.Tuple2
//import arrow.core.extensions.*
//import arrow.typeclasses.*
//import arrow.core.k
//import arrow.fx.IO
//import arrow.fx.extensions.fx
//
//fun <A, B> monoidTuple(MA: Monoid<A>, MB: Monoid<B>): Monoid<Tuple2<A, B>> =
//    object: Monoid<Tuple2<A, B>> {
//
//        override fun Tuple2<A, B>.combine(y: Tuple2<A, B>): Tuple2<A, B> {
//            val (xa, xb) = this
//            val (ya, yb) = y
//            return Tuple2(MA.run { xa.combine(ya) }, MB.run { xb.combine(yb) })
//        }
//
//        override fun empty(): Tuple2<A, B> = Tuple2(MA.empty(), MB.empty())
//    }
//
//val M = monoidTuple(Int.monoid(), Int.monoid())
//
//enum class Contents {
//    Wire1, Wire2, Both
//}
//
//typealias GridDistance = MutableMap<Tuple2<Int, Int>, Int>
//
//operator fun Int.times(t: Tuple2<Int, Int>): Tuple2<Int, Int> =
//    Tuple2(this * t.a, this * t.b)
//
//data class Grid(var wire1Contents: String, var wire2Contents: String) {
//    // Determine the width and height.
//    val grid = mutableMapOf<Tuple2<Int, Int>, Contents>()
//    val grid1Distance = mutableMapOf<Tuple2<Int, Int>, Int>()
//    val grid2Distance = mutableMapOf<Tuple2<Int, Int>, Int>()
//    init {
//        processWire(Contents.Wire1, wire1Contents, grid1Distance)
//        processWire(Contents.Wire2, wire2Contents, grid2Distance)
//    }
//
//    // Process a wire.
//    private fun processWire(wire: Contents, wireContents: String, gridDistance: GridDistance): Unit {
//        val contents = wireContents.split(",").k()
//        var position = Tuple2(0, 0)
//        var totalDistance = 0
//        contents.forEach {
//            val units = it.drop(1).toInt()
//            val dvec = dir(it.first())
//            for (i in 1..units) {
//                totalDistance += 1
//                position = M.combineAll(listOf(position, dvec))
//
//                if (position !in grid) {
//                    grid[position] = wire
//                    gridDistance[position] = totalDistance
//                }
//                else if (position in grid && grid[position] != wire) {
//                    grid[position] = Contents.Both
//                    gridDistance[position] = totalDistance
//                }
//            }
//        }
//    }
//
//    fun wireDistance(p: Tuple2<Int, Int>): Int =
//        grid1Distance[p]!! + grid2Distance[p]!!
//
//    // Find the closest point to (0, 0) that has Both.
//    fun findOverlappingPoint(): Tuple2<Int, Int> =
//        grid.filterValues { it == Contents.Both }.keys.filterNot { it == Tuple2(0, 0) }.minBy { manhattanDistance(it) }!!
//
//    // Find the point that is the closest in terms of if we stretched the wires out.
//    fun findShortestWireOverlappingPoint(): Tuple2<Int, Int> =
//        grid.filterValues { it == Contents.Both }.keys.filterNot { it == Tuple2(0, 0) }.minBy { wireDistance(it) }!!
//
//    companion object {
//        fun dir(c: Char): Tuple2<Int, Int> = when(c) {
//            'R' -> Tuple2(1, 0)
//            'L' -> Tuple2(-1, 0)
//            'U' -> Tuple2(0, 1)
//            'D' -> Tuple2(0, -1)
//            else -> Tuple2(0, 0)
//        }
//
//        fun manhattanDistance(p: Tuple2<Int, Int>): Int =
//            abs(p.a) + abs(p.b)
//    }
//}
//
//fun main() {
//    val program = IO.fx() {
//        println("--- Day 3: Crossed Wires ---")
//        val wireStrings = object{}.javaClass.getResource("/day03.txt").readText().split("\n")
//        val g: Grid = Grid(wireStrings[0], wireStrings[1])
//
//        // Part 1: 209
//        val overlappingPointDistance = Grid.manhattanDistance(g.findOverlappingPoint())
//        println("Part 1: minimum overlapping point: $overlappingPointDistance")
//
//        // Part 2: 43258
//        val wireDistance = g.wireDistance(g.findShortestWireOverlappingPoint())
//        println("Part 2: minimum wire length distance: $wireDistance")
//    }
//    program.unsafeRunSync()
//}