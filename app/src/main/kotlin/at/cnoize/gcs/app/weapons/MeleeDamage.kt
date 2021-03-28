package at.cnoize.gcs.app.weapons

import at.cnoize.gcs.app.Dice

data class MeleeDamage(val attackMode: AttackMode, val dice: Dice) {
    constructor(attackMode: AttackMode, mod: Int = 0) : this(attackMode, Dice(0, mod))
}
