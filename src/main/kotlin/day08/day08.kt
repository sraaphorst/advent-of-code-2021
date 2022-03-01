package day08

// day08.kt
// By Sebastian Raaphorst, 2022.

enum class Wire {
    A, B, C, D, E, F, G
}

typealias Digit=Set<Wire>

data class DigitString(val inputs: List<Digit>, val outputs: List<Digit>) {
    companion object {
        private fun toDigit(str: String): Digit =
            str.trim().map { Wire.valueOf(it.uppercase()) }.toSet()

        private fun toDigits(str: String): List<Digit> =
            str.trim().split(' ').map(::toDigit)

        fun parse(input: String): List<DigitString> =
            input.replace("|\n", "| ").trim().split('\n').map {
                val result = it.split('|').map(::toDigits)
                DigitString(result[0], result[1])
            }

        fun count1478Outputs(digitStrings: List<DigitString>): Int =
            digitStrings.map(DigitString::count1478Outputs).sum()
    }

    fun count1478Outputs(): Int =
        outputs.count { it.size in setOf(2, 3, 4, 7)}

    override fun toString(): String =
        "$inputs | $outputs"
}

fun main() {
    val digitStrings = DigitString.parse(object {}.javaClass.getResource("/day08.txt")!!.readText())

    println("--- Day 8: Seven Segment Search ---\n")

    // Answer: 440
    println("Part 1: Unique outputs: ${DigitString.count1478Outputs(digitStrings)}")

    // Answer:
//    println("Part 2: Output sums: ${determineMinimumFuelCost(positions, fuel2)}")

}
