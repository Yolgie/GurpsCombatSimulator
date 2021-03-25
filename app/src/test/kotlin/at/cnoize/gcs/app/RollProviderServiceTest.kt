package at.cnoize.gcs.app

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RollProviderServiceTest {
    var rollProviderServices = listOf(
        RandomRollProviderService()
    ) + Dice.faces.map { FixedRollProviderService(it) }

    @TestFactory
    fun `test stays in dice range`() =
        rollProviderServices.map { rollProviderService ->
            dynamicTest("test ${rollProviderService.javaClass.simpleName} stays in dice range") {
                repeat(TestSettings.repeatRandomTests) {
                    assertTrue(rollProviderService.singleDieRoll() in Dice.faces)
                }
            }
        }

    @Test
    fun `test fixedRollProviderServices`() {
        Dice.faces.map { dice ->
            val fixedRollProvider = FixedRollProviderService(dice)
            repeat(10) {
                assertEquals(dice, fixedRollProvider.singleDieRoll())
            }
        }
    }

    @Test
    fun `test fixedRollProviderService throws on init with invalid result`() {
        assertThrows<IllegalArgumentException> { FixedRollProviderService(Dice.minValue - 1) }
        assertThrows<IllegalArgumentException> { FixedRollProviderService(Dice.maxValue + 1) }
    }
}
