package at.cnoize.gcs.app

import kotlin.random.Random
import kotlin.random.nextInt

@Suppress(
        "MagicNumber", // remove and extract into settings or so
        "UnusedPrivateMember", "kotlin:S1481", "UNUSED_VARIABLE", // ignore until some more code is added
)
fun main() {
    println("Hello GURPS!")

    val axe = Weapon(listOf(WeaponMode(MeleeDamage(AttackMode.Swing, +2), DamageMode.Cutting)))
    val mace = Weapon(listOf(WeaponMode(MeleeDamage(AttackMode.Swing, +3), DamageMode.Crushing)))
    val pick = Weapon(listOf(WeaponMode(MeleeDamage(AttackMode.Swing, +1), DamageMode.Impaling)))

    val playerOne = Character(10, 11, 10, 10, HP = 12)
    val playerOneInitialState = playerOne.getPlayerState()
    val playerTwo = Character(12, 10, 10, 10, FP = 12)
    val playerTwoInitialState = playerTwo.getPlayerState()

    val playerOneWithAxe = CharacterWieldingWeapon(playerOne, axe)
    val damageWithAxe = playerOneWithAxe.getDamage()

    val damageToPlayerTwo = with(FixedRollProviderService(3)) { damageWithAxe.roll() }
    println("Player one attacks player two with an axe and does $damageWithAxe damage (=$damageToPlayerTwo)")
    val newPlayerTwoState = playerTwoInitialState.run { copy(hp = hp-damageToPlayerTwo) }

    println("Player two initial state: $playerTwoInitialState")
    println("Player two final state: $newPlayerTwoState")
}
