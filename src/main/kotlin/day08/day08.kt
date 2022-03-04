package day08

// day08.kt
// By Sebastian Raaphorst, 2022.

import arrow.core.sequenceNullable

enum class Wire {
    A, B, C, D, E, F, G
}

// These are the digits as originally defined in the problem.
//
//  AAAA
// B    C
// B    C
//  DDDD
// E    F
// E    F
//  GGGG

enum class Digit(val value: Int, val wires: Set<Wire>) {
    ZERO(0, setOf(Wire.A, Wire.B, Wire.C, Wire.E, Wire.F, Wire.G)),
    ONE(1, setOf(Wire.C, Wire.F)),
    TWO(2, setOf(Wire.A, Wire.C, Wire.D, Wire.E, Wire.G)),
    THREE(3, setOf(Wire.A, Wire.C, Wire.D, Wire.F, Wire.G)),
    FOUR(4, setOf(Wire.B, Wire.C, Wire.D, Wire.F)),
    FIVE(5, setOf(Wire.A, Wire.B, Wire.D, Wire.F, Wire.G)),
    SIX(6, setOf(Wire.A, Wire.B, Wire.D, Wire.E, Wire.F, Wire.G)),
    SEVEN(7, setOf(Wire.A, Wire.C, Wire.F)),
    EIGHT(8, Wire.values().toSet()),
    NINE(9, setOf(Wire.A, Wire.B, Wire.C, Wire.D, Wire.F, Wire.G))
}


class DigitString constructor(val inputs: List<Set<Wire>>, val outputs: List<Set<Wire>>) {
    // val permutation: Map<Wire, Wire>? = findWirePermutation()
    val bijection: Map<Set<Wire>, Digit>? = findWireSetToDigitMap()
    val decodedInputs: List<Digit>? = inputs.map { bijection?.get(it) }.sequenceNullable()
    val decodedOutputs: List<Digit>? = outputs.map { bijection?.get(it) }.sequenceNullable()
    val decodedOutput: Int? = decodedOutputs?.fold(""){ str, digit -> str + digit.value.toString() }?.toInt()

    companion object {
        // Take a string like "CDGF" and convert to a set [C, D, G, F].
        private fun toWireSet(str: String): Set<Wire> =
            str.trim().map { Wire.valueOf(it.uppercase()) }.toSet()

        // Take a string like "CDGF ABEFG" and split into list of sets [C, D, G, F], [A, B, E, F, G]
        private fun toWireSets(str: String): List<Set<Wire>> =
            str.trim().split(' ').map(::toWireSet)

        fun parse(input: String): List<DigitString> =
            input.trim().split('\n').map {
                val result = it.split('|').map(::toWireSets)
                DigitString(result[0], result[1])
            }

        fun count1478Outputs(digitStrings: List<DigitString>): Int =
            digitStrings.map(DigitString::count1478Outputs).sum()

        fun sumDigitStringOutputs(digitStrings: List<DigitString>): Int? =
            digitStrings.map(DigitString::decodedOutput).sequenceNullable()?.sum()
    }

    // Technique 1: Find the bijection directly between sets of wires and the digits they represent.
    private fun findWireSetToDigitMap(): Map<Set<Wire>, Digit>? {
        // One is always going to be the only two digit permutation.
        val one = inputs.find { it.size == 2 } ?: return null

        // Seven is always going to be the only three digit permutation.
        val seven = inputs.find { it.size == 3 } ?: return null

        // Four is always going to be the only four digit permutation.
        val four = inputs.find { it.size == 4 } ?: return null

        // Eight is always going to be the only seven digit permutation.
        val eight = inputs.find { it.size == 7 } ?: return null

        // Now it remains to figure out the identity of the three groups of set 5 and of set 6.
        val sizeFive = inputs.filter { it.size == 5 }
        val sizeSix = inputs.filter { it.size == 6 }

        // Three is the only size five that contains one.
        val three = sizeFive.find { it.containsAll(one) } ?: return null

        // Nine is the only size six one that contains three
        val nine = sizeSix.find { it.containsAll(three) } ?: return null

        // Nine contains five, and is not three.
        val five = sizeFive.find { it !== three && nine.containsAll(it) } ?: return null

        // Only size five left is two.
        // Kotlin is wonky using subtraction here.
        val two = sizeFive.find { it !== three && it !== five } ?: return null

        // Zero is the size six tht contains one and is not nine.
        val zero = sizeSix.find { it !== nine && it.containsAll(one) } ?: return null

        // Last one is six.
        val six = sizeSix.find { it !== nine && it !== zero } ?: return null

        return listOf(
            zero to Digit.ZERO,
            one to Digit.ONE,
            two to Digit.TWO,
            three to Digit.THREE,
            four to Digit.FOUR,
            five to Digit.FIVE,
            six to Digit.SIX,
            seven to Digit.SEVEN,
            eight to Digit.EIGHT,
            nine to Digit.NINE
        ).toMap()
    }

