package at.cnoize.gcs.app

import at.cnoize.gcs.app.character.ActiveDefenseType
import at.cnoize.gcs.app.character.Character
import at.cnoize.gcs.app.character.CharacterState
import at.cnoize.gcs.app.character.SecondaryCharacteristic
import at.cnoize.gcs.app.weapons.ActiveWeapon
import at.cnoize.gcs.app.weapons.mediumShield
import at.cnoize.gcs.app.weapons.smallShield
import at.cnoize.util.toSingleOrNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertNotNull

class CharacterStateTest {

    @Test
    fun `test minimal initial character state`() {
        val character = Character("John")
        val initialState = character.getInitialPlayerState()

        assertEquals(character, initialState.character)
        assertEquals(character.get(SecondaryCharacteristic.HP), initialState.hp)
        assertEquals(character.get(SecondaryCharacteristic.FP), initialState.fp)
        assertEquals(0, initialState.encumbrance, "minimal character has no encumbrance")
        assertTrue(initialState.weapons.isEmpty(), "minimal character has no weapons or shields")
        assertTrue(initialState.activeWeapons.isEmpty(), "minimal character has no active weapons")
    }

    @Test
    fun `test active weapon not in weapons`() {
        val character = Character("John")
        val exception = assertThrows<IllegalArgumentException> {
            CharacterState(character, weapons = emptyList(), activeWeapons = listOf(ActiveWeapon(smallShield)))
        }

        assertNotNull(exception.message, "should throw with message")
        assert(exception.message!!.contains("active weapons must be")) { "validate error message" }
    }

    @Test
    fun `test constructor with active weapon auto filling weapons`() {
        val state = CharacterState(Character("John"), activeWeapons = listOf(ActiveWeapon(smallShield)))

        assertEquals(smallShield, state.weapons.single(), "weapons contain exactly the active weapon")
    }

    @Nested
    inner class `test getDefenseBonus` {

        @Test
        fun `test defenseBonus with no shield`() {
            val initialState = Character("John").getInitialPlayerState()

            assertEquals(0, initialState.getDefenseBonus(), "no defense bonus without shield")
        }

        @Test
        fun `test defenseBonus with shield`() {
            val initialState = Character("John").getInitialPlayerState()
            val characterWithShield = initialState.copy(
                weapons = listOf(smallShield),
                activeWeapons = listOf(ActiveWeapon(smallShield))
            )

            assertEquals(
                smallShield.shieldMode!!.defenseBonus,
                characterWithShield.getDefenseBonus(),
                "defense bonus comes from active shield"
            )
        }

        @Test
        fun `test defenseBonus with multiple shields`() {
            val initialState = Character("John").getInitialPlayerState()
            val characterWithShield = initialState.copy(
                weapons = listOf(smallShield, mediumShield),
                activeWeapons = listOf(ActiveWeapon(smallShield), ActiveWeapon(mediumShield))
            )

            assertEquals(
                mediumShield.shieldMode!!.defenseBonus,
                characterWithShield.getDefenseBonus(),
                "defense bonus comes from best active shield"
            )
        }

        @Test
        fun `test defenseBonus with no active shield`() {
            val initialState = Character("John").getInitialPlayerState()
            val characterWithNoActiveShield = initialState.copy(weapons = listOf(smallShield))

            assertEquals(
                0,
                characterWithNoActiveShield.getDefenseBonus(),
                "only active shields count for active defense"
            )
        }
    }

    @Nested
    inner class `test getActiveDefenseOptions` {
        @Test
        fun `test everyone can dodge`() {
            val initialState = Character("John").getInitialPlayerState()
            val activeDefenseOptions = initialState.getActiveDefenseOptions()

            assertNotNull(activeDefenseOptions.toSingleOrNull(), "minimal characters can only dodge")
            assertEquals(ActiveDefenseType.Dodge, activeDefenseOptions.singleOrNull()?.activeDefenseType)
        }
    }
}
