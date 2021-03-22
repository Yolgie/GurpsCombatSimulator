package at.cnoize.gcs.app

data class Weapon(val modes: Collection<WeaponMode>)

data class WeaponMode(
        val meleeDamage: MeleeDamage,
        val damageMode: DamageMode
) {
    fun Character.toDamageDice(): Dice {
        return meleeDamage.attackMode.getDamageDice(ST) + meleeDamage.dice
    }
}

data class MeleeDamage(val attackMode: AttackMode, val dice: Dice) {
    constructor(attackMode: AttackMode, mod: Int) : this(attackMode, Dice(0, mod))
}
