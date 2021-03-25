package at.cnoize.gcs.app.weapons

import at.cnoize.gcs.app.Dice

enum class AttackMode {
    Swing,
    Thrust
}

@Suppress(
    "FunctionParameterNaming", // allow names for basic attributes
    "MagicNumber", "ComplexMethod", // numbers come from B16
)
fun AttackMode.getDamageDice(ST: Int): Dice {
    return when (this) {
        AttackMode.Swing -> when (ST) {
            in 1..2 -> Dice(1, -5)
            in 3..4 -> Dice(1, -4)
            in 5..6 -> Dice(1, -3)
            in 7..8 -> Dice(1, -2)
            9 -> Dice(1, -1)
            10 -> Dice(1, 0)
            11 -> Dice(1, 1)
            12 -> Dice(1, 2)
            13 -> Dice(2, -1)
            14 -> Dice(2, 0)
            15 -> Dice(2, 1)
            16 -> Dice(2, 2)
            17 -> Dice(3, -1)
            18 -> Dice(3, 0)
            19 -> Dice(3, 1)
            20 -> Dice(3, 2)
            else -> throw NotImplementedError("swing damage is not available for this ST")
        }
        AttackMode.Thrust -> when (ST) {
            in 1..2 -> Dice(1, -6)
            in 3..4 -> Dice(1, -5)
            in 5..6 -> Dice(1, -4)
            in 7..8 -> Dice(1, -3)
            in 9..10 -> Dice(1, -2)
            in 11..12 -> Dice(1, -1)
            in 13..14 -> Dice(1, 0)
            in 15..16 -> Dice(1, 1)
            in 17..18 -> Dice(1, 2)
            in 19..20 -> Dice(2, -1)
            else -> throw NotImplementedError("thrust damage is not available for this ST")
        }
    }
}
