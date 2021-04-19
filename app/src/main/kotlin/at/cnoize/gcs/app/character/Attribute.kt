@file:Suppress("MagicNumber", "unused") // rules

package at.cnoize.gcs.app.character

import at.cnoize.gcs.app.character.BasicAttribute.DX
import at.cnoize.gcs.app.character.BasicAttribute.HT
import at.cnoize.gcs.app.character.BasicAttribute.IQ
import at.cnoize.gcs.app.character.BasicAttribute.ST
import at.cnoize.gcs.app.character.skills.DefaultableFrom

interface Attribute : DefaultableFrom

enum class BasicAttribute : Attribute {
    ST,
    DX,
    IQ,
    HT
}

enum class SecondaryCharacteristic(val derivingFunction: (Character) -> Int) : Attribute {
    HP({ character -> character.get(ST) }),
    FP({ character -> character.get(HT) }),
    Will({ character -> character.get(IQ) }),
    Per({ character -> character.get(IQ) }),

    /** BasicSpeed has 0.25/0.5/0.75 values so *100 to be able to keep it as an int */
    BasicSpeed100({ character ->
        100 * (character.get(HT) + character.get(DX)) / 4
    }),
    BasicSpeedNoFractions({ character -> character.get(BasicSpeed100) / 100 }),
    BasicMove({ character -> character.get(BasicSpeed100) / 100 }),

    // ignores special case of BasicLift being less than 10lbs
    BasicLift({ character -> character.get(ST) * character.get(ST) / 5 })
}
