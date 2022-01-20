package at.cnoize.gcs.app

import at.cnoize.gcs.app.character.BasicAttribute
import at.cnoize.gcs.app.character.Character
import at.cnoize.gcs.app.character.maxOrNull
import at.cnoize.gcs.app.character.skills.Skill
import at.cnoize.gcs.app.weapons.ActiveWeapon
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
        "Player 1 (Higher DX)",
        attributes = mapOf(BasicAttribute.DX to 11),
        skills = mapOf(Skill.AxeMace to 12)
    )
        .getInitialPlayerState()
        .copy(weapons = listOf(axe), activeWeapons = listOf(ActiveWeapon(axe, Ready, Skill.AxeMace)))
    val playerTwo = Character(
        "Player 2 (Higher ST)",
        attributes = mapOf(BasicAttribute.ST to 12),
        skills = mapOf(Skill.AxeMace to 11)
    )
        .getInitialPlayerState()
        .copy(weapons = listOf(axe), activeWeapons = listOf(ActiveWeapon(axe, Ready, Skill.AxeMace)))

    val combatPairingFirstRound = CombatPairing(attacker = playerOne, defender = playerTwo)

    val result = (1..10000).map {
        val combat = generateSequence(seed = combatPairingFirstRound) { pairing ->
            val fight = Fight(pairing)
            val result = fight.attack(
                { combatPairing ->
                    AttackAction(
                        combatPairing.attacker.weapons.first(),
                        combatPairing.attacker.weapons.first().weaponModes.first()
                    )
                },
                { _, _, options -> options.maxOrNull() }
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
