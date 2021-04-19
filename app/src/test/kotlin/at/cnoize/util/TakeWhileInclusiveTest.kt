package at.cnoize.util

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TakeWhileInclusiveTest {

    private val data = listOf(0, 1, 2, 3, 4, 5)
    private val predicateElement = 3 // this will result in the list containing the first element where the condition fails -> 3
    private val expectedList = listOf(0, 1, 2, 3)

    @Test
    fun `test takeWhileInclusive TypedArray`() {
        val array: Array<Int> = data.toTypedArray()
        val predicateElement = this.predicateElement
        val predicate = { element: Int -> element < predicateElement }
        val expectedResult = this.expectedList

        val result: List<Int> = array.takeWhileInclusive(predicate)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `test takeWhileInclusive ByteArray`() {
        val input: ByteArray = data.map(Int::toByte).toByteArray()
        val predicateElement = this.predicateElement.toByte()
        val predicate = { element: Byte -> element < predicateElement }
        val expectedResult = this.expectedList.map(Int::toByte)

        val result: List<Byte> = input.takeWhileInclusive(predicate)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `test takeWhileInclusive ShortArray`() {
        val input: ShortArray = data.map(Int::toShort).toShortArray()
        val predicateElement = this.predicateElement.toShort()
        val predicate = { element: Short -> element < predicateElement }
        val expectedResult = this.expectedList.map(Int::toShort)

        val result: List<Short> = input.takeWhileInclusive(predicate)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `test takeWhileInclusive IntArray`() {
        val input: IntArray = data.toIntArray()
        val predicateElement = this.predicateElement
        val predicate = { element: Int -> element < predicateElement }
        val expectedResult = this.expectedList

        val result: List<Int> = input.takeWhileInclusive(predicate)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `test takeWhileInclusive LongArray`() {
        val input: LongArray = data.map(Int::toLong).toLongArray()
        val predicateElement = this.predicateElement.toLong()
        val predicate = { element: Long -> element < predicateElement }
        val expectedResult = this.expectedList.map(Int::toLong)

        val result: List<Long> = input.takeWhileInclusive(predicate)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `test takeWhileInclusive FloatArray`() {
        val input: FloatArray = data.map(Int::toFloat).toFloatArray()
        val predicateElement = this.predicateElement.toFloat()
        val predicate = { element: Float -> element < predicateElement }
        val expectedResult = this.expectedList.map(Int::toFloat)

        val result: List<Float> = input.takeWhileInclusive(predicate)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `test takeWhileInclusive DoubleArray`() {
        val input: DoubleArray = data.map(Int::toDouble).toDoubleArray()
        val predicateElement = this.predicateElement.toDouble()
        val predicate = { element: Double -> element < predicateElement }
        val expectedResult = this.expectedList.map(Int::toDouble)

        val result: List<Double> = input.takeWhileInclusive(predicate)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `test takeWhileInclusive BooleanArray`() {
        val input: BooleanArray = arrayOf(true, true, true, false, false, false).toBooleanArray()
        val predicateElement = false
        val predicate = { element: Boolean -> element != predicateElement }
        val expectedResult = listOf(true, true, true, false)

        val result: List<Boolean> = input.takeWhileInclusive(predicate)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `test takeWhileInclusive CharArray`() {
        val input: CharArray = data.map(Int::toChar).toCharArray()
        val predicateElement = this.predicateElement.toChar()
        val predicate = { element: Char -> element < predicateElement }
        val expectedResult = this.expectedList.map(Int::toChar)

        val result: List<Char> = input.takeWhileInclusive(predicate)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `test takeWhileInclusive Iterable`() {
        val input: Iterable<Int> = data.asIterable()
        val predicateElement = this.predicateElement
        val predicate = { element: Int -> element < predicateElement }
        val expectedResult = this.expectedList

        val result: List<Int> = input.takeWhileInclusive(predicate)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `test takeWhileInclusive Sequence`() {
        val input: Sequence<Int> = data.asSequence()
        val predicateElement = this.predicateElement
        val predicate = { element: Int -> element < predicateElement }
        val expectedResult = this.expectedList

        val result: List<Int> = input.takeWhileInclusive(predicate).toList()

        assertEquals(expectedResult, result)
    }

    @Test
    fun `test takeWhileInclusive CharSequence`() {
        val input: CharSequence = this.data.joinToString()
        val predicateElement = this.predicateElement.toString()[0]
        val predicate = { element: Char -> element < predicateElement }
        val expectedResult = this.expectedList.joinToString()

        val result: CharSequence = input.takeWhileInclusive(predicate)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `test takeWhileInclusive String`() {
        val input: String = this.data.joinToString()
        val predicateElement = this.predicateElement.toString()[0]
        val predicate = { element: Char -> element < predicateElement }
        val expectedResult = this.expectedList.joinToString()

        val result: String = input.takeWhileInclusive(predicate)

        assertEquals(expectedResult, result)
    }
}
