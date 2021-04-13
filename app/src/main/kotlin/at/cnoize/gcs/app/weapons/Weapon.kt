package at.cnoize.gcs.app.weapons

import at.cnoize.gcs.app.character.skills.Skill
import at.cnoize.util.toSingleOrNull

data class Weapon(
    val name: String,
    val modes: Collection<WeaponMode> = emptyList(),
    val shieldMode: ShieldMode? = null
) {
    constructor(name: String, mode: WeaponMode, shieldMode: ShieldMode? = null) : this(name, listOf(mode), shieldMode)
    constructor(name: String, shieldMode: ShieldMode) : this(name, emptyList(), shieldMode)

    init {
        require(modes.isNotEmpty() || shieldMode != null) { "Weapon has to have at least one mode" }
    }
}

data class ActiveWeapon(
    val weapon: Weapon,
    val state: WeaponState = WeaponState.Ready,
    val usedSkill: Skill
) {
    constructor(weapon: Weapon, state: WeaponState = WeaponState.Ready) : this(
        weapon,
        state,
        (weapon.modes.map(WeaponMode::skill) + weapon.shieldMode?.skill)
            .filterNotNull()
            .toSingleOrNull()
            ?: throw IllegalArgumentException("Cannot deduct weapon skill, please specify.")
    )
}

data class ShieldMode(
    val defenseBonus: Int,
    val skill: Skill
)
