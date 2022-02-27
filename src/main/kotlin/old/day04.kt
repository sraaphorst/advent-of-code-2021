//package day04
//// day04.kt
//// By Sebastian Raaphorst, 2019.
//
//import arrow.fx.IO
//import arrow.fx.extensions.fx
//
//typealias RulePredicate = (num: Int) -> Boolean
//
//fun rulePredicate1(num: Int): Boolean {
//    val digits = num.toString().map { it.toInt() - '0'.toInt() }
//    val digitPairs = digits.zipWithNext()
//    return digitPairs.any { (f,s) -> f == s } && digitPairs.all { (f,s) -> f <= s}
//}
//
//fun rulePredicate2(num: Int): Boolean {
//    val digits = num.toString().map { it.toInt() - '0'.toInt()}
//    val digitPairs = digits.zipWithNext()
//    // The digits will be sorted so we can count and they will be consecutive.
//    val numDigits = (0..9).map{ digit -> digits.count { digit == it }}
//    // Make sure there is a digit that appears exactly twice (instead of perhaps more than twice.)
//    return numDigits.any { it == 2 } && digitPairs.all { (f,s) -> f <= s}
//}
//
//fun numPasswords(lower: Int, upper: Int, rulePredicate: RulePredicate) =
//    (lower..upper).count { rulePredicate(it) }
//
//fun main() {
//    val program = IO.fx {
//        val lower = 256310
//        val upper = 732736
//
//        println("--- Day 4: Secure Container ---")
//
//        // Answer: 979
//        val passwordCount1 = numPasswords(lower, upper, ::rulePredicate1)
//        println("Part 1: Password Candidates: $passwordCount1")
//
//        // Answer: 635
//        val passwordCount2 = numPasswords(lower, upper, ::rulePredicate2)
//        println("Part 2: Password Candidates: $passwordCount2")
//    }
//    program.unsafeRunSync()
//}