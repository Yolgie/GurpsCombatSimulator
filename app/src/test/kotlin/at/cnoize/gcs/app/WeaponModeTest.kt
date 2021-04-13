package at.cnoize.gcs.app

import at.cnoize.gcs.app.character.BasicAttribute
import at.cnoize.gcs.app.character.Character
import at.cnoize.gcs.app.character.skills.Skill
import at.cnoize.gcs.app.weapons.AttackMode
import at.cnoize.gcs.app.weapons.DamageMode
import at.cnoize.gcs.app.weapons.MeleeDamage
import at.cnoize.gcs.app.weapons.WeaponMode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

class WeaponModeTest {

    data class WeaponModeTestCase(val st: Int, val meleeDamageMod: Int, val expectedDamageDice: Dice)

    @TestFactory
    fun `test toDamageDice`() = listOf(
        WeaponModeTestCase(10, +0, Dice(1, +0)),
        WeaponModeTestCase(10, +1, Dice(1, +1)),
        WeaponModeTestCase(8, +3, Dice(1, +1)),
        WeaponModeTestCase(15, +2, Dice(2, +3)),
    ).map { (st, meleeDamageMod, expectedDamageDice) ->
        dynamicTest("expecting character with ST $st and weapon with thr+$meleeDamageMod to do $expectedDamageDice of damage") {
            val character = Character("Bob", attributes = mapOf(BasicAttribute.ST to st))
            val weaponMode = WeaponMode(
                meleeDamage = MeleeDamage(AttackMode.Swing, meleeDamageMod),
                damageMode = DamageMode.Crushing,
                skill = Skill.Brawling
            )

            val result = with(weaponMode) { character.toDamageDice() }

            assertEquals(expectedDamageDice, result)
        }


    }
}
