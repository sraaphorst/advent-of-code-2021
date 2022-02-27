//package day05
//
//import java.util.*
//
//typealias AddressIndex = Int
//typealias ParameterCount = Int
//
//// An IntComp is a computer initialized to the data supplied at startup time.
//data class IntComp(val memory: MutableList<Int>) {
//
//    enum class ParameterMode(value: Int) {
//        Position(0),
//        Immediate(1)
//    }
//
//    /**
//     * A simple address decoupled from its ParameterType.
//     */
//    // A simple address.
//    abstract inner class Address(val address: AddressIndex) {
//        fun withParameterMode(mode: ParameterMode): ParameterizedAddress {
//            if (mode == ParameterMode.Position) return ReferenceAddress(address)
//            if (mode == ParameterMode.Immediate) return ConcreteAddress(address)
//            throw InvalidPropertiesFormatException("Illegal Parameter Mode")
//        }
//    }
//
//    abstract inner class ParameterizedAddress(address: AddressIndex, val mode: ParameterMode) : Address(address) {
//        fun memget(): Int = when (mode) {
//            ParameterMode.Position -> memory[address]
//            ParameterMode.Immediate -> address
//
//        }
//        fun memset(value: Int): Unit {
//            when (mode) {
//                ParameterMode.Position -> memory[address] = value
//                ParameterMode.Immediate -> address
//            }
//        }
//    }
//
//    /**
//     * A concrete address that is a reference to a node.
//     */
//    inner class ReferenceAddress(address: AddressIndex): ParameterizedAddress(address, ParameterMode.Position)
//    inner class ConcreteAddress(address: AddressIndex): ParameterizedAddress(address, ParameterMode.Immediate)
//
//    private var addressPointer = ConcreteAddress(0)
//
//    class Operation(opCode: Int, n: ParameterCount, parameterModes: List<ParameterMode>)
//}
//
//class MyClass(val list: MutableList<Int>) {
//    inner class X(val idx: Int) {
//        fun hi() = list[idx]
//    }
//}
//
//fun main() {
//    val mc = MyClass(mutableListOf(0, 1, 2))
//    val abc = mc.X(5)
//
//
//}