package at.cnoize.gcs.app

import at.cnoize.gcs.app.weapons.Weapon
import at.cnoize.gcs.app.weapons.WeaponMode
import at.cnoize.gcs.app.weapons.axe
import at.cnoize.gcs.app.weapons.mace
import at.cnoize.util.takeWhileInclusive

@Suppress(
    "MagicNumber", // remove and extract into settings or so
)
fun main() {
    println("Hello GURPS!")

    val playerOne = Character("Player 1", 10, 11, 10, 10)
    val playerOneInitialState = playerOne.getInitialPlayerState()
    val playerOneWithWeapon = playerOneInitialState.copy(weapons = listOf(axe))
    val playerTwo = Character("Player 2", 12, 10, 10, 10)
    val playerTwoInitialState = playerTwo.getInitialPlayerState()
    val playerTwoWithWeapon = playerTwoInitialState.copy(weapons = listOf(mace))

    val combatPairingFirstRound = CombatPairing(attacker = playerOneWithWeapon, defender = playerTwoWithWeapon)

    val combat = generateSequence(seed = combatPairingFirstRound) { combatPairing ->
        val result = attack(
            combatPairing,
            combatPairing.attacker.weapons.first(),
            combatPairing.attacker.weapons.first().modes.first()
        )
        return@generateSequence result.switch()
    }

    val (winner, looser) = combat
        .takeWhileInclusive { combatPairing -> combatPairing.attacker.hp > 0 && combatPairing.defender.hp > 0 }
        .last()

    println("${winner.character.name} is the winner with ${winner.hp} vs. ${looser.hp}")
}

fun attack(
    combatPairing: CombatPairing,
    weapon: Weapon,
    weaponMode: WeaponMode
): CombatPairing {
    val attacker = combatPairing.attacker
    var defender = combatPairing.defender

    println("${attacker.character.name} attacks ${defender.character.name} with ${weapon.name}")
    val attackRoll = Dice().roll()
    val attackSuccess = attackRoll <= attacker.character.primaryWeaponSkill
    println(
        "${attacker.character.name} rolls $attackRoll " +
                "on ${attacker.character.primaryWeaponSkill} " +
                "and would ${if (!attackSuccess) "not " else ""}hit."
    )

    if (attackSuccess) {
        val defenseRoll = Dice().roll()
        val defenseSuccess = defenseRoll <= defender.character.defenseRollTarget
        println(
            "${defender.character.name} rolls $defenseRoll " +
                    "on ${defender.character.defenseRollTarget} " +
                    "and would ${if (!defenseSuccess) "not " else ""}defend."
        )

        if (!defenseSuccess) {
            // add validation that the weapon mode is possible (weapon active)
            val attackDice = with(weaponMode) { attacker.character.toDamageDice() }
            val damageRoll = attackDice.roll()
            val penetratingDamage = damageRoll
            val modifiedDamage = damageRoll
            val defenderNewHp = defender.hp - modifiedDamage
            println(
                "${attacker.character.name} rolls $damageRoll ($attackDice) " +
                        "resulting in $penetratingDamage damage getting through, " +
                        "modified to $modifiedDamage. " +
                        "${defender.character.name} now has $defenderNewHp HP left."
            )

            defender = defender.copy(hp = defenderNewHp)
        }
    }
    println("The turn is over.")
    println()

    return CombatPairing(attacker, defender)
}

data class CombatPairing(val attacker: CharacterState, val defender: CharacterState) {
    fun switch() = CombatPairing(attacker = defender, defender = attacker)
}
