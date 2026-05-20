package com.example.cmp_hello.sample
import androidx.compose.runtime.Composable
import kotlin.collections.asSequence
import kotlin.ranges.downTo
import kotlin.ranges.step
import kotlin.sequences.filter
import kotlin.sequences.take
import kotlin.text.trimIndent
import kotlin.to

// 全局枚举类
internal enum class Color(val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF)
}

internal enum class Direction {
    NORTH, SOUTH, EAST, WEST
}

// 全局密封类
internal sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

// 全局嵌套类
internal class Outer {
    val outerValue = "outer"

    class Nested {
        val nestedValue = "nested"
    }

    inner class Inner {
        val innerValue = "inner"
        fun getOuterValue() = outerValue
    }
}

// 全局数据类
internal data class Person(val name: String, val age: Int, val city: String)
internal data class Point(val x: Double, val y: Double)
internal data class Complex(val real: Double, val imaginary: Double)

// 复杂数据结构 - 用于LLDB调试测试
internal data class ContactInfo(
    val address: String,
    val phoneNumber: String
)

internal data class ComplexPerson(
    val name: String,
    val age: Int,
    val weight: Double,
    val contactInfo: ContactInfo,
    val contacts: List<ComplexPerson> = listOf()
)


internal class ComplexClass1 {
    val complexClass2: ComplexClass2=ComplexClass2()
    val name: String="第二层"
}

internal class ComplexClass2 {
    val name: String="第三层"
}

internal class ComplexClass {
    val complexClass1: ComplexClass1=ComplexClass1()
    val name: String="第一层"
}

internal class ComplexThreeClass {
    val name: String="3个普通3个复杂"
    val myNumber: Int=1
    val intArray: Array<Int> = arrayOf(1, 2, 3, 4, 5)
    val c1: ComplexClass2= ComplexClass2 ()
    val c2: ComplexClass2= ComplexClass2 ()
    val c3: ComplexClass= ComplexClass ()
}

