package day12

// day12.kt
// By Sebastian Raaphorst, 2022.

import kotlinx.coroutines.runBlocking
import java.security.InvalidParameterException

// Method that should be in Kotlin.
// Generic enough to handle all necessary cases here.
inline fun <reified U, reified V> Pair<U, V>.contains(elem: Any?): Boolean =
        (elem is U || elem is V) && (elem === first || elem === second)

enum class NodeType {
    START,
    SMALL,
    BIG,
    END
}

typealias NodeName = String

data class Node(val name: NodeName, val type: NodeType,
                val terminal: Boolean = name == endLabel) {
    companion object {
        const val startLabel = "start"
        const val endLabel = "end"
    }
}


// Entire representation of the graph of the caves.
data class Graph(val startNode: Node, val endNode: Node, val nodes: Set<Node>, val edges: Map<Node, Set<Node>>)


fun Graph.findDistinctPaths(allowSmallCaveTwice: Boolean): Int {
    val smallCaves = nodes.filter { it.type == NodeType.SMALL }

    fun aux(currentPath: List<Node>, traversedPaths: Set<List<Node>> = emptySet(), numPaths: Int = 0): Int = when {
        // If we have already traversed this path, abort.
        traversedPaths.contains(currentPath) -> 0

        // If we are at the end, stop.
        currentPath.last().terminal -> numPaths + 1

        // Extend by all neighbours that are possible.
        else -> {
            // This is a brutal computation for the small caves.
            // Cases:
            // 1. We don't allow any small cave to be visited twice and this small cave has not been visited.
            // 2. We allow one small cave to be visited twice and none have been, or this small cave has not been visited.
            val smallCaveMaxVisits = smallCaves.maxOfOrNull { s -> currentPath.count { it === s } } ?: 0
            val extensionPaths = edges.getValue(currentPath.last())
                    .filter { c ->
                        c.type == NodeType.BIG || c.type == NodeType.END
                                || (c.type == NodeType.SMALL && (!allowSmallCaveTwice && !currentPath.contains(c)))
                                || (c.type == NodeType.SMALL && allowSmallCaveTwice && (smallCaveMaxVisits < 2 || !currentPath.contains(c))) }
                    .map { currentPath + it }

            // Now calculate the number of paths from the extension paths.
            // Set.plus is being finicky, to we convert currentPath to a singleton set to avoid this.
            extensionPaths.sumOf { aux(it, traversedPaths + setOf(currentPath), numPaths) }
        }
    }

    return aux(listOf(startNode))
}


fun String.createGraph(): Graph {
    val rawEdges = trim().split('\n').flatMap {
        val e = it.split('-')
        listOf(e[0] to e[1], e[1] to e[0])
    }.filter { it.first != Node.endLabel && it.second != Node.startLabel }

    fun nodeType(s: String): NodeType = when (s) {
        Node.startLabel -> NodeType.START
        Node.endLabel   -> NodeType.END
        s.uppercase()   -> NodeType.BIG
        s.lowercase()   -> NodeType.SMALL
        else            -> throw InvalidParameterException("Invalid node type: $s.")
    }

    val nameToNode = rawEdges.flatMap { setOf(it.first, it.second) }.associateWith {
        Node(it, nodeType(it))
    }.toMap()

    // We also need the neighbours of each node. This is an undirected graph.
    // We weed out edges with nonexistent nodes, which should never happen.
    val edges = nameToNode.map { (name, node) -> node to rawEdges.filter { it.first == name }.map { nameToNode.getValue(it.second) }.toSet() }.toMap()

    return Graph(nameToNode.getValue(Node.startLabel),
            nameToNode.getValue(Node.endLabel),
            nameToNode.values.toSet(),
            edges)
}


fun main() = runBlocking {
    val graph = object {}.javaClass.getResource("/day12.txt")!!
            .readText().trim().createGraph()

    println("--- Day 12: Passage Pathing ---\n")

    // Answer: 5457
    println("Part 1: Number of paths from start to end: ${graph.findDistinctPaths(false)}")

    // Answer: 128506
    println("Part 2: Number of path from start to end allowing a repeated small cave: ${graph.findDistinctPaths(true)}")
}