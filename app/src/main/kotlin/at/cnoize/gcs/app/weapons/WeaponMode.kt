package at.cnoize.gcs.app.weapons

import at.cnoize.gcs.app.character.Character
import at.cnoize.gcs.app.Dice
import at.cnoize.gcs.app.character.BasicAttribute
import at.cnoize.gcs.app.character.skills.Skill

data class WeaponMode(
    val meleeDamage: MeleeDamage,
    val damageMode: DamageMode,
    val skill: Skill
) {
    fun Character.toDamageDice(): Dice {
        return meleeDamage.attackMode.getDamageDice(get(BasicAttribute.ST)) + meleeDamage.dice
    }
}
