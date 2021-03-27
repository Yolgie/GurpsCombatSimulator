package at.cnoize.util.at.cnoize.gcs.app

import at.cnoize.gcs.app.character.BasicAttribute.DX
import at.cnoize.gcs.app.character.BasicAttribute.HT
import at.cnoize.gcs.app.character.BasicAttribute.IQ
import at.cnoize.gcs.app.character.BasicAttribute.ST
import at.cnoize.gcs.app.character.Character
import at.cnoize.gcs.app.character.SecondaryCharacteristic.BasicMove
import at.cnoize.gcs.app.character.SecondaryCharacteristic.BasicSpeed100
import at.cnoize.gcs.app.character.SecondaryCharacteristic.BasicSpeedNoFractions
import at.cnoize.gcs.app.character.SecondaryCharacteristic.FP
import at.cnoize.gcs.app.character.SecondaryCharacteristic.HP
import at.cnoize.gcs.app.character.SecondaryCharacteristic.Per
import at.cnoize.gcs.app.character.SecondaryCharacteristic.Will
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

class SecondaryCharacteristicsTest {

    @Test
    fun `test simple default derived characteristic`() {
        val basicCharacter = Character("Player")

        // hp, fp, will, per will all be the same as some primary attribute by default
        listOf(
            HP,
            FP,
            Will,
            Per
        ).forEach { characteristic ->
            val result = basicCharacter.get(characteristic)
            assertEquals(Character.defaultAttributeLevel, result)
        }

        mapOf(
            HP to ST,
            FP to HT,
            Will to IQ,
            Per to IQ
        ).forEach { (characteristic, attribute) ->
            val betterCharacter = Character("Player", mapOf(attribute to 13))
            val result = betterCharacter.get(characteristic)
            assertEquals(13, result)
        }
    }

    @Test
    fun `test basic speed & basic move`() {
        val basicCharacter = Character("Player")

        assertEquals(500, basicCharacter.get(BasicSpeed100))
        assertEquals(5, basicCharacter.get(BasicSpeedNoFractions))
        assertEquals(5, basicCharacter.get(BasicMove))

        val betterCharacters = listOf(
            Character("much DX", mapOf(DX to 15)),
            Character("much HT", mapOf(HT to 15)),
            Character("mixed DX", mapOf(DX to 14, HT to 11)),
            Character("mixed HT", mapOf(DX to 12, HT to 13)),
        )

        betterCharacters.forEach { betterCharacter ->
            assertEquals(625, betterCharacter.get(BasicSpeed100))
            assertEquals(6, betterCharacter.get(BasicSpeedNoFractions))
            assertEquals(6, betterCharacter.get(BasicMove))
        }
    }

    @TestFactory
    fun `test basic speed decimals`() = mapOf(
        8 to 450,
        9 to 475,
        10 to 500,
        11 to 525,
        12 to 550,
        13 to 575,
        14 to 600,
        15 to 625,
        16 to 650,
        17 to 675
    ).map { (dx, speed) ->
        dynamicTest("character with DX $dx should have speed $speed (/100)") {
            assertEquals(speed, Character("unnamed", mapOf(DX to dx)).get(BasicSpeed100))
        }
    }
}