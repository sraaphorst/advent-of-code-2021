package day14

// day14.kt
// By Sebastian Raaphorst, 2022.

import kotlinx.coroutines.runBlocking
import java.math.BigInteger
import java.util.Collections.max
import java.util.Collections.min

typealias Polymer = String
typealias Rules = Map<Pair<Char, Char>, Char>


fun Polymer.step(rules: Rules): Polymer =
        dropLast(1).zip(drop(1)).joinToString("") {
            if (rules.contains(it)) "${it.first}${rules[it]}"
            else "${it.first}"
        } + last()


tailrec fun Polymer.steps(steps: Int, rules: Rules): Polymer = when (steps) {
    0 -> this
    else -> step(rules).steps(steps - 1, rules)
}


fun Polymer.counts(): Map<Char, Int> =
        toSet().associateWith { c -> count { c == it } }


// Was going to try this and be fancy with list of functions, but it is getting unreadable.
fun Polymer.maxMinDiff(): Int {
    val cts = counts()
    return max(cts.values) - min(cts.values)
}


// Another good to have function to count the number of times an element appears in a uniform pair.
fun <T> Pair<T, T>.count(t: T) = when {
    first == t && second == t -> 2
    first == t || second == t -> 1
    else -> 0
}


// For a high level of steps, we have to keep track of the polymer pairs rather than actually generate the string.
// We will dive out of functional programming here since it is not the most efficient way to handle this.
fun Polymer.countsForLargeSteps(steps: Int, rules: Rules): Map<Char, BigInteger> {
    // We need a list of all the elements.
    val elements = (rules.keys.flatMap { it.toList() } + rules.values).toSet()

    // We want a list of all possible pairs.
    val pairs = elements.flatMap { e -> elements.map { e to it } }.toSet()

    tailrec fun aux(n: Int, pairsDict: Map<Pair<Char, Char>, BigInteger>): Map<Pair<Char, Char>, BigInteger> = when (n) {
        0 -> pairsDict
        else -> {
            // Create a new dictionary with the new pair counts.
            // 1. If we have m pairs AC, for every rule AC -> B, then every AC turns into an AB and a BC, so
            //    we don't keep any of the ACs, but instead add m ABs and m BCs.
            // 2. If we have m pairs AA and a rule AA -> A, then we get AAA, so we only gain m pairs AA and not 2m.
            // 3. If we have m pairs AC and no rule for AC, we keep the m ACs.
            // Thus, to find the number of pairs AB, we have to find the rules Ax -> B and xB -> A.
            // Filter out the zero entries to save time. We don't need to track pairs that don't appear.
            val newPairsDict = pairs.associateWith { p ->
                rules.filter { (pp, t) ->
                    (p.first == pp.first && p.second == t) || (p.first == t && p.second == pp.second)
                }.keys.map {
                    pairsDict.getOrDefault(it, BigInteger.ZERO)
                }.fold(BigInteger.ZERO) { acc, v -> acc.add(v) }
            }.filterValues { it !== BigInteger.ZERO }
            aux(n-1, newPairsDict)
        }
    }

    val initPairs = dropLast(1).zip(drop(1))
    val initDict: Map<Pair<Char, Char>, BigInteger> =
            initPairs.toSet().associateWith { p -> BigInteger.valueOf(initPairs.count { p == it }.toLong()) }
    val finalPairs = aux(steps, initDict)

    // Now we count the number of times each element appears from the pairs.
    return elements.associateWith { e -> ((finalPairs.map { (pp, factor) ->
        factor.times(pp.count(e).toBigInteger())
    }.fold(BigInteger.ZERO) { acc, v -> acc.add(v) })
                .add(if (first() == e) BigInteger.ONE else BigInteger.ZERO)
                .add(if (last() == e) BigInteger.ONE else BigInteger.ZERO)).divide(BigInteger.TWO)
    }.filterValues { it !== BigInteger.ZERO }
}


fun Polymer.maxMinDiffForLargeSteps(steps: Int, rules: Rules): BigInteger {
    val counts = countsForLargeSteps(steps, rules)
    val max = counts.values.fold(BigInteger.ZERO) { m, c -> m.max(c) }
    val min = counts.values.fold(max) { m, c -> m.min(c) }
    return max.subtract(min)
}


// Not my finest code.
fun String.parsePolymerizationInput(): Pair<Polymer, Rules> {
    val lines = trim().split('\n')
    val initial = lines.first().trim()
    val rules = lines.drop(2).associate {
        val (c, p) = it.split("->").map(String::trim)
        (c[0] to c[1]) to p.first()
    }
    return initial to rules
}


fun main(): Unit = runBlocking {
    val (polymer, rules) = object {}.javaClass.getResource("/day14.txt")!!.readText().parsePolymerizationInput()

    println("--- Day 14: Extended Polymerization ---\n")

    // Answer: 4244
    println("Part 1: Maximum element difference after 10 steps: ${polymer.steps(10, rules).maxMinDiff()}")

    // Answer: 4807056953866
    println("Part 2: Maximum element difference after 40 steps: ${polymer.maxMinDiffForLargeSteps(40, rules)}")
}