//package day02
//// day02.kt
//// By Sebastian Raaphorst, 2019.
//
//import arrow.core.ListK
//import arrow.core.k
//import arrow.fx.IO
//import arrow.fx.extensions.fx
//
//typealias OpCode = Int
//typealias Intcode = ListK<OpCode>
//
//fun <E> ListK<E>.updated(index: Int, elem: E) = mapIndexed { i, existing ->  if (i == index) elem else existing }.k()
//
//fun runIntcode(opcodes: Intcode, position: Int = 0): Intcode {
//    val opcode = opcodes[position]
//    if (opcode == 99)
//        return opcodes
//
//    val (pos1, pos2, dest) = opcodes.drop(position+1).take(3)
//    require(opcode == 1 || opcode == 2)
//    val op = if (opcode == 1) {i: Int, j: Int -> opcodes[i] + opcodes[j]} else {i: Int, j: Int -> opcodes[i] * opcodes[j]}
//    return runIntcode(opcodes.updated(dest, op(pos1, pos2)), position + 4)
//}
//
//fun findIntCode(intcode: Intcode, target: Int = 19690720): Int? {
//    (0..99).forEach { noun ->
//        (0..99).forEach { verb ->
//            if (runIntcode(intcode.updated(1, noun).updated(2, verb)).first() == target)
//                return noun * 100 + verb
//        }
//    }
//    return null
//}
//
//fun main() {
//    val program = IO.fx() {
//        println("--- Day 2: 1202 Program Alarm ---")
//        val intcode = object{}.javaClass.getResource("/day02.txt").readText().split(",").map { it.toInt() }.k()
//
//        val intcodeFixed = intcode.updated(1, 12).updated(2, 2)
//        val runProgram = runIntcode(intcodeFixed)
//
//        // Part 1: 9706670
//        println("Part 1: ${runProgram.first()}")
//
//        // Part 2: 2552
//        val runProgram2 = findIntCode(intcode)
//        println("Part 2: $runProgram2")
//    }
//    program.unsafeRunSync()
//}
