package day18

// day18.kt
// By Sebastian Raaphorst, 2022.

import arrow.core.Tuple4
import arrow.typeclasses.Semigroup
import kotlinx.coroutines.runBlocking
import java.security.InvalidParameterException


// A SnailfishNumber is a binary tree with numbers only on the leaves.
// Note that toString just re-encodes the SnailfishNumber to its original input.
sealed interface SnailfishNumber
data class SnailfishLeaf(val value: Int): SnailfishNumber {
    override fun toString(): String = value.toString()
}
data class SnailfishBranch(val left: SnailfishNumber, val right: SnailfishNumber): SnailfishNumber {
    override fun toString(): String = "[$left,$right]"
}


// There is no monoid on SnailfishNumber because we have no identity, so the best we can do is a Semigroup.
object SnailfishNumberMonoid: Semigroup<SnailfishNumber> {
    override fun SnailfishNumber.combine(b: SnailfishNumber): SnailfishNumber =
            SnailfishBranch(this, b)

    // Split: preorder traversal of the tree, and split - if any - the first number > 9 that we find.
    // As soon as we split, we complete.
    // Otherwise, check the left to see if we split, and if not, check the right.
    fun SnailfishNumber.split(hasSplit: Boolean = false): Pair<SnailfishNumber, Boolean> = when {
        hasSplit -> Pair(this, true)
        this is SnailfishBranch -> {
            val (left, leftHasSplit) = this.left.split(false)
            val (right, rightHasSplit) = this.right.split(leftHasSplit)
            Pair(SnailfishBranch(left, right), leftHasSplit || rightHasSplit)
        }
        this is SnailfishLeaf && value > 9 ->
            Pair(SnailfishBranch(SnailfishLeaf(value / 2),
                    SnailfishLeaf(value / 2 + value % 2)),
                    true
            )
        else -> Pair(this, false)
    }

    // This is an irritating way to propagate a value to the left. We want to add it to the rightmost leaf.
    private fun SnailfishNumber.propagateLeft(propValue: Int): SnailfishNumber = when (this) {
        is SnailfishLeaf -> SnailfishLeaf(value + propValue)
        is SnailfishBranch -> SnailfishBranch(this.left, this.right.propagateLeft(propValue))
    }

    // The second operation is an explosion: this occurs when a pair is nested inside four pairs.
    // For this, we need to be able to find:
    // 1. The number closest to the left, if any.
    // 2. The number closest to the right, if any.
    // The exploding pair's left number is added to the number that is the closest on the left.
    // The exploding pair's right number is added to the number that is the closest on the right.
    // The exploding pair is replaced with 0.
    // To propagate left is tricky. For each branch, we check the left to see if it explodes.
    // If it does not, we check the right to see if it explodes. If the right explodes, we need to propagate left.
    // TODO: Consider using a node stack of parent to make the propagation of left and right values easier?
    // TODO: This is getting overly complicated.
    fun SnailfishNumber.explode(depth: Int = 1):
            Tuple4<SnailfishNumber, Int?, Int?, Boolean> = when {
                this is SnailfishLeaf -> Tuple4(this, null, null, false)
                depth == 5 -> {
                    // A branch at depth 5 so explode.
                    this as SnailfishBranch
                    left as SnailfishLeaf
                    right as SnailfishLeaf
                    Tuple4(SnailfishLeaf(0), left.value, right.value, true)
                }
                else -> {
                    // Look to the left to see if we can find an exploding entry.
                    this as SnailfishBranch
                    val (node, left, right, result) = this.left.explode(depth + 1)

                    // If we exploded on the left, propagate the value back up and propagate the value to the right.
//                    if (left != null)
//                        Tuple4(SnailfishBranch(SnailfishLeaf(0), ))
                    TODO()
                }
            }

    // We have to perform two reductions: splits and explosions.
    fun SnailfishNumber.reduce(): SnailfishNumber {
        TODO()
    }
}

fun String.parseSnailFishNumber(): SnailfishNumber {
    // There should always be starting and ending parens: get rid of them.
    val shed = drop(1).dropLast(1)

    // We need to find the separating comma. Start at the left and continue to take until we get balanced
    // parentheses and hit a comma.
    fun stackTake(pars: Int = 0, position: Int = 0): Int = when {
        shed[position] == ',' && pars == 0 -> position
        shed[position] == ',' || shed[position].isDigit() -> stackTake(pars, position + 1)
        shed[position] == '[' -> stackTake(pars + 1,  position + 1)
        shed[position] == ']' -> stackTake(pars - 1, position + 1)
        else -> throw InvalidParameterException("Invalid parameter found: $shed")
    }

    val position = stackTake()
    val left = shed.take(position)
    val right = shed.drop(position+1)

    val leftNode = if (left.contains(',')) left.parseSnailFishNumber() else SnailfishLeaf(left.toInt())
    val rightNode = if (right.contains(',')) right.parseSnailFishNumber() else SnailfishLeaf(right.toInt())
    return SnailfishBranch(leftNode, rightNode)
}

fun main(): Unit = runBlocking {
    val snailfishNumber = object {}.javaClass.getResource("/day18.txt")!!.readText().trim().parseSnailFishNumber()

    println("--- Day 18: Snailfish ---\n")

    // Answer: 4244
//    println("Part 1: Maximum element difference after 10 steps: ${polymer.steps(10, rules).maxMinDiff()}")

    // Answer: 4807056953866
//    println("Part 2: Maximum element difference after 40 steps: ${polymer.maxMinDiffForLargeSteps(40, rules)}")
}