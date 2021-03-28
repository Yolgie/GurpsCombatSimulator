package at.cnoize.gcs.app.character

import at.cnoize.gcs.app.character.skills.DefaultFrom
import at.cnoize.gcs.app.character.skills.Skill
import at.cnoize.gcs.app.character.skills.SkillDefaults

@Suppress(
    "kotlin:S117", "ConstructorParameterNaming", // allow names for basic attributes
    "MagicNumber" // allow for relative defaults
)
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

