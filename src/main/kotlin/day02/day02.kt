package day02

// day02.kt
// By Sebastian Raaphorst, 2022.

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.computations.either
import arrow.core.orNull
import kotlinx.coroutines.runBlocking

enum class Direction(val delta: Pair<Int, Int>) {
    FORWARD(Pair(1, 0)),
    DOWN(Pair(0,1)),
    UP(Pair(0,-1))
}

sealed class ValidationError(val msg: String) {
    data class IllegalFormat(val string: String): ValidationError("Illegal format: $string")
    data class IllegalDirection(val string: String): ValidationError("Illegal direction specified: $string")
    data class NotAnInt(val string: String): ValidationError("Movement units not integral: $string")
    data class NonpositiveInt(val int: Int): ValidationError("Nonpositive movement unit: $int")
}

data class Movement(val direction: Direction, val units: Int) {
    companion object {
        private fun parseFormat(string: String): Either<ValidationError.IllegalFormat, Pair<String, String>> {
            val substrings = string.split(' ')
            return if (substrings.size == 2) Right(Pair(substrings[0].uppercase(), substrings[1]))
            else Left(ValidationError.IllegalFormat(string))
        }

        private fun parseDirection(string: String): Either<ValidationError.IllegalDirection, Direction> =
            if (string in Direction.values().map(Direction::name)) Right(Direction.valueOf(string))
            else Left(ValidationError.IllegalDirection(string))

        private fun parseInt(string: String): Either<ValidationError.NotAnInt, Int> =
            if (string.isNotEmpty() && string.all(Character::isDigit)) Right(string.toInt())
            else Left(ValidationError.NotAnInt(string))

        private fun checkInt(int: Int): Either<ValidationError.NonpositiveInt, Int> =
            if (int >= 1) Right(int)
            else Left(ValidationError.NonpositiveInt(int))

        suspend fun parse(string: String): Either<ValidationError, Movement> =
            either {
                val (dir_string, unit_string) = parseFormat(string).bind()
                val dir = parseDirection(dir_string).bind()
                val unitInt = parseInt(unit_string).bind()
                val unitPos = checkInt(unitInt).bind()
                Movement(dir, unitPos)
            }
    }
}

typealias Coordinates = Pair<Int, Int>


fun calculateCoordinates(lst: List<String>, useAim: Boolean): Either<ValidationError, Coordinates> = runBlocking {
    // If useAim is false, we proceed with the original coordinate calculations and ignore aim.
    // If useAim is true, we take the aim of the sub into account, which only moves when going forward.
    tailrec suspend fun aux(lst: List<String>, aim: Int, c: Coordinates): Either<ValidationError, Coordinates> {
        fun coords1(m: Movement): Coordinates =
            Coordinates(c.first + m.direction.delta.first * m.units, c.second + m.direction.delta.second * m.units)
        fun coords2(m: Movement): Coordinates =
            Coordinates(c.first + m.direction.delta.first * m.units, c.second + m.direction.delta.first * aim * m.units)

        if (lst.isEmpty())
            return Right(c)

        return when (val v = Movement.parse(lst.first())) {
            is Left -> Left(v.value)
            is Right -> {
                val m = v.value
                aux(lst.drop(1),
                    aim + m.direction.delta.second * m.units,
                    if (useAim) coords2(m) else coords1(m))
            }
        }
    }

    return@runBlocking aux(lst, 0, Pair(0, 0))
}


fun calculateCoordinateProduct(lst: List<String>, useAim: Boolean): Either<ValidationError, Int> =
    calculateCoordinates(lst, useAim).bimap({ it }, { it.first * it.second })


fun main(): Unit = runBlocking {
    val moves = object {}.javaClass.getResource("/day02.txt")!!.readText().trim().split("\n")

    println("--- Day 2: Dive! ---\n")

    // Answer: 2147104
    println("Part 1: Coordinates: ${calculateCoordinateProduct(moves, false).orNull()}")

    // Answer: 2044620088
    println("Part 2: Coordinates: ${calculateCoordinateProduct(moves, true).orNull()}")
}