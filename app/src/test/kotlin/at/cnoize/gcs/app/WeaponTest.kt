package at.cnoize.util.at.cnoize.gcs.app

import at.cnoize.gcs.app.weapons.Weapon
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class WeaponTest {
    @Test
    fun `weapon can't be created without modes`() {
        val exception = assertThrows<IllegalArgumentException> { Weapon("no weapon at all") }
        assertNotNull(exception.message) { "weapon without modes throws with message" }
        assertEquals("Weapon has to have at least one mode", exception.message)
    }
}
