package at.cnoize.util.at.cnoize.gcs.app

import at.cnoize.util.toSingleOrNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class IterableUtilTest {

    @Nested
    inner class `test toSingleOrNull`() {
        @Test
        fun `empty returns null`() {
            assertEquals(null, emptyList<Any>().toSingleOrNull())
        }

        @Test
        fun `single element returns the element`() {
            val element = 3
            assertEquals(element, listOf(element).toSingleOrNull())
        }

        @Test
        fun `equal elements return the element`() {
            val element = 3
            assertEquals(element, List(12) { element }.toSingleOrNull())
        }

        @Test
        fun `different elements return null`() {
            assertEquals(null, listOf(3, 3, 3, 3, 3, 4).toSingleOrNull())
        }
    }
}
