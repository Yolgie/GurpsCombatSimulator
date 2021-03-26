package at.cnoize.gcs.app

import at.cnoize.gcs.app.character.ActiveDefense
import at.cnoize.gcs.app.character.BasicAttribute
import at.cnoize.gcs.app.character.Character
import at.cnoize.gcs.app.character.CharacterState
import at.cnoize.gcs.app.character.skills.Skill
import at.cnoize.gcs.app.weapons.ActiveWeapon
import at.cnoize.gcs.app.weapons.Weapon
import at.cnoize.gcs.app.weapons.WeaponMode
import at.cnoize.gcs.app.weapons.WeaponState.Ready
import at.cnoize.gcs.app.weapons.axe
import at.cnoize.util.takeWhileInclusive
import java.util.logging.Logger

@Suppress(
    "MagicNumber", // remove and extract into settings or so
)
fun main() {
    val log = Logger.getLogger("GurpsCombatSimulator App")

    log.info("Hello Logger!")
    println("Hello GURPS!")

    val playerOne = Character(
        "Player 1",
        attributes = mapOf(BasicAttribute.DX to 11),
        skills = mapOf(Skill.AxeMace to 12)
    )
        .getInitialPlayerState()
        .copy(weapons = listOf(axe), activeWeapons = listOf(ActiveWeapon(axe, Ready)))
    val playerTwo = Character(
        "Player 2",
        attributes = mapOf(BasicAttribute.ST to 12),
        skills = mapOf(Skill.AxeMace to 11)
    )
        .getInitialPlayerState()
        .copy(weapons = listOf(axe), activeWeapons = listOf(ActiveWeapon(axe, Ready)))

    val combatPairingFirstRound = CombatPairing(attacker = playerOne, defender = playerTwo)

    val result = (1..10000).map {
        val combat = generateSequence(seed = combatPairingFirstRound) { combatPairing ->
            val result = attack(
                combatPairing,
                AttackAction(
                    combatPairing.attacker.weapons.first(),
                    combatPairing.attacker.weapons.first().modes.first()
                ),
                { _, _ -> ActiveDefense.Dodge }
            )
            return@generateSequence result.switch()
        }

        val (looser, winner) = combat
            .takeWhileInclusive { combatPairing -> combatPairing.attacker.hp > 0 && combatPairing.defender.hp > 0 }
            .last()

        println("${winner.character.name} is the winner with ${winner.hp} vs. ${looser.hp}")
        println("###################################")
        println()

        winner.character
    }
        .groupingBy { it.name }.eachCount()

    println(result.toSortedMap())
}

data class CombatPairing(val attacker: CharacterState, val defender: CharacterState) {
    fun switch() = CombatPairing(attacker = defender, defender = attacker)
}

data class AttackAction(val weapon: Weapon, val weaponMode: WeaponMode)

fun attack(
    combatPairing: CombatPairing,
    attackAction: AttackAction,
    activeDefenseDecision: (CombatPairing, AttackAction) -> ActiveDefense
): CombatPairing {
    val attacker = combatPairing.attacker
    var defender = combatPairing.defender

    println("${attacker.character.name} attacks ${defender.character.name} with ${attackAction.weapon.name}")
    val attackRoll = Dice().roll()
    val attackSkillTarget = attacker.character.get(attackAction.weaponMode.skill)
        ?: throw IllegalStateException("character can not use this weapon mode, even with a default")
    val attackSuccess = attackRoll <= attackSkillTarget
    println(
        "${attacker.character.name} rolls $attackRoll on $attackSkillTarget " +
                "and would ${if (!attackSuccess) "not " else ""}hit."
    )

    if (attackSuccess) {
        val defenseRoll = Dice().roll()
        val activeDefense = activeDefenseDecision.invoke(CombatPairing(attacker, defender), attackAction)
        val defenseTarget = defender.getActiveDefense(activeDefense)
            ?: throw IllegalStateException("we assume that the chosen active defense always is possible")
        val defenseSuccess = defenseRoll <= defenseTarget
        println(
            "${defender.character.name} rolls $defenseRoll on $defenseTarget " +
                    "and would ${if (!defenseSuccess) "not " else ""}defend."
        )

        if (!defenseSuccess) {
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

            defender = defender.copy(hp = defenderNewHp)
        }
    }
    println("The turn is over.")
    println()

    return CombatPairing(attacker, defender)
}
