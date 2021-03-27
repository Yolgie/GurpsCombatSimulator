package at.cnoize.util.at.cnoize.gcs.app

import at.cnoize.gcs.app.Dice
import at.cnoize.gcs.app.FixedRollProviderService
import at.cnoize.gcs.app.ProjectSettings
import at.cnoize.gcs.app.roll
import io.mockk.every
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class DiceWithRollProviderTest {
    @SpyK
    var rollProviderService = FixedRollProviderService(1)

    @BeforeTest
    fun setup() {
        mockkObject(ProjectSettings)
        every { ProjectSettings.getRollProviderService() } returns rollProviderService
    }

    @Test
    fun `test Dice roll`() {
        val dice = Dice(10)

        val result = dice.roll()

        assertEquals(10, result)
        verify(exactly = 10) { rollProviderService.singleDieRoll() }
    }
}