    // Technique 2: find the actual wire permutation.
    // This is unnecessarily complicated and has been supplanted by technique one above.
    private fun findWirePermutation(): Map<Wire, Wire>? {
        // We can use the inputs with simple set logic to determine the permutation.
        // The element in the *** x *** is what maps to x in the permutation.

        // *** A ***
        // Take the difference of the set of size 3 (digit 7, ACF) from the set of size 2 (digit 1, CF).
        val set7 = inputs.find { it.size == 3 } ?: return null
        val set1 = inputs.find { it.size == 2 } ?: return null
        val a = (set7 - set1).firstOrNull() ?: return null

        // *** G ***
        // Find the set of size 4 (digit 4, BCDF).
        // Use it to find the set of size 6 (digit 9, aBCDFG)
        // Remove a and BCDF to get G.
        val set4 = inputs.find { it.size == 4 } ?: return null
        val set9 = inputs.find { it.size == 6 && it.containsAll(set4) && it.contains(a) } ?: return null
        val g = (set9 - set4 - a).firstOrNull() ?: return null

        // *** D ***
        // Now of the sets of size 5, we find set5 and set2, which we can identify because they
        // are the two that have three elements in common.
        // I suspect there is an easier way to extract these two sets from the trio but I am not sure how.
        val setsSize5 = inputs.filter { it.size == 5 }.toSet()
        val sets2and51 = setsSize5.find { s1 -> setsSize5.any { s2 -> s1 !== s2 && s2.intersect(s1).size == 3 } } ?: return null
        val sets2and52 = setsSize5.find { setsSize5.any { it !== sets2and51 && it.intersect(sets2and51).size == 3 } } ?: return null
        val d = (sets2and51.intersect(sets2and52) - a - g).firstOrNull() ?: return null

        // *** C ***
        // Now we can use set4, [D]BCF, and set5 (which we must find from the two candidates), [ADG]BF, to determine c.
        val set5 = setOf(sets2and51, sets2and52).find { set4.intersect(it).size == 3 } ?: return null
        val c = (set4 - set5 - d).firstOrNull() ?: return null

        // *** E ***
        // If we can get set0 [ACG]BEF and set9 [ACDG]BF, then we can get the value of E.
        // set0 is the only one amongst the size 6 sets that contains C and does not contain D.
        // We already have identified set9 above.
        val setsSize6 = inputs.filter { it.size == 6 }.toSet()
        val set0 = setsSize6.find { it.contains(c) && !it.contains(d) } ?: return null
        val e = (set0 - set9).firstOrNull() ?: return null

        // *** F ***
        // set3 should now be [ACDG]F, so we just need to locate it and pull out the only remaining value.
        // Since we already have sets2and5, it is the only other one. Kotlin type checking is having problems
        // subtracting Set<T> from Set<Set<T>>, though, and is converting the result to Set<Any> instead of leaving
        // it as Set<Set<T>> so we have to do this in this ridiculous way.
        val set3 = setsSize5.find { it !== sets2and51 && it !== sets2and52 } ?: return null
        val f = (set3 - a - c - d - g).firstOrNull() ?: return null

        // *** B ***
        // The only remaining element is B, which we can easily grab out of set4.
        val b = (set4 - c - d - f).firstOrNull() ?: return null

        return listOf(
            Wire.A to a,
            Wire.B to b,
            Wire.C to c,
            Wire.D to d,
            Wire.E to e,
            Wire.F to f,
            Wire.G to g
        ).toMap()
    }

    // The digits 1, 4, 7, and 8 have a unique number of wires in their wire sets, namely 2, 4, 3, and 7 respectively.
    fun count1478Outputs(): Int =
        outputs.count { it.size in setOf(2, 4, 3, 7)}

    override fun toString(): String =
        "$inputs | $outputs"
}


fun main() {
    val digitStrings = DigitString.parse(object {}.javaClass.getResource("/day08.txt")!!.readText())

    println("--- Day 8: Seven Segment Search ---\n")

    // Answer: 440
    println("Part 1: Unique outputs: ${DigitString.count1478Outputs(digitStrings)}")

    // Answer: 1046281
    println("Part 2: Output sums: ${DigitString.sumDigitStringOutputs(digitStrings)}")

}
