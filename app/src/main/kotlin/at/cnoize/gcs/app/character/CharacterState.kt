package at.cnoize.gcs.app.character

import at.cnoize.gcs.app.weapons.ActiveWeapon
import at.cnoize.gcs.app.weapons.Weapon

// extend this with Major wounds, stunned, posture, ...
data class CharacterState(
    val character: Character,
    val hp: Int = character.get(SecondaryCharacteristic.HP),
    val fp: Int = character.get(SecondaryCharacteristic.FP),
    val encumbrance: Int = 0,
    val weapons: List<Weapon> = emptyList(),
    val activeWeapons: List<ActiveWeapon> = emptyList()
) {
    constructor(
        character: Character,
        hp: Int = character.get(SecondaryCharacteristic.HP),
        fp: Int = character.get(SecondaryCharacteristic.FP),
        encumbrance: Int = 0,
        activeWeapons: List<ActiveWeapon> = emptyList()
    ) : this(character, hp, fp, encumbrance, activeWeapons.map(ActiveWeapon::weapon), activeWeapons)

    private val activeWeaponsWithShieldMode =
        activeWeapons.filter { it.weapon.shieldMode != null }
    private val activeWeaponsWithWeaponModes =
        activeWeapons.filter { it.weapon.weaponModes.isNotEmpty() && it.weapon.shieldMode == null }

    init {
        require(
            weapons.containsAll(activeWeapons.map(ActiveWeapon::weapon))
        ) { "all active weapons must be in the weapons list as well" }
    }

    fun getDefenseBonus(): Int {
        return activeWeaponsWithShieldMode
            .mapNotNull { shield -> shield.weapon.shieldMode?.defenseBonus }
            .maxOrNull() ?: 0
    }

    fun getActiveDefenseOptions(): List<ActiveDefenseOption> {
        val activeDefenseOptions = mutableListOf<ActiveDefense>()
        activeDefenseOptions.add(Dodge(this))
        activeDefenseOptions.addAll(activeWeaponsWithWeaponModes.map { Parry(this, it) })
        activeDefenseOptions.addAll(activeWeaponsWithShieldMode.map { Block(this, it) })

        return activeDefenseOptions.mapNotNull { it.toActiveDefenseOption() }
    }
}
