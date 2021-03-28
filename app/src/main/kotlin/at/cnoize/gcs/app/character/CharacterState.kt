package at.cnoize.gcs.app.character

import at.cnoize.gcs.app.weapons.ActiveWeapon
import at.cnoize.gcs.app.weapons.Weapon

// extend this with Major wounds, stunned, posture, ...
data class CharacterState(
    val character: Character,
    val hp: Int,
    val fp: Int,
    val encumbrance: Int = 0,
    val weapons: List<Weapon> = emptyList(),
    val activeWeapons: List<ActiveWeapon> = emptyList()
) {
    fun getDefenseBonus(): Int {
        return activeWeapons
            .mapNotNull { activeWeapon -> activeWeapon.weapon.shieldMode?.defenseBonus }
            .maxOrNull() ?: 0
    }

    fun getActiveDefenseOptions(): List<ActiveDefenseOption> {
        val activeDefenseOptions = mutableListOf<ActiveDefense>()
        activeDefenseOptions.add(Dodge(this))
        activeDefenseOptions.addAll(activeWeapons.map { Parry(this, it) })
        activeDefenseOptions.addAll(activeWeapons.map { Block(this, it) })

        return activeDefenseOptions.map { it.toActiveDefenseOption() }.filterNotNull()
    }
}
