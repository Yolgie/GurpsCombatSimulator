package at.cnoize.gcs.app.character

import at.cnoize.gcs.app.character.skills.DefaultableFrom
import at.cnoize.gcs.app.character.skills.SKILL_DEFAULTS
import at.cnoize.gcs.app.character.skills.Skill

data class Character(
    val name: String,
    val attributes: Map<Attribute, Int> = emptyMap(),
    val skills: Map<Skill, Int> = emptyMap(),
) {
    fun get(attribute: BasicAttribute): Int {
        return attributes[attribute]
            ?: defaultAttributeLevel
    }

    fun get(characteristic: SecondaryCharacteristic): Int {
        return attributes[characteristic]
            ?: characteristic.derivingFunction.invoke(this)
    }

    fun get(skill: Skill, followDefaults: Boolean = true): Int? {
        val skillValue = skills[skill]
        return if (followDefaults) {
            skillValue ?: skill.getBestDefault()
        } else {
            skillValue
        }
    }

    fun get(value: DefaultableFrom, followDefaults: Boolean = true): Int? {
        return when (value) {
            is BasicAttribute -> get(value)
            is SecondaryCharacteristic -> get(value)
            is Skill -> get(value, followDefaults)
            else -> null // if not handled by other overloaded methods the char does not have it
        }
    }

    fun Skill.getBestDefault(): Int? {
        val skill = this
        val skillDefaults = SKILL_DEFAULTS[skill]
        return skillDefaults
            ?.mapNotNull { (defaultFrom, mod) -> get(defaultFrom, followDefaults = false)?.plus(mod) }
            ?.maxByOrNull { it }
    }

    fun getInitialPlayerState(): CharacterState {
        return CharacterState(this)
    }

    companion object {
        const val defaultAttributeLevel = 10
    }
}
