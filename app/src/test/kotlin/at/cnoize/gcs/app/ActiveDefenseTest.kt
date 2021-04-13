package at.cnoize.util.at.cnoize.gcs.app

import at.cnoize.gcs.app.character.ActiveDefenseOption
import at.cnoize.gcs.app.character.ActiveDefenseType
import at.cnoize.gcs.app.character.Block
import at.cnoize.gcs.app.character.Character
import at.cnoize.gcs.app.character.Dodge
import at.cnoize.gcs.app.character.Parry
import at.cnoize.gcs.app.character.SecondaryCharacteristic
import at.cnoize.gcs.app.character.maxOrNull
import at.cnoize.gcs.app.character.skills.Skill
import at.cnoize.gcs.app.weapons.ActiveWeapon
import at.cnoize.gcs.app.weapons.AttackMode
import at.cnoize.gcs.app.weapons.DamageMode
import at.cnoize.gcs.app.weapons.MeleeDamage
import at.cnoize.gcs.app.weapons.Weapon
import at.cnoize.gcs.app.weapons.WeaponMode
import at.cnoize.gcs.app.weapons.smallShield
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ActiveDefenseTest {

    @Nested
    inner class `test Dodge` {
        @TestFactory
        fun `test getActiveDefenseValue from basicSpeed`() = mapOf(
            300 to 6,
            400 to 7,
            500 to 8,
            600 to 9,
            700 to 10,
            525 to 8,
            550 to 8,
            575 to 8
        ).map { (basicSpeed, expectedDodge) ->
            dynamicTest("expecting character with BasicSpeed $basicSpeed(/100) to have a dodge of $expectedDodge") {
                val characterState =
                    Character("Player", attributes = mapOf(SecondaryCharacteristic.BasicSpeed100 to basicSpeed))
                        .getInitialPlayerState()
                assertEquals(expectedDodge, Dodge(characterState).getActiveDefenseValue())
            }
        }

        @TestFactory
        fun `test getActiveDefenseValue from basicSpeed with medium encumbrance`() = mapOf(
            475 to 5,
            500 to 6,
            525 to 6,
            550 to 6,
            575 to 6,
            600 to 7
        ).map { (basicSpeed, expectedDodge) ->
            dynamicTest("expecting character with BasicSpeed $basicSpeed(/100) and medium encumbrance to have a dodge of $expectedDodge") {
                val characterState =
                    Character("Player", attributes = mapOf(SecondaryCharacteristic.BasicSpeed100 to basicSpeed))
                        .getInitialPlayerState().copy(encumbrance = 2)
                assertEquals(expectedDodge, Dodge(characterState).getActiveDefenseValue())
            }
        }

        @Test
        fun `test activeDefenseType`() {
            val character = Character("Bob").getInitialPlayerState()
            val dodge = Dodge(character)
            assertEquals(ActiveDefenseType.Dodge, dodge.activeDefenseType)
        }

        @Test
        fun `test mapper to ActiveDefenseOption`() {
            val character = Character("Bob").getInitialPlayerState()
            val activeDefense = Dodge(character)
            val activeDefenseOption = activeDefense.toActiveDefenseOption()

            assertEquals(activeDefense.activeDefenseType, activeDefenseOption.activeDefenseType)
            assertEquals(activeDefense.getActiveDefenseValue(), activeDefenseOption.defenseValue)
            assertEquals(character.getDefenseBonus(), activeDefenseOption.includedDefenseBonus)
            assertNull(activeDefenseOption.activeWeapon)
        }
    }

    @Nested
    inner class `test Parry` {
        val skill = Skill.Broadsword
        val simpleWeapon = Weapon(
            "simple weapon",
            WeaponMode(MeleeDamage(AttackMode.Swing, +0), DamageMode.Cutting, skill)
        )

        @TestFactory
        fun `test getActiveDefenseValue from a simple weapon`() = mapOf(
            9 to 7,
            10 to 8,
            11 to 8,
            12 to 9,
            13 to 9,
            14 to 10,
            15 to 10,
            16 to 11,
            17 to 11
        ).map { (skillValue, expectedParry) ->
            dynamicTest("expecting character with weapon skill $skillValue to have a parry of $expectedParry") {
                val activeWeapon = ActiveWeapon(simpleWeapon)
                val character = Character("Warrior", skills = mapOf(skill to skillValue))
                    .getInitialPlayerState().copy(activeWeapons = listOf(activeWeapon))
                assertEquals(expectedParry, Parry(character, activeWeapon).getActiveDefenseValue())
            }
        }

        @Test
        fun `test activeDefenseType`() {
            val character = Character("Bob", skills = mapOf(skill to 10)).getInitialPlayerState()
            val parry = Parry(character, ActiveWeapon(simpleWeapon))
            assertEquals(ActiveDefenseType.Parry, parry.activeDefenseType)
        }

        @Test
        fun `test mapper to ActiveDefenseOption gives null when no weapon skill is found`() {
            val character = Character("Bob", skills = mapOf(skill to 10)).getInitialPlayerState()
            val weaponWithNoDefaultSkill = Weapon(
                "weapon with no default",
                WeaponMode(MeleeDamage(AttackMode.Thrust, -1), DamageMode.Crushing, Skill.Bolas)
            )
            val activeDefense = Parry(character, ActiveWeapon(weaponWithNoDefaultSkill))
            val activeDefenseOption = activeDefense.toActiveDefenseOption()

            assertNull(activeDefenseOption, "you should not be able to parry without skill")
        }

        @Test
        fun `test mapper to ActiveDefenseOption`() {
            val character = Character("Bob", skills = mapOf(skill to 10)).getInitialPlayerState()
            val activeDefense = Parry(character, ActiveWeapon(simpleWeapon))
            val activeDefenseOption = activeDefense.toActiveDefenseOption()

            assertEquals(activeDefense.activeDefenseType, activeDefenseOption?.activeDefenseType)
            assertEquals(activeDefense.getActiveDefenseValue(), activeDefenseOption?.defenseValue)
            assertEquals(character.getDefenseBonus(), activeDefenseOption?.includedDefenseBonus)
            assertEquals(activeDefense.weapon, activeDefenseOption?.activeWeapon)
        }
    }

    @Nested
    inner class `test Block` {
        @TestFactory
        fun `test getActiveDefenseValue from a simple shield`() = mapOf(
            9 to 8,
            10 to 9,
            11 to 9,
            12 to 10,
            13 to 10,
            14 to 11,
            15 to 11,
            16 to 12,
            17 to 12
        ).map { (skillValue, expectedBlock) ->
            dynamicTest("expecting character with shield skill $skillValue and DB 1 shield to have a block of $expectedBlock") {
                assertEquals(1, smallShield.shieldMode!!.defenseBonus)
                val activeShield = ActiveWeapon(smallShield)
                val character = Character("Coward", skills = mapOf(activeShield.usedSkill to skillValue))
                    .getInitialPlayerState().copy(activeWeapons = listOf(activeShield))
                assertEquals(expectedBlock, Block(character, activeShield).getActiveDefenseValue())
            }
        }

        @Test
        fun `test activeDefenseType`() {
            val character = Character("Shield Wielder", skills = mapOf(Skill.Shield to 10)).getInitialPlayerState()
            val block = Block(character, ActiveWeapon(smallShield))
            assertEquals(ActiveDefenseType.Block, block.activeDefenseType)
        }

        @Test
        fun `test mapper to ActiveDefenseOption`() {
            val character = Character("Shield Wielder", skills = mapOf(Skill.Shield to 10)).getInitialPlayerState()
            val activeDefense = Block(character, ActiveWeapon(smallShield))
            val activeDefenseOption = activeDefense.toActiveDefenseOption()

            assertEquals(activeDefense.activeDefenseType, activeDefenseOption?.activeDefenseType)
            assertEquals(activeDefense.getActiveDefenseValue(), activeDefenseOption?.defenseValue)
            assertEquals(smallShield.shieldMode!!.defenseBonus, activeDefenseOption?.includedDefenseBonus)
            assertEquals(activeDefense.shield, activeDefenseOption?.activeWeapon)
        }
    }

    @Nested
    inner class `test maxOrNull` {
        @Test
        fun `test emptyList`() {
            val activeDefenseOptions: List<ActiveDefenseOption> = emptyList()
            val result = activeDefenseOptions.maxOrNull()
            assertNull(result)
        }

        @Test
        fun `test single item`() {
            val activeDefenseOption = ActiveDefenseOption(ActiveDefenseType.Block, 8)
            val activeDefenseOptions: List<ActiveDefenseOption> = listOf(activeDefenseOption)
            val result = activeDefenseOptions.maxOrNull()
            assertEquals(activeDefenseOption, result)
        }

        @Test
        fun `test multiple equal items`() {
            val activeDefenseOptions: List<ActiveDefenseOption> = listOf(
                ActiveDefenseOption(ActiveDefenseType.Block, 8),
                ActiveDefenseOption(ActiveDefenseType.Parry, 8),
                ActiveDefenseOption(ActiveDefenseType.Dodge, 7)
            )
            val result = activeDefenseOptions.maxOrNull()
            assertNotNull(result)
            assertEquals(8, result.defenseValue)
            assert(result.activeDefenseType in listOf(ActiveDefenseType.Block, ActiveDefenseType.Parry))
        }

        @Test
        fun `test unique result`() {
            val activeDefenseOptions: List<ActiveDefenseOption> = listOf(
                ActiveDefenseOption(ActiveDefenseType.Block, 11),
                ActiveDefenseOption(ActiveDefenseType.Parry, 10),
                ActiveDefenseOption(ActiveDefenseType.Dodge, 12)
            )
            val result = activeDefenseOptions.maxOrNull()
            assertNotNull(result)
            assertEquals(12, result.defenseValue)
            assertEquals(ActiveDefenseType.Dodge, result.activeDefenseType)
        }
    }
}
