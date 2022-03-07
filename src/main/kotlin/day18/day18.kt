package day18

// day18.kt
// By Sebastian Raaphorst, 2022.

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

    // The data returned here is:
    // 1. The modified subtree to replace.
    // 2. The number to propagate left, if any.
    // 3. The number to propagate right, if any.
    // 4. The result, i.e. that we exploded.
//    fun explode(stack: List<SnailfishNumber>):
//            Tuple4<SnailfishNumber, Int?, Int?, Boolean> = when {
//                stack.first() is SnailfishLeaf -> Tuple4(stack.first(), null, null, false)
//                stack.size == 5 -> {
//                    // A branch at depth 5 so explode.
//                    this as SnailfishBranch
//                    left as SnailfishLeaf
//                    right as SnailfishLeaf
//                    Tuple4(SnailfishLeaf(0), left.value, right.value, true)
//                }
//                else -> {
//                    // Look to the left to see if we can find an exploding entry.
//                    this as SnailfishBranch
//                    val (node, left, right, result) = this.left.explode(depth + 1)
//
//                    // If we exploded on the left, propagate the value back up and propagate the value to the right.
////                    if (left != null)
////                        Tuple4(SnailfishBranch(SnailfishLeaf(0), ))
//                    TODO()
//                }
//            }

    private data class ExplodeResults(
            val leftValue: Int? = null,
            val movedLeft: Boolean = false,
            val rightValue: Int? = null,
            val movedRight: Boolean = false,
            val exploded: Boolean = false,
            val explodedNode: SnailfishNumber? = null
    )

    private val NoExplodeResults = ExplodeResults()

    // Exploding is much more difficult than splitting to the point where I almost reverted to string, modified
    // the string and reparsed.
    //
    // Instead, we use the following technique:
    //
    // Maintain a stack of the parent nodes.
    // If we reach an exploding node, replace it, and then set propLeft to the left value and propRight to the
    // right value.
    //
    // When we explode, we begin backtracking on the stack.
    //
    // Propagating left:
    // As soon as we can go left while backtracking on the stack, set goneLeft to true to indicate that we have
    // gone left. Then continue to go right (zero or more times) until we hit a leaf and add the value to the leaf.
    // If we can never go left, we drop the value.
    //
    // Propagating Right:
    // As soon as we can go right while backtracking on the stack, set goneRight to true to indicate that we have
    // gone right. Then continue to go left (zero or more times) until we hit a leaf and add the value to the leaf.
    // If we can never go right, drop the value.
//    fun explode(stack: List<SnailfishNumber>,
//                propLeft: Int? = null,
//                goneLeft: Boolean = false,
//                propRight: Int? = null,
//                goneRight: Boolean = false,
//                exploded: Boolean): Boolean {
    fun explode(stack: List<SnailfishNumber>): ExplodeResults = when {
        // If we are at a node to explode, then explode it and propagate the results back up.
        stack.size == 5 && stack.first() is SnailfishBranch -> {
            val node = stack.first() as SnailfishBranch
            val left = node.left as SnailfishLeaf
            val right = node.right as SnailfishLeaf
            ExplodeResults(
                    left.value, false,
                    right.value, false, true,
                    SnailfishLeaf(0)
            )
        }
    }

    private enum class Direction {
        NONE,
        LEFT,
        RIGHT
    }

    // We are propagating back up from the exploded node.
    // TODO: We have to make sure to never revisit the exploded node and accidentally increase it.
    private fun propagate(stack: List<Pair<SnailfishNumber, Direction>>, cameFrom: Direction, er: ExplodeResults):
            Pair<SnailfishNumber, ExplodeResults> {
        // Pop the top element of the stack.
        val (node, dir) = stack.first()
        val newStack = stack.drop(1)

        // Base cases: we are at a leaf.
        // TODO: Make sure this leaf is NOT the one created in the explosion.
        // TODO: I think this should be easy. We just have to make sure to never revisit this node.
        if (node is SnailfishLeaf) {
            // If we moved left to get here and had not yet gone left, then this is the node to increase with the
            // left value since there is no right to proceed to for us to get closer to the exploded node.
            if (dir == Direction.LEFT && !er.movedLeft && er.leftValue != null) {
                val newER = ExplodeResults(null, false, er.rightValue, er.movedRight, true, null)
                return SnailfishLeaf(node.value + er.leftValue) to newER
            }

            // If we moved right to get here, and had already gone left, then this is the node to increase with the
            // left value since this is the furthest right we can go and the closest we can get to the exploded node.
            if (dir == Direction.RIGHT && er.movedLeft && er.leftValue != null) {
                val newER = ExplodeResults(null, false, er.rightValue, er.movedRight, true, null)
                return SnailfishLeaf(node.value + er.leftValue) to newER
            }

            // If we moved right to get here and had not yet gone right, then this is the node to increase with the
            // right value since there is no left to proceed to for us to get closer to the exploded node.
            if (dir == Direction.RIGHT && !er.movedRight && er.rightValue != null) {
                val newER = ExplodeResults(er.leftValue, er.movedLeft, null, false, true, null)
                return SnailfishLeaf(node.value + er.rightValue) to newER
            }

            // If we moved left to get here and had already gone right, then this is the node to increase with the
            // right value since this is the furthest left we can go and the closest we can get to the exploded node.
            if (dir == Direction.LEFT && er.movedRight && er.rightValue != null) {
                val newER = ExplodeResults(er.leftValue, er.movedLeft, null, false, true, null)
                return SnailfishLeaf(node.value + er.rightValue) to newER
            }

            // Otherwise, this leaf is not special: just return it and the current explosion results.
            return node to er
        }

        // We are at a branch. If we came from the right,
        // If we are still propagating left and have not yet moved left, do this.
        if (wasRight && !explodeResults.movedLeft) {
            // We can move left, so do so.

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