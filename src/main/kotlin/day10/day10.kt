package day10

// day10.kt
// By Sebastian Raaphorst, 2022.

import kotlinx.coroutines.runBlocking


enum class ChunkType(val open: Char, val close: Char, val invalidValue: Int, val completionValue: Long) {
    PARENTHESES('(', ')', 3, 1L),
    SQUARES('[', ']', 57, 2L),
    BRACES('{', '}', 1197, 3L),
    ANGULAR('<', '>', 25137, 4L)
}


enum class Alignment {
    OPEN,
    CLOSE
}


data class Chunk(val type: ChunkType, val align: Alignment)


sealed class ChunksStatus {
    object Complete: ChunksStatus()

    class Incomplete(private val stack: List<Chunk>): ChunksStatus() {
        fun completionScore(): Long =
            stack.reversed().map { chunk -> ChunkType.values().first { chunk.type.close == it.close }.completionValue }
                .fold(0L) { score, value -> score * 5L + value }
    }

    class InvalidClose(val invalidChunk: ChunkType): ChunksStatus()
}


fun List<Chunk>.checkChunks(): ChunksStatus {
    tailrec fun aux(remaining: List<Chunk>, stack: List<Chunk> = emptyList()): ChunksStatus = when {
        // Everything consumed.
        remaining.isEmpty() && stack.isEmpty() -> ChunksStatus.Complete

        // All characters consumed, but there is still some stack left.
        remaining.isEmpty() -> ChunksStatus.Incomplete(stack)

        // An opening character: extend the stack and continue.
        remaining.first().align == Alignment.OPEN -> aux(remaining.drop(1), stack + listOf(remaining.first()))

        // A closing character: if the stack is empty or the previous character is not closed by this one, illegal.
        stack.isEmpty() || stack.last().type !== remaining.first().type -> ChunksStatus.InvalidClose(remaining.first().type)

        // The last case: a closing character and the previous character is closed by this one.
        else -> aux(remaining.drop(1), stack.dropLast(1))
    }

    return aux(this)
}


fun List<Chunk>.score(): Int = when (val status = checkChunks()) {
    is ChunksStatus.InvalidClose -> status.invalidChunk.invalidValue
    else -> 0
}


fun Iterable<List<Chunk>>.score(): Int =
    sumOf { it.score() }


fun <T> List<T>.middle(): T = this[this.size / 2]


fun Iterable<List<Chunk>>.completionScore(): Long =
    mapNotNull { when(val check = it.checkChunks()) {
        is ChunksStatus.Incomplete -> check.completionScore()
        else -> null
    } }.sorted().middle()


fun Char.toChunk(): Chunk =
    ChunkType.values().filter { it.open == this || it.close == this }
        .map { type -> Chunk(type, if (type.open == this) Alignment.OPEN else Alignment.CLOSE)  }
        .first()


fun String.toChunks(): List<Chunk> =
    map(Char::toChunk)


fun List<String>.toChunkList(): List<List<Chunk>> =
    map(String::toChunks)


fun main() = runBlocking {
    val chunkList = object {}.javaClass.getResource("/day10.txt")!!
        .readText().trim().split('\n').toChunkList()

    println("--- Day 10: Syntax Scoring ---\n")

    // Answer: 387363
    println("Part 1: Sum of syntax scoring: ${chunkList.score()}")

    // Answer: 4330777059
    println("Part 2: Completion score: ${chunkList.completionScore()}")
}