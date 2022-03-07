package day18

// day18.kt
// By Sebastian Raaphorst, 2022.

import arrow.core.Tuple4
import arrow.typeclasses.Semigroup
import kotlinx.coroutines.runBlocking
import java.security.InvalidParameterException


// A SnailfishNumber is a binary tree with numbers only on the leaves.
sealed interface SnailfishNumber
class SnailfishLeaf(val value: Int): SnailfishNumber
class SnailfishBranch(val left: SnailfishNumber, val right: SnailfishNumber): SnailfishNumber


class SnailfishNumberMonoid: Semigroup<SnailfishNumber> {
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
            Pair(SnailfishBranch(SnailfishLeaf(value / 9),
                    SnailfishLeaf(value / 9 + value % 2)),
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
                    if (left != null)
                        Tuple4(SnailfishBranch(SnailfishLeaf(0), ))
                }
        }
    }

    // We have to perform two reductions: splits and explosions.
    fun SnailfishNumber.reduce(): SnailfishNumber {


    }
//    fun SnailFishNumber.reduce(): SnailFishNumber {
//        // Check for and if necessary, perform a split operation.
//        // This entails finding the leftmost number > 9 and splitting it, so traverse left until
//        fun split(explore: SnailFishNumber, hasSplit: Boolean = false): Pair<SnailFishNumber, Boolean> = when {
//            hasSplit -> Pair(explore, true)
//            explore.first is Int && explore.first as Int > 9 -> {
//                val value = explore.first as Int
//                Pair(SnailFishNumber(SnailFishNumber(value / 2, value / 2 + value % 2), explore.second), true)
//            }
//            explore.first is Int && explore.second is Int && explore.second as Int > 9 -> {
//                val value = explore.second as Int
//                Pair(SnailFishNumber(explore.first))
//            }
//        }
//    }
}

// We have no idea what is going to be on the inside of the parentheses, so we have to have an unpleasant
// return type of Pair<Any, Any>.
fun String.readSnailFishNumber(): SnailFishNumber {
    // We need to drop an outer layer of parentheses.
    val shed = drop(1).dropLast(1)

    // Now we need to find the separating comma. Start at the left and continue to take until we get
    // balanced parentheses and hit a comma.
    fun stackTake(pars: Int = 0, position: Int = 0): Int = when {
        shed[position] == ',' && pars == 0 -> position
        shed[position] == ',' || shed[position].isDigit() -> stackTake(pars, position + 1)
        shed[position] == '[' -> stackTake(pars + 1,  position + 1)
        shed[position] == ']' -> stackTake(pars - 1, position + 1)
        else -> throw InvalidParameterException("Invalid parameter found: $shed")
    }

    val position = stackTake()
    val left = take(position-1)
    val right = drop(position)

    // Now if left is a number, or right is a number, then simply return it.
    return Pair(
            if (left.contains(',')) left.readSnailFish() else left.toInt(),
            if (right.contains(',')) right.readSnailFish() else right.toInt()
    )
}

fun main(): Unit = runBlocking {
    val snailfish = object {}.javaClass.getResource("/day14.txt")!!.readText()

    println("--- Day 18: Snailfish ---\n")

    // Answer: 4244
//    println("Part 1: Maximum element difference after 10 steps: ${polymer.steps(10, rules).maxMinDiff()}")

    // Answer: 4807056953866
//    println("Part 2: Maximum element difference after 40 steps: ${polymer.maxMinDiffForLargeSteps(40, rules)}")
}