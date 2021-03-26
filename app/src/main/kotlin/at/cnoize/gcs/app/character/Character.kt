package at.cnoize.gcs.app.character

import at.cnoize.gcs.app.Dice
import at.cnoize.gcs.app.character.skills.DefaultFrom
import at.cnoize.gcs.app.character.skills.Skill
import at.cnoize.gcs.app.character.skills.SkillDefaults
import at.cnoize.gcs.app.weapons.ActiveWeapon
import at.cnoize.gcs.app.weapons.Weapon
import at.cnoize.gcs.app.weapons.WeaponState

@Suppress(
    "kotlin:S117", "ConstructorParameterNaming", // allow names for basic attributes
    "MagicNumber" // allow for relative defaults
)
data class Character(
    val name: String,
    val attributes: Map<Attribute, Int>,
    val skills: Map<Skill, Int>,
) {
    fun get(attribute: BasicAttribute): Int {
        return attributes[attribute]
            ?: defaultAttributeLevel
    }

    fun get(characteristic: SecondaryCharacteristic): Int {
        return attributes[characteristic]
            ?: characteristic.derivingFunction.invoke(this)
    }

    fun get(skill: Skill): Int? {
        return skills[skill]
            ?: skill.getBestDefault()
    }

    fun get(defaultFrom: DefaultFrom): Int? {
        return when (defaultFrom) {
            is BasicAttribute -> get(defaultFrom)
            is SecondaryCharacteristic -> get(defaultFrom)
            is Skill -> get(defaultFrom)
            else -> null // if not handled by other overloaded methods the char does not have it
        }
    }

    fun Skill.getBestDefault(): Int? {
        val skill = this
        val skillDefaults = SkillDefaults[skill]
        return skillDefaults
            ?.mapNotNull { (defaultFrom, mod) -> get(defaultFrom)?.plus(mod) }
            ?.maxByOrNull { it }
    }

    fun getInitialPlayerState(): CharacterState {
        return CharacterState(
            this,
            get(SecondaryCharacteristic.HP),
            get(SecondaryCharacteristic.FP)
        )
    }

    companion object {
        const val defaultAttributeLevel = 10
    }
}

// extend this with Major wounds, stunned, posture, ...
data class CharacterState(
    val character: Character,
    val hp: Int,
    val fp: Int,
    val encumbrance: Int = 0,
    val weapons: List<Weapon> = emptyList(),
    val activeWeapons: List<ActiveWeapon> = emptyList()
) {
    fun getDamage(): Dice {
        // this assumes just takes the first weapon mode in the first active weapon
        return with(activeWeapons.first().weapon.modes.first()) { character.toDamageDice() }
    }

    @Suppress("MagicNumber") // rules
    fun getActiveDefense(activeDefense: ActiveDefense) : Int? {
        // currently completely ignoring multiple defense restrictions/modifiers
        return when (activeDefense) {
            ActiveDefense.Dodge -> character.get(SecondaryCharacteristic.BasicSpeedNoFractions)+3-encumbrance
            ActiveDefense.Parry -> activeWeapons
                .filter { activeWeapon -> activeWeapon.state == WeaponState.Ready }
                .flatMap { activeWeapon -> activeWeapon.weapon.modes }
                .mapNotNull { weaponMode -> character.get(weaponMode.skill) }
                .maxOrNull()
            ActiveDefense.Block -> throw NotImplementedError("Shields not yet implemented")
        }
    }
}
