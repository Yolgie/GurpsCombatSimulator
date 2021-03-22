package at.cnoize.gcs.app

@Suppress("kotlin:S117", "ConstructorParameterNaming") // allow names for basic attributes
data class Character(val ST: Int, val DX: Int, val IQ: Int, val HT: Int, val HP: Int = ST, val FP: Int = HT)

// extend this with Major wounds, stunned, posture, ...
data class CharacterState(val character: Character, val hp: Int, val fp: Int)

fun Character.getPlayerState(): CharacterState {
    return CharacterState(this, this.HP, this.FP)
}

data class CharacterWieldingWeapon(val character: Character, val weapon: Weapon) {
    fun getDamage(): Dice {
        // this assumes just takes the first weapon mode in the weapon
        return with(weapon.modes.first()) { character.toDamageDice() }
    }
}