internal fun start_debug() {
    // 1. 基本数据类型
    val byteValue: Byte = 127
    val shortValue: Short = 32767
    val intValue: Int = 2147483647
    val longValue: Long = 9223372036854775807L
    val floatValue: Float = 3.14159f
    val doubleValue: Double = 3.141592653589793
    val booleanValue: Boolean = true
    val charValue: Char = 'A'

    // 2. 字符串类型
    val stringValue: String = "Hello, Kotlin!"
    val stringWithUnicode: String = "Hello 世界! 🌍"
    val emptyString: String = ""
    val multilineString: String = """
        This is a
        multiline string
        with multiple lines
    """.trimIndent()

    // 3. 数组类型
    val intArray: IntArray = intArrayOf(1, 2, 3, 4, 5)
    val doubleArray: DoubleArray = doubleArrayOf(1.1, 2.2, 3.3)
    val booleanArray: BooleanArray = booleanArrayOf(true, false, true)
    val charArray: CharArray = charArrayOf('a', 'b', 'c')
    val stringArray: Array<String> = arrayOf("apple", "banana", "cherry")
    val objectArray: Array<Any> = arrayOf(1, "hello", 3.14, true)

    // 4. 集合类型 - List
    val intList: List<Int> = listOf(1, 2, 3, 4, 5)
    val stringList: List<String> = listOf("red", "green", "blue")
    val mixedList: List<Any> = listOf(1, "hello", 3.14, true)
    val emptyList: List<Int> = emptyList()
    val mutableIntList: MutableList<Int> = mutableListOf(1, 2, 3)
    val arrayList: ArrayList<String> = arrayListOf("one", "two", "three")

    // 5. 集合类型 - Set
    val intSet: Set<Int> = setOf(1, 2, 3, 4, 5)
    val stringSet: Set<String> = setOf("apple", "banana", "cherry")
    val emptySet: Set<Int> = emptySet()
    val mutableStringSet: MutableSet<String> = mutableSetOf("a", "b", "c")
    val hashSet: HashSet<Int> = hashSetOf(1, 2, 3, 4, 5)
    val linkedHashSet: LinkedHashSet<String> = linkedSetOf("first", "second", "third")

    // 6. 集合类型 - Map
    val stringToIntMap: Map<String, Int> = mapOf("one" to 1, "two" to 2, "three" to 3)
    val intToStringMap: Map<Int, String> = mapOf(1 to "first", 2 to "second", 3 to "third")
    val emptyMap: Map<String, Int> = emptyMap()
    val mutableStringMap: MutableMap<String, Int> = mutableMapOf("a" to 1, "b" to 2)
    val hashMap: HashMap<String, Int> = hashMapOf("x" to 10, "y" to 20, "z" to 30)
    val linkedHashMap: LinkedHashMap<Int, String> = linkedMapOf(1 to "first", 2 to "second")

    // 7. 序列类型
    val intSequence: Sequence<Int> = sequenceOf(1, 2, 3, 4, 5)
    val generatedSequence: Sequence<Int> = generateSequence(1) { it + 1 }.take(5)
    val filteredSequence: Sequence<Int> = (1..10).asSequence().filter { it % 2 == 0 }

    // 8. 范围类型
    val intRange: IntRange = 1..10
    val charRange: CharRange = 'a'..'z'
    val longRange: LongRange = 1L..100L
    val downToRange: IntProgression = 10 downTo 1
    val stepRange: IntProgression = 0..10 step 2

    // 9. 可空类型
    val nullableString: String? = "nullable string"
    val nullString: String? = null
    val nullableInt: Int? = 42
    val nullInt: Int? = null
    val nullableList: List<Int>? = listOf(1, 2, 3)
    val nullList: List<Int>? = null

    // 10. 自定义数据类实例
    val person1 = Person("Alice", 30, "New York")
    val person2 = Person("Bob", 25, "London")
    val point1 = Point(3.14, 2.71)
    val complex1 = Complex(1.0, 2.0)

    // 11. 枚举类型实例
    val color1 = Color.RED
    val color2 = Color.GREEN
    val direction1 = Direction.NORTH
    val direction2 = Direction.SOUTH

    // 12. 密封类实例
    val successResult = Result.Success("Operation completed")
    val errorResult = Result.Error("Something went wrong")
    val loadingResult = Result.Loading

    // 13. 嵌套类和内部类实例
    val outer = Outer()
    val nested = Outer.Nested()
    val inner = outer.Inner()

    // 14. 复杂数据结构
    val contact1 = ComplexPerson(
        name = "Alice",
        age = 25,
        weight = 55.0,
        contactInfo = ContactInfo("456 Elm St", "555-5678")
    )

    val contact2 = ComplexPerson(
        name = "Bob",
        age = 30,
        weight = 70.0,
        contactInfo = ContactInfo("789 Oak St", "555-9012")
    )

    val complexPerson = ComplexPerson(
        name = "Gai",
        age = 18,
        weight = 68.5,
        contactInfo = ContactInfo("123 Main Street", "555-1234"),
        contacts = listOf(contact1, contact2)
    )

    val complexThreeClass = ComplexThreeClass()

    val stringList30:List<String> = listOf(
        "s01", "s02s02", "s03s03s03", "s04s04s04s04", "s05s05s05s05s05",
        "s06s06s06s06s06s06", "s07", "s08", "s09", "s10",
        "s11", "s12", "s13", "s14", "s15",
        "s16", "s17", "s18", "s19", "s20",
        "s21", "s22", "s23", "s24", "s25",
        "s26", "s27s27", "s28", "s29s29", "s30"
    )

    val debugBreakpoint = "Set breakpoint here to inspect variables"
    println(debugBreakpoint)
}

@Composable
internal fun  runCases(id: String) {
    println("开始调试 $id")
    start_debug()
    println("完成调试 $id")
}
