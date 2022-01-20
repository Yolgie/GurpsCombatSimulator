package at.cnoize.gcs.app

import at.cnoize.gcs.app.character.ActiveDefenseOption
import at.cnoize.gcs.app.character.CharacterState
import at.cnoize.gcs.app.weapons.Weapon
import at.cnoize.gcs.app.weapons.WeaponMode

typealias attackDecision = (CombatPairing) -> AttackAction?
typealias defenseDecision = (CombatPairing, AttackAction, List<ActiveDefenseOption>) -> ActiveDefenseOption?

data class CombatPairing(val attacker: CharacterState, val defender: CharacterState) {
    fun switch() = CombatPairing(attacker = defender, defender = attacker)
}

data class AttackAction(val weapon: Weapon, val weaponMode: WeaponMode)

data class Fight(val combatPairing: CombatPairing) {
    fun attack(
        attackDecision: attackDecision,
        defenseDecision: defenseDecision
    ): CombatPairing {
        val attackAction = attackDecision.invoke(combatPairing) ?: return combatPairing
        val attackSuccess = handleAttackRoll(attackAction)

        val defenderAfterAttack = if (attackSuccess) {
            val defenseAction = defenseDecision.invoke(
                combatPairing,
                attackAction,
                combatPairing.defender.getActiveDefenseOptions()
            )
            val defenseSuccess = handleDefenseRoll(defenseAction)

            if (!defenseSuccess) {
                handleAttack(attackAction)
            } else null
        } else null

        println("The turn is over.")
        println()

        return CombatPairing(combatPairing.attacker, defenderAfterAttack ?: combatPairing.defender)
    }

    fun handleAttackRoll(
        attackAction: AttackAction
    ): Boolean {
        val attacker = combatPairing.attacker
        val defender = combatPairing.defender

        println("${attacker.character.name} attacks ${defender.character.name} with ${attackAction.weapon.name}")
        val attackRoll = Dice().roll()
        val attackSkillTarget = attacker.character.get(attackAction.weaponMode.skill)
            ?: throw IllegalStateException("character can not use this weapon mode, even with a default")
        val attackSuccess = attackRoll <= attackSkillTarget
        println(
            "${attacker.character.name} rolls $attackRoll on $attackSkillTarget " +
                    "and would ${if (!attackSuccess) "not " else ""}hit."
        )
        return attackSuccess
    }

    fun handleDefenseRoll(defenseAction: ActiveDefenseOption?): Boolean {
        val defender = combatPairing.defender
        return if (defenseAction == null) {
            println("${defender.character.name} does not defend.")
            false
        } else {
            val defenseRoll = Dice().roll()
            val defenseSuccess = defenseRoll <= defenseAction.defenseValue
            println(
                "${defender.character.name} rolls $defenseRoll " +
                        "on ${defenseAction.activeDefenseType} ${defenseAction.defenseValue} " +
                        "and would ${if (!defenseSuccess) "not " else ""}defend."
            )
            defenseSuccess
        }
    }

    fun handleAttack(
        attackAction: AttackAction
    ): CharacterState {
        val attacker = combatPairing.attacker
        val defender = combatPairing.defender

        // add validation that the weapon mode is possible (weapon active)
        val attackDice = with(attackAction.weaponMode) { attacker.character.toDamageDice() }
        val damageRoll = attackDice.roll()
        val penetratingDamage = damageRoll
        val modifiedDamage = penetratingDamage
        val defenderNewHp = defender.hp - modifiedDamage
        println(
            "${attacker.character.name} rolls $damageRoll ($attackDice) " +
                    "resulting in $penetratingDamage damage getting through, " +
                    "modified to $modifiedDamage. " +
                    "${defender.character.name} now has $defenderNewHp HP left."
        )

        return defender.copy(hp = defenderNewHp)
    }
}
