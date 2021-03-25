package at.cnoize.gcs.app

import at.cnoize.gcs.app.weapons.ActiveWeapon
import at.cnoize.gcs.app.weapons.Weapon

@Suppress(
    "kotlin:S117", "ConstructorParameterNaming", // allow names for basic attributes
    "MagicNumber" // allow for relative defaults
)
data class Character(
    val name: String,
    val ST: Int,
    val DX: Int,
    val IQ: Int,
    val HT: Int,
    val HP: Int = ST,
    val FP: Int = HT,
    val primaryWeaponSkill: Int = DX - 1,
    val defenseRollTarget: Int = primaryWeaponSkill / 2 + 3
) {
    fun getInitialPlayerState(): CharacterState {
        return CharacterState(this, this.HP, this.FP)
    }
}

// extend this with Major wounds, stunned, posture, ...
data class CharacterState(
    val character: Character,
    val hp: Int,
    val fp: Int,
    val weapons: List<Weapon> = emptyList(),
    val activeWeapons: List<ActiveWeapon> = emptyList()
) {
    fun getDamage(): Dice {
        // this assumes just takes the first weapon mode in the first active weapon
        return with(activeWeapons.first().weapon.modes.first()) { character.toDamageDice() }
    }
}

