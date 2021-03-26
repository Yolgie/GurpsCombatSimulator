@file:Suppress("MagicNumber", "Unused")

package at.cnoize.gcs.app.weapons

import at.cnoize.gcs.app.character.skills.Skill

val axe = Weapon(
    "Axe",
    listOf(
        WeaponMode(MeleeDamage(AttackMode.Swing, +2), DamageMode.Cutting, Skill.AxeMace),
        WeaponMode(MeleeDamage(AttackMode.Swing, +3), DamageMode.Cutting, Skill.TwoHandedAxeMace),
    )
)
val mace = Weapon(
    "Mace",
    listOf(
        WeaponMode(MeleeDamage(AttackMode.Swing, +3), DamageMode.Crushing, Skill.AxeMace),
        WeaponMode(MeleeDamage(AttackMode.Swing, +4), DamageMode.Crushing, Skill.TwoHandedAxeMace)
    )
)
val pick = Weapon(
    "Pick",
    listOf(
        WeaponMode(MeleeDamage(AttackMode.Swing, +1), DamageMode.Impaling, Skill.AxeMace)
    )
)
