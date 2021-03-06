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
    WeaponMode(MeleeDamage(AttackMode.Swing, +1), DamageMode.Impaling, Skill.AxeMace)
)

val smallShield = Weapon(
    "Small Shield",
    WeaponMode(MeleeDamage(AttackMode.Thrust), DamageMode.Crushing, Skill.Shield),
    ShieldMode(1, Skill.Shield)
)

val mediumShield = Weapon(
    "Medium Shield",
    WeaponMode(MeleeDamage(AttackMode.Thrust), DamageMode.Crushing, Skill.Shield),
    ShieldMode(2, Skill.Shield)
)

val lightCloak = Weapon(
    "Light Cloak",
    shieldMode = ShieldMode(1, Skill.Cloak)
)

val heavyCloak = Weapon(
    "Heavy Cloak",
    shieldMode = ShieldMode(2, Skill.Cloak)
)
