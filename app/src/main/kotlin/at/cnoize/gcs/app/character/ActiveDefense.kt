package at.cnoize.gcs.app.character

import at.cnoize.gcs.app.character.SecondaryCharacteristic.BasicSpeedNoFractions
import at.cnoize.gcs.app.weapons.ActiveWeapon

interface ActiveDefense {
    val characterState: CharacterState
    val activeDefenseType: ActiveDefenseType

    fun getActiveDefenseValue(): Int?

    fun toActiveDefenseOption(): ActiveDefenseOption?
}

class Dodge(override val characterState: CharacterState) : ActiveDefense {
    override val activeDefenseType = ActiveDefenseType.Dodge

    @Suppress("MagicNumber") // B374
    override fun getActiveDefenseValue(): Int {
        val basicSpeed = characterState.character.get(BasicSpeedNoFractions)
        val encumbrance = characterState.encumbrance
        val defenseBonus = characterState.getDefenseBonus()
        return basicSpeed + 3 - encumbrance + defenseBonus
    }

    override fun toActiveDefenseOption(): ActiveDefenseOption {
        return ActiveDefenseOption(
            activeDefenseType,
            getActiveDefenseValue(),
            characterState.getDefenseBonus()
        )
    }
}

class Parry(override val characterState: CharacterState, val weapon: ActiveWeapon) : ActiveDefense {
    override val activeDefenseType = ActiveDefenseType.Parry

    @Suppress("MagicNumber") // B376
    override fun getActiveDefenseValue(): Int? {
        val skill = characterState.character.get(weapon.usedSkill) ?: return null
        val defenseBonus = characterState.getDefenseBonus()
        return skill / 2 + 3 + defenseBonus
    }

    override fun toActiveDefenseOption(): ActiveDefenseOption? {
        return getActiveDefenseValue()?.let {
            ActiveDefenseOption(
                activeDefenseType,
                it,
                characterState.getDefenseBonus(),
                weapon
            )
        }
    }
}

class Block(override val characterState: CharacterState, val shield: ActiveWeapon) : ActiveDefense {
    override val activeDefenseType = ActiveDefenseType.Block

    @Suppress("MagicNumber") // B375
    override fun getActiveDefenseValue(): Int? {
        val skill = shield.weapon.shieldMode?.skill?.let { characterState.character.get(it) } ?: return null
        return skill / 2 + 3 + shield.weapon.shieldMode.defenseBonus
    }

    override fun toActiveDefenseOption(): ActiveDefenseOption? {
        return getActiveDefenseValue()?.let {
            ActiveDefenseOption(
                activeDefenseType,
                it,
                shield.weapon.shieldMode?.defenseBonus ?: 0,
                shield
            )
        }
    }
}

enum class ActiveDefenseType {
    Dodge,
    Parry,
    Block
}

data class ActiveDefenseOption(
    val activeDefenseType: ActiveDefenseType,
    val defenseValue: Int,
    val includedDefenseBonus: Int = 0,
    val activeWeapon: ActiveWeapon? = null
)

fun List<ActiveDefenseOption>.maxOrNull(): ActiveDefenseOption? {
    return maxByOrNull(ActiveDefenseOption::defenseValue)
}
