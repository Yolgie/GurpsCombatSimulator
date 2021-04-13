package at.cnoize.util.at.cnoize.gcs.app

import at.cnoize.gcs.app.character.skills.Skill
import at.cnoize.gcs.app.weapons.ActiveWeapon
import at.cnoize.gcs.app.weapons.AttackMode
import at.cnoize.gcs.app.weapons.DamageMode
import at.cnoize.gcs.app.weapons.MeleeDamage
import at.cnoize.gcs.app.weapons.ShieldMode
import at.cnoize.gcs.app.weapons.Weapon
import at.cnoize.gcs.app.weapons.WeaponMode
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ActiveWeaponTest {

    @Test
    fun `test autodetecting skill for weapon with only one weapon mode`() {
        val simpleWeapon =
            Weapon("simple weapon", WeaponMode(MeleeDamage(AttackMode.Swing, +0), DamageMode.Cutting, Skill.AxeMace))

        val activeWeapon = ActiveWeapon(simpleWeapon)

        assertEquals(Skill.AxeMace, activeWeapon.usedSkill)
    }

    @Test
    fun `test autodetecting skill for weapon with only one shield mode`() {
        val simpleWeapon =
            Weapon("simple shield", ShieldMode(1, Skill.AxeMace))

        val activeWeapon = ActiveWeapon(simpleWeapon)

        assertEquals(Skill.AxeMace, activeWeapon.usedSkill)
    }

    @Test
    fun `test autodetecting skill for weapon with same skill in weapon modes`() {
        val multiuseWeapon = Weapon(
            "multiuse weapon",
            listOf(
                WeaponMode(MeleeDamage(AttackMode.Swing, +0), DamageMode.Cutting, Skill.AxeMace),
                WeaponMode(MeleeDamage(AttackMode.Thrust, +2), DamageMode.Impaling, Skill.AxeMace),
                WeaponMode(MeleeDamage(AttackMode.Swing, -1), DamageMode.Crushing, Skill.AxeMace),
            )
        )

        val activeWeapon = ActiveWeapon(multiuseWeapon)

        assertEquals(Skill.AxeMace, activeWeapon.usedSkill)
    }

    @Test
    fun `test autodetecting skill for weapon with same skill in weapon modes & shield skill`() {
        val multiuseWeapon = Weapon(
            "multiuse weapon shield combo",
            listOf(
                WeaponMode(MeleeDamage(AttackMode.Swing, +0), DamageMode.Cutting, Skill.AxeMace),
                WeaponMode(MeleeDamage(AttackMode.Swing, -1), DamageMode.Crushing, Skill.AxeMace),
            ),
            ShieldMode(1, Skill.AxeMace)
        )

        val activeWeapon = ActiveWeapon(multiuseWeapon)

        assertEquals(Skill.AxeMace, activeWeapon.usedSkill)
    }

    @Test
    fun `can not autodetect skill for weapon with different skills in weapon modes`() {
        val versatileWeapon = Weapon(
            "versatile weapon",
            listOf(
                WeaponMode(MeleeDamage(AttackMode.Swing, +0), DamageMode.Crushing, Skill.AxeMace),
                WeaponMode(MeleeDamage(AttackMode.Thrust, +2), DamageMode.Impaling, Skill.AxeMace),
                WeaponMode(MeleeDamage(AttackMode.Swing, -1), DamageMode.Cutting, Skill.Broadsword),
            )
        )

        val exception = assertThrows<IllegalArgumentException> { ActiveWeapon(versatileWeapon) }
        assertNotNull(exception.message) { "autodetect with different skills throws with message" }
        assertEquals("Cannot deduct weapon skill, please specify.", exception.message)
    }

    @Test
    fun `can not autodetect skill for weapon with different skills in weapon & shield modes`() {
        val weirdWeapon = Weapon(
            "very weird weapon",
            listOf(
                WeaponMode(MeleeDamage(AttackMode.Swing, +0), DamageMode.Crushing, Skill.AxeMace),
                WeaponMode(MeleeDamage(AttackMode.Thrust, +2), DamageMode.Impaling, Skill.AxeMace),
            ),
            ShieldMode(1, Skill.Shield)
        )

        val exception = assertThrows<IllegalArgumentException> { ActiveWeapon(weirdWeapon) }
        assertNotNull(exception.message) { "autodetect with different skills throws with message" }
        assertEquals("Cannot deduct weapon skill, please specify.", exception.message)
    }
}
